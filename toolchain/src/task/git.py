import os
import shutil
import subprocess

from src.model.data import Env, ApkDescriptor
from src.task.base import BaseTask


def split_out(out):
    diffs = []
    diff = []
    for l in out.splitlines():
        if l.startswith(b'diff --git '):
            if diff:
                diffs.append(diff)
                diff = []
            diff.append(l)
        else:
            diff.append(l)
    if diff:
        diffs.append(diff)

    return diffs


def get_filename(split):
    l = split[0].decode().split()[2][2:]
    if l.startswith("smali/") or l.startswith("smali_"):
        l = l.split("/", 1)[1]

    return l.replace("/", ".") + ".patch"


def gen_patches(splits, out_dir):
    if not out_dir.exists():
        os.makedirs(out_dir.as_posix())

    for split in splits:
        filename = get_filename(split)
        with open(out_dir.joinpath(filename).as_posix(), 'wb') as fp:
            for line in split:
                fp.write(line)
                fp.write(b'\n')
        print("gen::{}".format(filename))


class GeneratePatches(BaseTask):
    __NAME__ = "Generate patches"

    def run(self, env: Env):
        with subprocess.Popen(["git", "diff", "--minimal", "--ignore-space-at-eol"], cwd=self._apk.decompile_dir,
                              stdout=subprocess.PIPE, stderr=subprocess.PIPE) as p:
            splits = split_out(p.stdout.read())
            gen_patches(splits, env.patches_dir.joinpath(self._apk.decompile_dir.name))

    def cancel(self):
        pass


class CreateGitRepo(BaseTask):
    __NAME__ = "Create git repo"

    def run(self, env: Env):
        shutil.copy(env.bin_dir.joinpath("gitignore"), self._apk.decompile_dir.joinpath(".gitignore"))
        subprocess.run(["git", "init"], cwd=self._apk.decompile_dir)
        subprocess.run(["git", "add", "."], cwd=self._apk.decompile_dir)
        subprocess.run(["git", "commit", "-m", "init"], cwd=self._apk.decompile_dir)

    def cancel(self):
        pass


class Restore(BaseTask):
    __NAME__ = "Restore"

    def run(self, env: Env):
        subprocess.run(["git", "restore", "*"], cwd=self._apk.decompile_dir)

    def cancel(self):
        pass


class ApplyPatches(BaseTask):
    __NAME__ = "Apply patches"

    def __init__(self, apk: ApkDescriptor, check: bool):
        super().__init__(apk)
        self._check = check
        if check:
            self.__NAME__ = "CheckGitPatches"

    def run(self, env: Env):
        name = self._apk.decompile_dir.name
        patches = env.patches_dir.joinpath(name)
        if not patches.exists():
            print("Patches not found")
            return

        for file in patches.iterdir():
            if file.is_file():
                try:
                    args = ["git", "apply", "--ignore-space-change", "--ignore-whitespace", file.as_posix()]
                    if self._check:
                        args.append("--check")
                    subprocess.run(args, cwd=self._apk.decompile_dir, shell=True, check=True, capture_output=True)
                    if not self._check:
                        print("{}: OK".format(file.name))
                except Exception as e:
                    if self._check:
                        print("{}\nFAILED [{}] --> \n{}{}".format('=' * 25, file.name, e.stderr.decode(), '=' * 25))
                    else:
                        print("{}: ERROR".format(file.name))

    def cancel(self):
        pass

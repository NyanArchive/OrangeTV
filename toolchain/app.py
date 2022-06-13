import argparse
import base64
import json
import os
import sys
from pathlib import Path

from pyaxmlparser import APK

from src.model.data import Env, ApkDescriptor
from src.task import apktool, internal, git, d2j
from src.task.base import BaseTask


def get_working_directory():
    return Path(os.getcwd())


def parse_file_arg(arg: str):
    if not arg:
        raise Exception("file is null")

    arg = arg.strip()
    if not arg:
        raise Exception("file is empty")

    if os.path.isabs(arg):
        fpath = Path(arg)
    else:
        if not arg.startswith("/apk") and not arg.startswith("\\apk"):
            arg = "apk{}{}".format(os.path.sep, arg)

        fpath = Path(get_working_directory(), arg)

    if fpath.exists():
        if fpath.is_file():
            return fpath

    raise FileNotFoundError(arg)


def get_path(src):
    if os.path.isabs(src):
        return Path(src)
    else:
        return Path(get_working_directory(), src)


def get_safe_path(src):
    fpath = get_path(src)

    if not fpath.exists():
        raise FileNotFoundError(fpath)

    return fpath


def parse_env():
    wd = get_working_directory()
    with open(os.path.join(wd, "etc/env.json"), "r", encoding="utf-8") as fp:
        js = json.load(fp)

    apktool = get_safe_path(os.path.join("bin", js['apktool']))
    apksigner = get_safe_path(os.path.join("bin", js['apksigner']))
    d2j = get_safe_path(os.path.join("bin", js['d2j']))

    return Env(apktool=apktool,
               apksigner=apksigner,
               d2j=d2j,
               out_dir=wd.joinpath("out"),
               signature=js['signature'],
               bin_dir=Path(wd, "bin"),
               patches_dir=Path(wd, "patches"),
               zipalign=Path(wd, "bin", "zipalign.exe"))


def get_apk_desc(src: Path):
    apk_container = APK(src.as_posix())
    if not apk_container.is_valid_APK():
        raise Exception("Invalid APK: {}".format(src))

    return ApkDescriptor(file_path=src,
                         decompile_dir=src.parent.joinpath(src.stem),
                         version_name=apk_container.version_name,
                         version_code=apk_container.version_code,
                         signature=base64.b64encode(apk_container.get_signature()).decode('ascii'),
                         out_apk=Path(get_working_directory(), "out", src.name))


def check_apk(env: Env, apk: ApkDescriptor):
    if env.signature != apk.signature:
        raise Exception("Bad apk signature!")


def done(msg):
    print(msg)
    sys.exit(0)


def die(msg):
    print(msg)
    sys.exit(1)


def run(tasks: [BaseTask], env: Env, restricted=True):
    if not tasks:
        done("Done!")

    for task in tasks:
        try:
            print("{}: running...".format(task.__NAME__))
            task.run(env=env)
            print("{}: done".format(task.__NAME__))
        except Exception as e:
            print(e)
            if restricted:
                raise e


def handle_args(args, env: Env, apk: ApkDescriptor):
    tasks = []

    if args.decompile:
        if apk.decompile_dir.exists() and not args.force:
            die("Use --force")

        tasks.append(internal.Cleanup(apk))
        tasks.append(apktool.DecompileApk(apk))
        tasks.append(git.CreateGitRepo(apk))
    if args.jar:
        tasks.append(d2j.GenerateJarFile(apk))
    if args.check:
        tasks.append(git.ApplyPatches(apk, check=True))
    if args.generate:
        tasks.append(git.GeneratePatches(apk))
    if args.apply:
        tasks.append(git.ApplyPatches(apk, check=False))
    if args.recompile:
        tasks.append(apktool.RecompileApk(apk))
        tasks.append(apktool.SignApk(apk))
    if args.restore:
        tasks.append(git.Restore(apk))
    if args.make:
        if apk.decompile_dir.exists() and not args.force:
            die("Use --force")

        tasks.append(internal.Cleanup(apk))
        tasks.append(apktool.DecompileApk(apk))
        tasks.append(git.CreateGitRepo(apk))
        tasks.append(git.ApplyPatches(apk, check=False))
        tasks.append(apktool.RecompileApk(apk))
        tasks.append(apktool.SignApk(apk))

    run(env=env, tasks=tasks)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='OrangeTV toolchain')
    parser.add_argument('-f', '--file', type=parse_file_arg, required=True)
    parser.add_argument('--force', action='store_true')

    group = parser.add_mutually_exclusive_group()
    group.add_argument('--decompile', action='store_true')
    group.add_argument('--jar', action='store_true')
    group.add_argument('--check', action='store_true')
    group.add_argument('--generate', action='store_true')
    group.add_argument('--apply', action='store_true')
    group.add_argument('--recompile', action='store_true')
    group.add_argument('--restore', action='store_true')
    group.add_argument('--make', action='store_true')

    args = parser.parse_args()

    env = parse_env()
    apk = get_apk_desc(args.file)

    check_apk(env, apk)
    handle_args(args=args, env=env, apk=apk)

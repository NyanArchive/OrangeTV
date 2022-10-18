import argparse
import base64
import json
import os
from datetime import datetime
from pathlib import Path

from pyaxmlparser import APK

from src.model.data import Env, ApkDescriptor
from src.task import apktool, internal, git, d2j
from src.task.base import BaseTask
from src.util import get_working_directory, get_safe_path, done, print_title, die


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


def parse_env():
    wd = get_working_directory()
    with open(os.path.join(wd, "etc/env.json"), "r", encoding="utf-8") as fp:
        js = json.load(fp)

    apktool = get_safe_path(os.path.join("bin", js['apktool']))
    apksigner = get_safe_path(os.path.join("bin", js['apksigner']))

    return Env(apktool=apktool,
               apksigner=apksigner,
               out_dir=wd.joinpath("out"),
               signature=js['signature'],
               bin_dir=Path(wd, "bin"),
               patches_dir=Path(wd, "patches"),
               zipalign=Path(wd, "bin", "zipalign.exe"),
               app_dir=Path(wd).parent.joinpath("app"),
               lib_dir=Path(wd).parent.joinpath("lib"),
               build=Path(wd, "etc").joinpath("build.json"),
               d2j=Path(wd, "bin/dex2jar"))


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


def run(tasks: [BaseTask], env: Env, restricted=True):
    if not tasks:
        done("Done!")

    for task in tasks:
        try:
            print_title("[TASK] {}".format(task.__NAME__))
            task.run(env=env)
            print("[TASK] {}: Done".format(task.__NAME__))
        except Exception as e:
            print(e)
            if restricted:
                raise e


def create_decompile_tasks(tasks):
    tasks.append(internal.Cleanup(apk))
    tasks.append(apktool.DecompileApk(apk))
    tasks.append(apktool.FixAnnotations(apk))
    tasks.append(apktool.FixClasses(apk))
    tasks.append(git.CreateGitRepo(apk))


def create_recompile_tasks(tasks):
    tasks.append(internal.CopySo(apk))
    tasks.append(internal.InjectRes(apk))
    tasks.append(internal.IncreaseBuildNumber(apk))
    tasks.append(apktool.RecompileApk(apk))
    tasks.append(internal.BuildAppDex(apk))
    tasks.append(internal.InjectAppDexs(apk))
    tasks.append(apktool.SignApk(apk))


def handle_args(args, env: Env, apk: ApkDescriptor):
    tasks = []

    if args.decompile:
        if apk.decompile_dir.exists() and not args.force:
            die("Use --force")

        create_decompile_tasks(tasks)
    if args.jar:
        tasks.append(d2j.GenerateJarFile(apk))
    if args.check:
        tasks.append(git.ApplyPatches(apk, check=True))
    if args.generate:
        tasks.append(git.GeneratePatches(apk))
    if args.apply:
        tasks.append(git.ApplyPatches(apk, check=False))
    if args.recompile:
        create_recompile_tasks(tasks)
    if args.debug:
        tasks.append(internal.InjectRes(apk))
    if args.restore:
        tasks.append(git.Restore(apk))
    if args.make:
        if apk.decompile_dir.exists() and not args.force:
            die("Use --force")

        create_decompile_tasks(tasks)
        tasks.append(git.ApplyPatches(apk, check=False))
        create_recompile_tasks(tasks)

    if args.install:
        tasks.append(internal.Install(apk))

    run(env=env, tasks=tasks)


def get_apk_filepath(path):
    if path:
        return path

    apk_dir = Path(get_working_directory()).joinpath('apk')
    if not apk_dir.exists():
        die("apk dir not found")

    for p in apk_dir.iterdir():
        if p.is_file() and p.name.endswith('.apk'):  # get first file
            return p

    die("apk not found")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='OrangeTV toolchain')
    parser.add_argument('-f', '--file', type=parse_file_arg)
    parser.add_argument('--force', action='store_true')
    parser.add_argument('--install', action='store_true')

    group = parser.add_mutually_exclusive_group()
    group.add_argument('--decompile', action='store_true', help="Decompile apk and create git repo")
    group.add_argument('--jar', action='store_true', help="Convert apk file to twitch jar file using enjarify")
    group.add_argument('--check', action='store_true', help="Check patches")
    group.add_argument('--generate', action='store_true', help="Generate patches")
    group.add_argument('--apply', action='store_true', help="Apply patches")
    group.add_argument('--recompile', action='store_true', help="Recompile apk")
    group.add_argument('--restore', action='store_true')
    group.add_argument('--make', action='store_true')
    group.add_argument('--debug', action='store_true')

    args = parser.parse_args()

    env = parse_env()
    start = datetime.now()
    apk_fp = get_apk_filepath(args.file)
    print("Current APK: {}".format(apk_fp))
    apk = get_apk_desc(apk_fp)

    handle_args(args=args, env=env, apk=apk)

    end = datetime.now()
    print("Total: {}s".format((end - start).total_seconds()))

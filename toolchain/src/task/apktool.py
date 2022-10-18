import os
import shutil
import subprocess
from pathlib import Path

import src.util
from src.model.data import Env
from src.task.base import BaseTask

JAVA_COMMAND = ["java", "-Xms512m", "-Xmx2048m", "-jar"]


class DecompileApk(BaseTask):
    __NAME__ = "DecompileApk"

    def run(self, env: Env):
        subprocess.run(
            [*JAVA_COMMAND, env.apktool.as_posix(),
             "-p", env.bin_dir.joinpath("framework").as_posix(),
             "d", "-f", "--keep-broken-res", "--use-aapt2",
             self._apk.file_path.as_posix(),
             "-o", self._apk.decompile_dir.as_posix()]
        )

    def cancel(self):
        pass


class RecompileApk(BaseTask):
    __NAME__ = "RecompileApk"

    def run(self, env: Env):
        subprocess.run(
            [*JAVA_COMMAND, env.apktool.as_posix(),
             "-p", env.bin_dir.joinpath("framework").as_posix(), "--use-aapt2",
             "b", self._apk.decompile_dir.as_posix(), "-o", self._apk.out_apk]
        )

    def cancel(self):
        pass


def fix_annotations(file: Path):
    with open(file.as_posix(), 'rb') as fp:
        content = fp.read()

    if b'&lt;annotation ' not in content:
        return

    print("Fix: {}".format(file.as_posix()))
    with open(file.as_posix(), 'wb') as fp:
        fp.write(content.replace(b'&lt;annotation ', b'<annotation ').replace(b'&lt;/annotation>', b'</annotation>'))


def get_last_dex_num(path: Path):
    last = 0
    for file in path.iterdir():
        if file.is_dir():
            if file.name.startswith("smali_classes"):
                num = file.name[13:]
                if num.isdecimal():
                    val = int(num)
                    if val > last:
                        last = val

    return last


def move_classes(src, dst):
    if src.exists():
        new_dir = dst
        if not new_dir.exists():
            new_dir.mkdir(parents=True)
        shutil.move(src.as_posix(), new_dir.as_posix())
        print("{}: OK".format(src))
    else:
        print("{}: not found".format(dst))


class FixClasses(BaseTask):
    __NAME__ = "FixClasses"

    def run(self, env: Env):
        last_dex_num = get_last_dex_num(self._apk.decompile_dir)
        md = Path(self._apk.decompile_dir).joinpath("smali_classes{}".format(last_dex_num + 1))
        md.mkdir()
        print("Moving classes to {}".format(md.as_posix()))
        move_classes(Path(self._apk.decompile_dir).joinpath("smali_classes2/com/fasterxml"),
                     md.joinpath("com"))
        move_classes(Path(self._apk.decompile_dir).joinpath("smali_classes4/jp"), md)
        move_classes(Path(self._apk.decompile_dir).joinpath("smali_classes5/tv/twitch/android/models"),
                     md.joinpath("tv/twitch/android"))
        move_classes(Path(self._apk.decompile_dir).joinpath("smali/berlin/volders/badger"),
                     md.joinpath("berlin/volder"))

    def cancel(self):
        pass


class FixAnnotations(BaseTask):
    __NAME__ = "FixAnnotations"

    def run(self, env: Env):
        for file in src.util.get_files(self._apk.decompile_dir.joinpath("res")):
            if file.parent.name == 'values' or file.parent.name.startswith('values-'):
                if file.name == 'plurals.xml':
                    fix_annotations(file)

    def cancel(self):
        pass


class SignApk(BaseTask):
    __NAME__ = "SignApk"

    def run(self, env: Env):
        key_path = Path(os.environ.get("ORANGE_TV_SIGN_DIR", env.bin_dir))
        print("Key path: {}".format(key_path))
        out_apk = self._apk.out_apk

        aligned = out_apk.parent.joinpath(out_apk.stem + "_aligned.apk")
        signed = out_apk.parent.joinpath(out_apk.stem + "_signed.apk")
        dig = Path(signed.parent, signed.name + ".idsig")

        subprocess.run([env.zipalign, "-f", "-p", "4", out_apk.as_posix(), aligned])
        subprocess.run(
            [*JAVA_COMMAND, env.apksigner.as_posix(), "sign",
             "--key", key_path.joinpath("sign.pk8"), "--cert", key_path.joinpath("sign.x509.pem"),
             "--out", signed, aligned]
        )

        aligned.unlink()
        out_apk.unlink()
        dig.unlink()

        signed.rename(out_apk.as_posix())

    def cancel(self):
        pass

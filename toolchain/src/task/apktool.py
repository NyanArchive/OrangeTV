import os
import subprocess
from pathlib import Path

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
        self._apk.out_apk = signed

    def cancel(self):
        pass

import json
import shutil
import subprocess
import zipfile
from datetime import datetime
from pathlib import Path

from pyaxmlparser import APK

from src.model.data import Env
from src.task.base import BaseTask


class Cleanup(BaseTask):
    __NAME__ = "Cleanup"

    def run(self, env: Env):
        if self._apk.decompile_dir.exists():
            if self._apk.decompile_dir.is_file():
                raise Exception("Cannot decompile apk: {} is file ".format(self._apk.decompile_dir))
            else:
                self._apk.decompile_dir.unlink()

    def cancel(self):
        pass


class BuildAppDex(BaseTask):
    __NAME__ = "BuildAppDex"

    def run(self, env: Env):
        subprocess.run(
            [env.app_dir.joinpath("gradlew.bat").as_posix(), "genDex"],  # FIXME: windows only
            cwd=env.app_dir.as_posix()
        )

    def cancel(self):
        pass


def is_app_dex_filename(name):
    if not name.startswith("classes"):
        return False

    if not name.endswith(".dex"):
        return False

    index: str = name[7:-4]

    if not index:
        return True

    if index.isdecimal():
        return True

    return False


def get_number(name: str):
    index: str = name[7:-4]

    if not index:
        return 1

    return int(index)


def get_last_dex_number(zip: APK):
    highest = 0
    for name in zip.get_files():
        if '/' in name:
            continue

        if is_app_dex_filename(name):
            current = get_number(name)
            if current > highest:
                highest = current

    return highest


def format_dex_filename(index):
    return "classes{}.dex".format(index) if index > 1 else "classes.dex"


def inject_dex(dex_path, last_index, zip):
    app_dex_name = format_dex_filename(last_index + 1)
    if dex_path is zipfile.Path:
        dex_path = dex_path.as_posix()

    print("Injecting {} [{}]...".format(dex_path, app_dex_name))
    last_index += 1
    zip.write(dex_path, app_dex_name)


class InjectRes(BaseTask):
    __NAME__ = "InjectRes"

    def run(self, env: Env):
        shutil.copytree(env.app_dir.joinpath("models/src/main/res").as_posix(),
                        self._apk.decompile_dir.joinpath("res").as_posix(),
                        dirs_exist_ok=True)
        print("OK")

    def cancel(self):
        pass


class CopySo(BaseTask):
    __NAME__ = "CopySo"

    def run(self, env: Env):
        shutil.copytree(env.lib_dir.joinpath("so").as_posix(), self._apk.decompile_dir.as_posix(), dirs_exist_ok=True)
        print("OK")

    def cancel(self):
        pass


class InjectAppDexs(BaseTask):
    __NAME__ = "InjectAppDexs"

    def run(self, env: Env):
        apk = APK(self._apk.out_apk.as_posix())
        last_index = get_last_dex_number(apk)

        writer = zipfile.ZipFile(self._apk.out_apk.as_posix(), mode="a", compression=zipfile.ZIP_DEFLATED)

        files = [i.as_posix() for i in env.lib_dir.iterdir() if i.name.endswith(".dex")]
        for path in [env.app_dir.joinpath("app/dex/app.dex").as_posix(), *files]:
            inject_dex(path, last_index, writer)
            last_index += 1

        writer.close()

    def cancel(self):
        pass


class IncreaseBuildNumber(BaseTask):
    __NAME__ = "IncreaseBuildNumber"
    BUILD_FILENAME = "build.json"

    def run(self, env: Env):
        if not env.build.exists():
            print("%s not found" % self.BUILD_FILENAME)
            return

        with open(env.build, "r", encoding="utf-8") as fp:
            js = json.load(fp)

        js["number"] += 1
        js['timestamp'] = int(datetime.now().timestamp())

        with open(env.build, "w", encoding="utf-8") as fp:
            json.dump(js, fp)

        shutil.copy(env.build.as_posix(), self._apk.decompile_dir.joinpath("assets").joinpath(self.BUILD_FILENAME))
        self._apk.out_apk = Path(self._apk.out_apk.parent, "PurpleTV_b{}_{}.apk".format(js["number"], self._apk.version_code))

    def cancel(self):
        pass

class Install(BaseTask):
    __NAME__ = "Install"

    def run(self, env: Env):
        subprocess.run(["adb", "install", "-r", self._apk.out_apk])

    def cancel(self):
        pass

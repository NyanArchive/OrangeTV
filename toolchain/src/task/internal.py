import subprocess
import zipfile

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


class InjectAppDexs(BaseTask):
    __NAME__ = "InjectAppDexs"

    def run(self, env: Env):
        apk = APK(self._apk.out_apk.as_posix())
        last_index = get_last_dex_number(apk)

        writer = zipfile.ZipFile(self._apk.out_apk.as_posix(), mode="a", compression=zipfile.ZIP_DEFLATED)

        for path in [env.app_dir.joinpath("app/dex/app.dex")]:
            inject_dex(path, last_index, writer)
            last_index += 1

        writer.close()

    def cancel(self):
        pass

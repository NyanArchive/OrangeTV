import subprocess

from src.model.data import Env
from src.task.base import BaseTask


def build_d2j_cp(env: Env):
    libs = env.d2j.joinpath("lib")
    return ";".join([i.as_posix() for i in libs.iterdir() if i.is_file() and i.name.endswith(".jar")])


class GenerateJarFile(BaseTask):
    __NAME__ = "Generate twitch jar file using dex2jar"

    def run(self, env: Env):
        cp = build_d2j_cp(env)
        out_file = env.out_dir.joinpath("tv.twitch.android.app.{}.jar".format(self._apk.version_code))
        subprocess.run(
            ["java", "-Xms512m", "-Xmx2048m", "-cp", cp,
             "com.googlecode.dex2jar.tools.Dex2jarCmd", self._apk.file_path.as_posix(),
             "-o", out_file.as_posix(), "--force"]
        )

    def cancel(self):
        pass

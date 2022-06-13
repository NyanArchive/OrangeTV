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

import abc

from src.model.data import Env, ApkDescriptor


class BaseTask(metaclass=abc.ABCMeta):
    __NAME__ = "BASE"

    def __init__(self, apk: ApkDescriptor):
        self._apk = apk

    @abc.abstractmethod
    def run(self, env: Env):
        raise NotImplementedError()

    @abc.abstractmethod
    def cancel(self):
        raise NotImplementedError()

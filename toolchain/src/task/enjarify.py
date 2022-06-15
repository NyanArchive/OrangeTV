import collections
import itertools
import zipfile
from copy import copy
from pathlib import Path

from bin.enjarify import parsedex
from bin.enjarify.jvm import writeclass
from bin.enjarify.jvm.optimization import options
from bin.enjarify.mutf8 import decode
from src.model.data import Env
from src.task.base import BaseTask


def read(fname, mode='rb'):
    with open(fname, mode) as f:
        return f.read()


def translate(data, opts, classes, errors):
    dex = parsedex.DexFile(data)

    for cls in dex.classes:
        # skip non-twitch packages
        if not cls.name.startswith(b'tv/twitch/'):
            continue

        unicode_name = decode(cls.name) + '.class'
        if unicode_name in classes:
            print('Warning, duplicate class name', unicode_name)
            continue

        try:
            class_data = writeclass.toClassFile(cls, opts)
            classes[unicode_name] = class_data
        except Exception:
            next(errors)

        if not (len(classes) + next(copy(errors))) % 100:
            print(len(classes), 'classes processed')

    return classes, errors


def write_to_jar(fname, classes):
    with zipfile.ZipFile(fname, 'w') as out:
        for unicode_name, data in classes.items():
            # Don't bother compressing small files
            compress_type = zipfile.ZIP_DEFLATED if len(data) > 10000 else zipfile.ZIP_STORED
            info = zipfile.ZipInfo(unicode_name)
            info.external_attr = 0o775 << 16  # set Unix file permissions
            out.writestr(info, data, compress_type=compress_type)


class GenerateJarFile(BaseTask):
    __NAME__ = "Generate twitch jar file"

    def run(self, env: Env):
        dexs = []
        with zipfile.ZipFile(self._apk.file_path.as_posix(), 'r') as z:
            for name in z.namelist():
                if name.startswith('classes') and name.endswith('.dex'):
                    dexs.append(z.read(name))

        out_file = Path(self._apk.out_apk.parent, "tv.twitch.android.app.jar")
        if out_file.exists():
            out_file.unlink()

        writer = open(out_file.as_posix(), 'wb')

        opts = options.NONE
        classes = collections.OrderedDict()
        errors = itertools.count()
        for data in dexs:
            translate(data, opts=opts, classes=classes, errors=errors)

        write_to_jar(writer, classes)
        writer.close()
        print('Output written to', out_file.as_posix())

        print('{} classes translated successfully, {} classes had errors'.format(len(classes), next(copy(errors))))

    def cancel(self):
        pass

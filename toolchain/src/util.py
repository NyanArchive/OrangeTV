import os
import sys
from pathlib import Path


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


def get_working_directory():
    return Path(os.getcwd())


def done(msg):
    print(msg)
    sys.exit(0)


def die(msg):
    print(msg)
    sys.exit(1)


def print_title(name, size=100):
    c = size - len(name)
    l_half = c // 2
    r_half = c - l_half
    print("*" * l_half, name, "*" * r_half)


def get_files(path: Path):
    if path.is_file():
        yield path
    else:
        for i in path.iterdir():
            yield from get_files(i)

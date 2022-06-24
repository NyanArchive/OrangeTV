import dataclasses
from pathlib import Path


@dataclasses.dataclass
class Env:
    apktool: Path
    apksigner: Path
    zipalign: Path
    out_dir: Path
    bin_dir: Path
    app_dir: Path
    patches_dir: Path
    lib_dir: Path
    signature: str


@dataclasses.dataclass
class ApkDescriptor:
    file_path: Path
    decompile_dir: Path
    out_apk: Path
    version_code: int
    version_name: str
    signature: str

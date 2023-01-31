import sys


def java_string_hashcode(s):
    h = 0
    for c in s:
        h = int((((31 * h + ord(c)) ^ 0x80000000) & 0xFFFFFFFF) - 0x80000000)
    return h


def replace(org):
    return org.replace("tv.twitch.android.media.action.",
                       "tv.orange.media.action.").replace("tv.twitch.android.push.",
                                                    "tv.orange.push.")


if __name__ == '__main__':
    args = sys.argv[1:]
    for org in args:
        new = replace(org)
        if new != org:
            hash_org = java_string_hashcode(org)
            hash_new = java_string_hashcode(new)

            print("{} -> {}".format(org, new))
            print("{} -> {}".format(hex(hash_org), hex(hash_new)))

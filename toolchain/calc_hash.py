import sys


def java_string_hashcode(s):
    h = 0
    for c in s:
        h = int((((31 * h + ord(c)) ^ 0x80000000) & 0xFFFFFFFF) - 0x80000000)
    return h


if __name__ == '__main__':
    args = sys.argv[1:]
    for string in args:
        mod = string.replace("tv.twitch.android.media.action.", "tv.orange.action.").replace("tv.twitch.android.push.", "tv.orange.push.")
        print(string + "->" + mod)
        hash_org = java_string_hashcode(string)
        hash_mod = java_string_hashcode(mod)

        print("{} -> {}".format(hex(hash_org), hex(hash_mod)))

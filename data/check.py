import sys


def check(path1, path2):
    set1, set2 = set(), set()
    with open(path1, "r") as f:
        line = f.readline().strip()
        while line:
            set1.add(line)
            line = f.readline().strip()
    with open(path2, "r") as f:
        line = f.readline().strip()
        while line:
            set2.add(line)
            line = f.readline().strip()

    print("{0} {1}".format(path1, len(set1)))
    print("{0} {1}".format(path2, len(set2)))
    print("{0} {1}".format("sames", len(set1 & set2) +
          len(list(filter(lambda x: x.split()[0] == x.split()[1], [i for i in (set1-(set1 & set2))])))))


if __name__ == "__main__":
    check(sys.argv[1], sys.argv[2])

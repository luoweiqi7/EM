from pathlib import Path
import subprocess
import re
import random
import time
import os
from typing import Dict, List, Set, Tuple
import json

JAVA_EXEC = "/usr/bin/java"
xm_io, hw_io, ps_io = None, None, None
with open("data/xiaomi/xiaomi_in_out.json", "r") as f:
    xm_io = json.load(f)
with open("data/huawei/huawei_in_out.json", "r") as f:
    hw_io = json.load(f)
with open("data/play_store/play_store_in_out.json", "r") as f:
    ps_io = json.load(f)


# 计算预测的结果指标
def check(target_path, base_path):
    target_set = set()
    base_set = set()
    recall = 0.0
    with open(target_path, "r") as f:
        lines = f.readlines()
        for line in lines:
            target_set.add(" ".join(line.strip().split(";;;;;;")))
    with open(base_path, "r") as f:
        lines = f.readlines()
        for line in lines:
            base_set.add(line.strip())
    total = len(target_set)
    correct = len(base_set & target_set)
    precision = correct / total
    recall = correct / len(base_set)
    f1 = 2 * (precision * recall) / (precision + recall)
    return total, correct, precision, recall, f1


# 自动选择出入度大的实体对作为种子
def select(seed_path, base_path, num, io1, io2):
    all_sames = set()

    with open(base_path, "r") as f:
        lines = f.readlines()
        for line in lines:
            if (len(line) < 2):
                continue
            all_sames.add(";;;;;;".join(line.strip().split(" ")))
    all_sames1 = list(filter(lambda x: len(x) == 2, [
        i.split(";;;;;;") for i in all_sames]))
    all_sames1.sort(key=lambda x: io1[x[0]][0] +
                                  io1[x[0]][1] + io2[x[1]][0] + io2[x[1]][1], reverse=True)
    with open(seed_path, "w") as f:
        seeds = all_sames1[:num]
        for seed in seeds:
            f.write("{0} {1}\n".format(*seed))
    res = "种子实体对在各自商店的出度和入度: \n"
    for a, b in seeds:
        res += "{0} (出度 : {1} , 入度 : {2}) <-----> {3} (出度 : {4} , 入度 : {5}) \n".format(
            a, io1[a][1], io1[a][0], b, io2[b][1], io2[b][0]
        )
    return res


# 通过jar包进行算法调用
def run_command(config_path):
    t1 = time.time()

    obj = subprocess.Popen(
        [JAVA_EXEC, "-Dfile.encoding=utf-8", "-Xmx100000m", "-jar", "data/entity_matching.jar", config_path],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, universal_newlines=True)
    assert obj != None
    obj.wait()
    obj.stdout.read()
    obj.stdout.close()

    used_time = time.time() - t1
    return used_time


def run(seed_path, config_path, work_space_folder, same_txt, io1, io2):
    for i in [1, 5, 10]:
        select_res = select(seed_path, same_txt, i, io1, io2)
        obj = subprocess.Popen(["rm", "-rf", work_space_folder],
                               stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               universal_newlines=True)
        obj.wait()
        obj.stdout.read()
        obj.stdout.close()
        obj = subprocess.Popen(["mkdir", work_space_folder],
                               stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               universal_newlines=True)
        obj.wait()
        obj.stdout.read()
        obj.stdout.close()
        run_time = run_command(config_path)

        p = Path(work_space_folder)
        l = list(map(lambda x: x.name, list(p.glob("seeds*"))))
        l.sort()
        total, correct, precision, recall, f1 = check(os.path.join(
            work_space_folder, l[-1]), same_txt)
        print("------------------------------------------\n")
        print(
            "{0} : seeds num:{1} ; total:{2} ; correct:{3} ; precision :{4} ; recall:{5} ; f1:{6} ; run time:{7}\n{8}".format(
                work_space_folder, i, total, correct, precision, recall, f1, run_time, select_res
            ))
        obj = subprocess.Popen(["mv", work_space_folder, work_space_folder[:-1] + "seed{}/".format(i)],
                               stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               universal_newlines=True)
        obj.wait()
        obj.stdout.read()
        obj.stdout.close()


# run_command("configs/play_store_huawei_config")

if __name__ == '__main__':
    run("data/seeds/xiaomi_huawei_sameas.txt",
        "configs/xiaomi_huawei_configure",
        "workspace/xiaomi_huawei/",
        "data/xm_hw_sames.txt", xm_io, hw_io)

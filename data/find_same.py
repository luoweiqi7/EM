import json
fxm = open("/home/zhouheng/jsonToTriples/rawData/xiaomi.json", "r")
xm = json.load(fxm)
fxm.close()
fhw = open("/home/zhouheng/jsonToTriples/rawData/huawei.json", "r")
hw = json.load(fhw)
fhw.close()
fps = open("/home/zhouheng/jsonToTriples/rawData/play_store1.json", "r")
ps = json.load(fps)
fps.close()

xm_package_app_dict, xm_name_app_dict = {}, {}
for i in xm:
    xm_package_app_dict[i["包名"]] = i
    xm_name_app_dict[i["名称"].strip().replace(
        " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")] = i

hw_package_app_dict, hw_name_app_dict = {}, {}
for i in hw:
    hw_package_app_dict[i["包名"]] = i
    hw_name_app_dict[i["名称"].strip().replace(
        " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")] = i

ps_package_app_dict, ps_name_app_dict = {}, {}
for i in ps:
    ps_package_app_dict[i["包名"]] = i
    ps_name_app_dict[i["名称"].strip().replace(
        " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")] = i

xm_hw_same, ps_xm_same, ps_hw_same = set(), set(), set()

for i in set(xm_package_app_dict.keys()) & set(hw_package_app_dict.keys()):
    xm_hw_same.add("{0} {1}".format(
        xm_package_app_dict[i]["名称"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_package_app_dict[i]["名称"].strip().replace(" ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")))
    xm_hw_same.add("{0} {1}".format(
        xm_package_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_package_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
for i in set(xm_name_app_dict.keys()) & set(hw_name_app_dict.keys()):
    xm_hw_same.add("{0} {0}".format(i))
    xm_hw_same.add("{0} {1}".format(
        xm_name_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_name_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
for i in set(ps_package_app_dict.keys()) & set(hw_package_app_dict.keys()):
    ps_hw_same.add("{0} {1}".format(
        ps_package_app_dict[i]["名称"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_package_app_dict[i]["名称"].strip().replace(" ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")))
    ps_hw_same.add("{0} {1}".format(
        ps_package_app_dict[i]["提供者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_package_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
for i in set(ps_name_app_dict.keys()) & set(hw_name_app_dict.keys()):
    ps_hw_same.add("{0} {0}".format(i))
    ps_hw_same.add("{0} {1}".format(
        ps_name_app_dict[i]["提供者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        hw_name_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
for i in set(ps_package_app_dict.keys()) & set(xm_package_app_dict.keys()):
    ps_xm_same.add("{0} {1}".format(
        ps_package_app_dict[i]["名称"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        xm_package_app_dict[i]["名称"].strip().replace(" ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")))
    ps_xm_same.add("{0} {1}".format(
        ps_package_app_dict[i]["提供者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        xm_package_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
for i in set(ps_name_app_dict.keys()) & set(xm_name_app_dict.keys()):
    ps_xm_same.add("{0} {0}".format(i))
    ps_xm_same.add("{0} {1}".format(
        ps_name_app_dict[i]["提供者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_"),
        xm_name_app_dict[i]["开发者"].strip().replace(
            " ", "_").replace("\t", "_").replace("\r", "_").replace("\n", "_")
    ))
with open("/home/zhouheng/workspace/EM/entity_matching/data/xm_hw_sames.txt", "w") as f:
    for i in xm_hw_same:
        f.write(i+"\n")

with open("/home/zhouheng/workspace/EM/entity_matching/data/ps_hw_sames.txt", "w") as f:
    for i in ps_hw_same:
        f.write(i+"\n")
with open("/home/zhouheng/workspace/EM/entity_matching/data/ps_xm_sames.txt", "w") as f:
    for i in ps_xm_same:
        f.write(i+"\n")

hw_triples = []
with open("/home/zhouheng/workspace/EM/entity_matching/data/huawei/huawei.nt", "r") as f:
    line = f.readline()
    while line:
        line1 = line.strip()
        temp = line1.split(";;;;;;")
        hw_triples.append(line1)
        if temp[1] == "开发者":
            hw_triples.append(
                "{0};;;;;;{1};;;;;;{2}".format(temp[2], "开发", temp[0]))
        line = f.readline()
with open("/home/zhouheng/workspace/EM/entity_matching/data/huawei/huawei.nt", "w") as f:
    for line in hw_triples:
        f.write(line+"\n")

xm_triples = []
with open("/home/zhouheng/workspace/EM/entity_matching/data/xiaomi/xiaomi.nt", "r") as f:
    line = f.readline()
    while line:
        line1 = line.strip()
        temp = line1.split(";;;;;;")
        xm_triples.append(line1)
        if temp[1] == "开发者":
            xm_triples.append(
                "{0};;;;;;{1};;;;;;{2}".format(temp[2], "开发", temp[0]))
        line = f.readline()
with open("/home/zhouheng/workspace/EM/entity_matching/data/xiaomi/xiaomi.nt", "w") as f:
    for line in hw_triples:
        f.write(line+"\n")
ps_triples = []
with open("/home/zhouheng/workspace/EM/entity_matching/data/play_store/play_store.nt", "r") as f:
    line = f.readline()
    while line:
        line1 = line.strip()
        temp = line1.split(";;;;;;")
        ps_triples.append(line1)
        if temp[1] == "提供者":
            ps_triples.append(
                "{0};;;;;;{1};;;;;;{2}".format(temp[2], "开发", temp[0]))
        line = f.readline()
with open("/home/zhouheng/workspace/EM/entity_matching/data/play_store/play_store.nt", "w") as f:
    for line in ps_triples:
        f.write(line+"\n")

import json
import csv

# 从文件中读取JSON数据
with open('the_json_file.json', 'r', encoding='utf-8') as file:
    data = json.load(file)

with open('the_target_file.csv', 'w', newline='', encoding='utf-8') as out_file:

    csvwriter = csv.writer(out_file, quoting = csv.QUOTE_MINIMAL)

    # 写入表头
    csvwriter.writerow(['filed1', 'filed2', 'filed3'])

    # 确保输出类型为文本
    def write_row(value1, value2, value3):
        csvwriter.writerow([str(value1), str(value2), str(value3)])

    # 获取json命中的数据项，匹配规则如下所示：
    # 匹配对象类型，get('target_filed_object', {})
    # 匹配集合类型，get('target_filed_list', [])
    # 匹配字符串类型，get('target_filed')
    hits = data.get('target_filed_object', {}).get('target_filed_list', [])

    # 遍历数组内的数据
    for idx, hit in enumerate(hits):
        hit_data1 = hit.get('target_filed_object',{}).get('filed1')
        hit_data2 = hit.get('target_filed_object',{}).get('filed2')
        hit_data3 = hit.get('target_filed_object',{}).get('filed3')

        # 输出到csv文件内
        write_row(hit_data1, hit_data2, hit_data3)

print('json文件成功解析并输出到data.csv文件内！')

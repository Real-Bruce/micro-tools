import os
import re

def find_urls_in_file(file_path):
    """读取文件并返回所有匹配的URL列表"""
    urls = []
    # 定义URL的正则表达式模式
    url_pattern = re.compile(r'`([^`]*)`')

    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            # 查找所有匹配的URL
            urls = url_pattern.findall(content)
    except (UnicodeDecodeError, IOError):
        print(f"无法读取文件或文件编码不正确: {file_path}")

    return urls

def find_urls_in_directory(directory):
    """遍历目录下的所有文件并收集所有URL"""
    all_urls = []

    for root, dirs, files in os.walk(directory):
        for file in files:
            file_path = os.path.join(root, file)
            urls = find_urls_in_file(file_path)
            all_urls.extend(urls)

    return all_urls

def write_urls_to_file(urls, output_file):
    """将URL列表写入到输出文件中"""
    with open(output_file, 'w', encoding='utf-8') as file:
        for url in urls:
            file.write(url + '\n')

def main(input_directory, output_file):
    urls = find_urls_in_directory(input_directory)
    write_urls_to_file(urls, output_file)
    print(f"已将所有URL写入到文件: {output_file}")

if __name__ == "__main__":
    # 设置输入目录和输出文件路径
    input_directory = "input_directory"  # 替换为你的输入文件夹路径
    output_file = "output_file"  # 你想保存URL的输出文件名

    main(input_directory, output_file)

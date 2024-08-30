import csv
import requests
import os

def download_files_from_csv(csv_file, download_dir):
    # 创建下载目录
    if not os.path.exists(download_dir):
        os.makedirs(download_dir)

    # 打开CSV文件并读取链接
    with open(csv_file, 'r', newline='', encoding='gbk') as file:
        csv_reader = csv.reader(file)
        next(csv_reader)  # 跳过标题行，如果有的话
        for row in csv_reader:
            file_url = row[0].strip()  # 假设链接在第一列
            download_file(file_url, download_dir)

def download_file(url, download_dir):
    # 从URL获取文件名
    file_name = url.split('/')[-1]
    file_path = os.path.join(download_dir, file_name)

    # 下载文件
    try:
        print(f"Downloading, {file_name}...")
        response = requests.get(url)
        if response.status_code == 200:
            with open(file_path, 'wb') as f:
                f.write(response.content)
            print(f"{file_name}, downloaded successfully.")
        else:
            print(f"Failed to download {file_name}. Status code: {response.status_code}")
    except Exception as e:
        print(f"Failed to download {file_name}. Exception: {str(e)}")

if __name__ == "__main__":
    csv_file = 'target_file.csv'  # CSV文件路径
    download_dir = 'download_file_dir'  # 下载目录
    download_files_from_csv(csv_file, download_dir)

package com.bruce;

/**
 * @author Bruce
 * @create 2023/12/13
 * @description 主程序入口
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=============启动下载任务===========");
        long startTime = System.currentTimeMillis();
        try {
//            DownloadJar.downloadFile();
            String url = "http://127.0.0.1:8080";
            String repository = "test";
            String localPatch = "E:\\nexus_path\\test\\";
            new DownloadJarByUrl().downloadFileWithPath(url, repository, localPatch);
        } catch (Exception e) {
            System.out.println("==============出现异常==============");
            e.printStackTrace();
        }

        System.out.println("==============下载任务结束，耗时：" + (System.currentTimeMillis() - startTime)  + "ms==========");

    }
}

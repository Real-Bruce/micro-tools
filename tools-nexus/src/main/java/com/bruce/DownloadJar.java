package com.bruce;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruce
 * @create 2023/12/08
 * @description 下载nexus指定仓库的全部jar包
 */
public abstract class DownloadJar {
    // 指定下载到本地路径
    private static final String NEXUS_PATH = "E:\\nexus_path\\";
    // 待解析JSON文件路径
    private static final String JSON_FILE_PATH = "E:\\workspace\\workspace05\\tools-nexus\\src\\main\\resources\\nexus.json";

    /**
     * 启动任务
     */
    public static void main(String[] args) {
        System.out.println("=============启动下载任务===========");
        long startTime = System.currentTimeMillis();
        try {
            DownloadJar.downloadFile();
        } catch (Exception e) {
            System.out.println("==============出现异常==============");
            e.printStackTrace();
        }

        System.out.println("==============下载任务结束，耗时：" + (System.currentTimeMillis() - startTime)  + "ms==========");

    }

    public static void downloadFile() throws IOException {
        HashMap<String, Object> jsonMap = parseJson();

        List<Map> list = (List) jsonMap.get("items");

        for (Map map : list) {
            List<Map> assets = (List<Map>) map.get("assets");
            for (Map dMap : assets) {
                String url = String.valueOf(dMap.get("downloadUrl"));
                String path = String.valueOf(dMap.get("path"));
                // 生成文件
                genFile(url, NEXUS_PATH + path);
            }
        }

    }

    /**
     * 读取解析JSON
     */
    private static HashMap<String, Object> parseJson() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        FileReader fileReader = new FileReader(JSON_FILE_PATH);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer sb = new StringBuffer();
        String s = "";
        while (StrUtil.isNotEmpty(s = bufferedReader.readLine())) {
            sb.append(s.trim());
        }
        return objectMapper.readValue(sb.toString(), HashMap.class);
    }

    /**
     * 生成文件
     */
    private static void genFile(final String urlStr, final String path) {
        System.out.println(path);

        URL url = null;
        try {
            url = new URL(urlStr);
            String tempFileName = path;

            // 先创建文件夹
            File t = new File(path);
            t.getParentFile().mkdirs();
            File temp = new File(tempFileName);
            FileUtils.copyURLToFile(url, temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

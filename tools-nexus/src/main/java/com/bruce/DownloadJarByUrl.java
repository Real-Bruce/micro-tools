package com.bruce;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Bruce
 * @create 2023/12/13
 * @description 根据nexus的请求地址获取仓库的全部JAR包
 */
public class DownloadJarByUrl {

    private static String NEXUS_PATH = "E:\\nexus_path\\test\\";
    private static final String JAR_URI = "/service/rest/v1/components";

    /**
     * 下载指定nexus仓库内的jar包，指定本地路径
     * @param url nexus地址
     * @param repository maven仓库
     * @param localPatch 本地路径
     */
    public void downloadFileWithPath(String url, String repository, String localPatch) {
        try {
            // 指定下载路径
            if (StrUtil.isNotBlank(localPatch)) {
                NEXUS_PATH = localPatch;
            }

            this.downloadFile(url, repository, null);
        } catch (Exception e) {
            System.out.println("出现异常！");
            e.printStackTrace();
        }
    }

    /**
     * 下载指定nexus仓库内的jar包
     * @param url nexus地址
     * @param repository 仓库
     * @param continuationToken 下一页token
     * @throws JsonProcessingException json转换异常
     */
    public void downloadFile(String url, String repository, String continuationToken) throws JsonProcessingException {

        String fullUrl ;
        if (StrUtil.isBlank(continuationToken)) {
            fullUrl = url + JAR_URI + "?repository=" + repository;
        } else {
            fullUrl = url + JAR_URI + "?continuationToken=" + continuationToken + "&repository=" + repository;
        }

        System.out.println("====1-开始请求nexus====");
        System.out.println("请求地址:" + fullUrl);
        String result = HttpRequest.get(fullUrl)
                .header(Header.ACCEPT, "application/json")
                .execute().body();

        Map<String, Object> jsonMap = new ObjectMapper().readValue(result, HashMap.class);

        System.out.println("====2-开始解析JSON====");
        List<JarDTO> jarDTOList = this.parseJson(jsonMap);

        System.out.println("====3-开始下载jar包====");
        jarDTOList.forEach(item -> this.genFile(item.downloadUrl, item.patch));

        // 回调继续解析
        String token = (String) jsonMap.get("continuationToken");
        if (StrUtil.isNotBlank(token)) {
            this.downloadFile(url, repository, token);
        }
    }

    /**
     * 下载jar包
     */
    private void genFile(final String urlStr, final String path){
        System.out.println(path);

        try {
            URL url = new URL(urlStr);
            // 创建文件夹
            File file = new File(path);
            file.getParentFile().mkdirs();
            File temp = new File(path);
            FileUtils.copyURLToFile(url, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析JSON获取下载路径
     */
    private List<JarDTO> parseJson(Map<String, Object> jsonMap) {

        List<Map> itemList = (List) jsonMap.get("items");
        List<JarDTO> fileUrlList = new ArrayList<>();

        for (Map map : itemList) {
            List<Map> assetsMap = (List<Map>) map.get("assets");

            for (Map dMap : assetsMap) {
                String downloadUrl = String.valueOf(dMap.get("downloadUrl"));
                String path = NEXUS_PATH + dMap.get("path");

                fileUrlList.add(new JarDTO(downloadUrl, path));
            }
        }

        return fileUrlList;
    }

}

package com.bruce;

/**
 * @author Bruce
 * @create 2023/12/14
 * @description
 */
public class Main {
    public static void main(String[] args) {
        String srcImagePath = "E:\\workspace\\workspace06\\micro-tools\\tools-graphics\\src\\main\\resources\\test.png";
        String targetPath = "E:\\workspace\\workspace06\\micro-tools\\tools-graphics\\src\\main\\resources\\test_water.png";
        String watermarkContent = "此图片xxx版权所有，如需商用请联系xxx";
        Graphics2DToWaterMark graphics2Dtowatermark = new Graphics2DToWaterMark();

        graphics2Dtowatermark.waterMark(srcImagePath,watermarkContent, 325.0, targetPath);
    }
}
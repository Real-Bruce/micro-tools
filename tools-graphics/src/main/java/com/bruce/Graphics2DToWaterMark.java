package com.bruce;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Bruce
 * @create 2023/12/14
 * @description
 */
public class Graphics2DToWaterMark {
    /**
     * 生成水印程序
     * @param srcImgPath 图片路径
     * @param watermarkContent 水印文字内容
     * @param angle 旋转角度
     * @param outPath 带水印图输出路径
     */
    public void waterMark(String srcImgPath,String watermarkContent, double angle, String outPath) {
        Graphics2D graphics2D = null;
        try {
            // 生成2D画布
            BufferedImage targetImg = ImageIO.read(new File(srcImgPath));
            int imgHeight = targetImg.getHeight();
            int imgWidth = targetImg.getWidth();
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_BGR);
            graphics2D = bufferedImage.createGraphics();
            graphics2D.drawImage(targetImg, 0, 0, imgWidth, imgHeight, null);

            // 设置水印样式
            graphics2D.getDeviceConfiguration().createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
            Font font = this.loadFontResource(imgWidth, imgHeight, angle, watermarkContent, graphics2D, bufferedImage);
            graphics2D.setColor(Color.lightGray);
            graphics2D.setFont(font);

            // 设置水印位置和角度
            int[] waterImg = waterImg(imgWidth, imgHeight, angle, font, watermarkContent, graphics2D);
            int x = waterImg[0];
            int y = waterImg[1];
            graphics2D.rotate(Math.toRadians(angle), x, y);

            // 绘制水印到目标图
            graphics2D.drawString(watermarkContent, x, y);
            FileOutputStream outImgStream = new FileOutputStream(outPath);
            ImageIO.write(bufferedImage, "jpg", outImgStream);
            System.out.println("图片水印添加完成，文件输出路径:" + outPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert graphics2D != null;
            graphics2D.dispose();
        }
    }

    /**
     * 根据图片分辨率计算水印的文字大小
     * @param imgWidth 图片宽度
     * @param imgHeight 图片高度
     * @return 字体大小
     */
    @Deprecated
    private Font getContentFont(int imgWidth, int imgHeight, double angle, String waterContent, Graphics2D graphics2D) {
        int contentWidth = 0;
        int contentHeight = 0;
        Font font = null;

        for (int i = 60; i > 0; i--) {
            font = new Font("Amatic SC", Font.BOLD, i);
            int contentLength = graphics2D.getFontMetrics(font).charsWidth(waterContent.toCharArray(), 0, waterContent.length());
            contentWidth = (int) (contentLength *  (- Math.cos(angle))) + imgWidth / 2;
            contentHeight = (int) (contentLength *  (- Math.sin(angle))) + imgHeight / 2;
            if (contentWidth < imgWidth && contentHeight < imgHeight) {
                break;
            }
        }
        return font;
    }

    /**
     * 计算水印生成位置坐标
     * @param imgWidth 图片宽度
     * @param imgHeight 图片高度
     * @param angle 旋转角度
     * @param font 文字大小
     * @param waterContent 水印内容
     * @return 水印生成位置坐标
     */
    private int[] waterImg(int imgWidth, int imgHeight, double angle, Font font, String waterContent, Graphics2D graphics2D) {
        int[] waterImg = new int [2];
        int contentLength = graphics2D.getFontMetrics(font).charsWidth(waterContent.toCharArray(), 0, waterContent.length());
        int contentWidth = (int) (contentLength *  (- Math.cos(angle)));
        int contentHeight = (int) (contentLength *  (- Math.sin(angle)));
        waterImg[0] = (imgWidth - 2 * contentWidth) / 4;
        waterImg[1] = imgHeight / 4 + contentHeight;
        return waterImg;
    }

    /**
     * 加载字体文件，根据图片分辨率计算水印的文字大小
     * @return Font
     */
    private Font loadFontResource(int imgWidth, int imgHeight, double angle, String waterContent, Graphics2D graphics2D, BufferedImage imgBuffer) {
        Font font = null;
        try {
            // 加载外部字体
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("font/SmileySans-Oblique.ttf");
            assert resourceAsStream != null;
            Font sourceFont = Font.createFont(Font.TRUETYPE_FONT, resourceAsStream);
            // 注册外部字体
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(sourceFont);
            graphics2D = graphicsEnvironment.createGraphics(imgBuffer);

            int contentWidth = 0;
            int contentHeight = 0;
            for (int i = 60; i > 1; i--) {
                font = sourceFont.deriveFont(Font.PLAIN, i);
                int contentLength = graphics2D.getFontMetrics(font).charsWidth(waterContent.toCharArray(), 0, waterContent.length());
                contentWidth = (int) (contentLength *  (- Math.cos(angle))) + imgWidth / 2;
                contentHeight = (int) (contentLength *  (- Math.sin(angle))) + imgHeight / 2;
                if (contentWidth < imgWidth && contentHeight < imgHeight) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return font;
    }
}

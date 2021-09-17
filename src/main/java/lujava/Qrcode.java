package lujava;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Hashtable;

public class Qrcode {
    /**
     * 生成二维码 直接将二维码图片写到指定文件目录
     *
     * @param content   二维码内容
     * @param width     二维码宽度
     * @param height    二维码高度，通常建议二维码宽度和高度相同
     * @param fileDir   二维码存放路径
     * @param picName   二维码名字
     * @param picFormat 二维码图片格式，jpg/png
     */
    public static void createQRCode(String content, int width, int height, String fileDir, String picName, String picFormat) {

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            // 构造二维字节矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 构造文件目录，若目录不存在，则创建目录
            if (!new File(fileDir).exists()) {
                new File(fileDir).mkdirs();
            }
            Path file = new File(fileDir + File.separator + picName + "." + picFormat).toPath();

            // 将二位字节矩阵按照指定图片格式，写入指定文件目录，生成二维码图片
            MatrixToImageWriter.writeToPath(bitMatrix, picFormat, file);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码 生成二维码图片字节流
     *
     * @param content   二维码内容
     * @param width     二维码内容
     * @param height    二维码宽度
     * @param picFormat 二维码图片格式
     */
    public static byte[] createQRCodeByte(String content, int width, int height, String picFormat) {
        byte[] codeBytes = null;
        try {
            // 构造二维字节矩阵，将二位字节矩阵渲染为二维缓存图片
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int onColor = 0xFF000000;
            int offColor = 0xFFFFFFFF;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? onColor : offColor);
                }
            }
            // 定义输出流，将二维缓存图片写到指定输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, picFormat, out);

            // 将输出流转换为字节数组
            codeBytes = out.toByteArray();

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return codeBytes;
    }

    /**
     * 解析二维码内容
     *
     * @param filepath 二维码路径
     */
    public static String readQRCode(String filepath) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        File file = new File(filepath);

        // 图片缓冲
        BufferedImage image = null;

        // 二进制比特图
        BinaryBitmap binaryBitmap = null;

        // 二维码结果
        Result result = null;

        try {
            image = ImageIO.read(file);
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            result = multiFormatReader.decode(binaryBitmap);
        } catch (IOException | NotFoundException e1) {
            e1.printStackTrace();
        }

        return result.getText();
    }
}

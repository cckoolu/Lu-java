package lujava;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;

public class Qrcode {
    /**
     * 解析二维码内容
     *
     * @param filepath 二维码路径
     */
    public static String readQRCode(String filepath) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        File file = new File(filepath);

        // 图片缓冲
        BufferedImage image;

        // 二进制比特图
        BinaryBitmap binaryBitmap;

        // 二维码结果
        Result result = null;

        try {
            image = ImageIO.read(file);
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            result = multiFormatReader.decode(binaryBitmap);
        } catch (IOException | NotFoundException e1) {
            System.out.println("readQRCode 当前路径没有该文件");
        }

        if (result == null) {
            return "二维码内容为空";
        }
        return result.getText();
    }

    /**
     * 生成二维码
     * 直接将二维码图片写到指定文件目录
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
     * 生成二维码
     * 生成二维码图片字节流
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
     * 生成二维码
     * 生成二维码图片加二维码备注
     *
     * @param content 二维码内容
     * @param remark  二维码备注内容
     * @param path    二维码保存路径
     * @param picName 二维码图片名字
     */
    public static void createQRCodeRemark(String content, String remark, String path, String picName) throws IOException, WriterException {

        // 创建输出画布，由于有备注信息，这里个二维码宽高不等
        // 通常情况下，建议设置宽高相等
        BufferedImage logoRemarkImage = new BufferedImage(300, 325, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 325);
        graphics.dispose();

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);

        // 定义二维码位图矩阵
        BitMatrix m = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);
        BufferedImage imageNew = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                imageNew.setRGB(x, y, m.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = imageNew.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 设置二维码备注信息
        Graphics graphicsText = logoRemarkImage.createGraphics();
        graphicsText.setColor(Color.black);
        graphicsText.setFont(new Font(null, Font.PLAIN, 24));
        graphicsText.drawString(remark, 100, 320);
        graphicsText.dispose();

        // 定义输出流，将二维缓存图片写到指定输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);

        // 二维码图片最终输出
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        File pathFile = new File(path + File.separator + picName + ".jpg");
        byte[] fileIo = out.toByteArray();
        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     * 生成二维码图片加二维码备注字节流
     *
     * @param content 二维码内容
     * @param remark  二维码备注内容
     */
    public static byte[] createQRCodeRemarkByte(String content, String remark) throws IOException, WriterException {

        // 创建输出画布，由于有备注信息，这里个二维码宽高不等
        // 通常情况下，建议设置宽高相等
        BufferedImage logoRemarkImage = new BufferedImage(300, 325, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 325);
        graphics.dispose();

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);

        // 定义二维码位图矩阵
        BitMatrix m = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);
        BufferedImage imageNew = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                imageNew.setRGB(x, y, m.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = imageNew.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 设置二维码备注信息
        Graphics graphicsText = logoRemarkImage.createGraphics();
        graphicsText.setColor(Color.black);
        graphicsText.setFont(new Font(null, Font.PLAIN, 24));
        graphicsText.drawString(remark, 100, 320);
        graphicsText.dispose();

        // 定义输出流，将二维缓存图片写到指定输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);

        return out.toByteArray();
    }

    /**
     * 生成二维码
     * 二维码中内嵌图片logo
     *
     * @param content 二维码携带内容信息
     * @param path    二维码保存路径
     * @param picName 二维码图片名字
     */
    public static void generateQRCodeLogo(String content, String logoPath, String path, String picName) throws WriterException, IOException {

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 因为有备注信息
        // 这里不要设置二维码边距，否则二维码和备注信息之间会有明显间隔
        hints.put(EncodeHintType.MARGIN, 1);

        // 定义二维码位图矩阵
        BitMatrix bt = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);

        // 定义二维码图片，并将位图矩阵点渲染到二维码图片上
        BufferedImage btImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                btImage.setRGB(x, y, bt.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 读取logo图片，并判断logo图片的宽高必须相等
        Image logoImg = ImageIO.read(new File(logoPath));
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        if (logoWidth != logoHeight) {
            return;
        }

        // 对logo图片实施压缩
        // 若logo图片宽高大于二维码宽高，必须对logo图片进行压缩
        // 否则，过大的照片会撑爆二维码，生成的二维码只能看到部分的logo图片信息
        if (logoWidth > 300 && logoHeight > 300) {
            logoWidth = 300;
            logoHeight = 300;
        }

        // 创建缩微版本的logo图片
        Image image = logoImg.getScaledInstance(logoWidth, logoHeight,
                Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(logoWidth, logoHeight,
                BufferedImage.TYPE_INT_RGB);

        // 绘制缩微版logo图片，并将新的logo图片赋给原logo图片变量
        Graphics logoGraphics = tag.getGraphics();
        logoGraphics.drawImage(image, 0, 0, null);
        logoGraphics.dispose();
        logoImg = image;

        // 在原二维码中计算位置，插入新的logo图片
        Graphics2D graph = btImage.createGraphics();
        int x = (300 - logoWidth) / 2;
        int y = (300 - logoHeight) / 2;
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        // 建立画布并设置背景色，一般设置为白色
        // 将二维码信息和备注信息均渲染在该画布上，作为最终的输出
        BufferedImage logoRemarkImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 300);
        graphics.dispose();

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = btImage.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 写备注信息
//        Graphics gText = logoRemarkImage.createGraphics();
//        gText.setColor(Color.black);
//        gText.setFont(new Font(null, Font.PLAIN, 18));
//        gText.drawString("二维码备注信息", 80, 320);
//        gText.dispose();

        // 定义输出流，将二维缓存图片写到指定输出流
        // 将最终生成的二维码写到本地
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);

//        String path = "./" + File.separator + "target" + File.separator ;
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        File pathFile = new File(path + File.separator + picName + ".jpg");
        byte[] fileIo = out.toByteArray();
        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     * 生成二维码图片镶嵌logo字节流
     *
     * @param content 二维码内容
     */
    public static byte[] generateQRCodeLogoByte(String content, String logoPath) throws WriterException, IOException {

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 因为有备注信息
        // 这里不要设置二维码边距，否则二维码和备注信息之间会有明显间隔
        hints.put(EncodeHintType.MARGIN, 1);

        // 定义二维码位图矩阵
        BitMatrix bt = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);

        // 定义二维码图片，并将位图矩阵点渲染到二维码图片上
        BufferedImage btImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                btImage.setRGB(x, y, bt.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 读取logo图片，并判断logo图片的宽高必须相等

        Image logoImg = ImageIO.read(new File(logoPath));
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        if (logoWidth != logoHeight) {
            return null;
        }

        // 对logo图片实施压缩
        // 若logo图片宽高大于二维码宽高，必须对logo图片进行压缩
        // 否则，过大的照片会撑爆二维码，生成的二维码只能看到部分的logo图片信息
        if (logoWidth > 300 && logoHeight > 300) {
            logoWidth = 300;
            logoHeight = 300;
        }

        // 创建缩微版本的logo图片
        Image image = logoImg.getScaledInstance(logoWidth, logoHeight,
                Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(logoWidth, logoHeight,
                BufferedImage.TYPE_INT_RGB);

        // 绘制缩微版logo图片，并将新的logo图片赋给原logo图片变量
        Graphics logoGraphics = tag.getGraphics();
        logoGraphics.drawImage(image, 0, 0, null);
        logoGraphics.dispose();
        logoImg = image;

        // 在原二维码中计算位置，插入新的logo图片
        Graphics2D graph = btImage.createGraphics();
        int x = (300 - logoWidth) / 2;
        int y = (300 - logoHeight) / 2;
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        // 建立画布并设置背景色，一般设置为白色
        // 将二维码信息和备注信息均渲染在该画布上，作为最终的输出
        BufferedImage logoRemarkImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 300);
        graphics.dispose();

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = btImage.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 定义输出流，将二维缓存图片写到指定输出流
        // 将最终生成的二维码写到本地
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);

        return out.toByteArray();
    }

    /**
     * 生成二维码
     * 二维码中内嵌图片logo
     *
     * @param content  二维码携带内容信息
     * @param remark   二维码备注
     * @param logoPath logo路径
     * @param path     二维码保存路径
     * @param picName  二维码图片名字
     */
    public static void generateQRCodeLogoRemark(String content, String remark, String logoPath, String path, String picName) throws WriterException, IOException {

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 因为有备注信息
        // 这里不要设置二维码边距，否则二维码和备注信息之间会有明显间隔
        hints.put(EncodeHintType.MARGIN, 1);


        // 定义二维码位图矩阵
        BitMatrix bt = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);

        // 定义二维码图片，并将位图矩阵点渲染到二维码图片上
        BufferedImage btImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                btImage.setRGB(x, y, bt.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 读取logo图片，并判断logo图片的宽高必须相等
        Image logoImg = ImageIO.read(new File(logoPath));
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        if (logoWidth != logoHeight) {
            return;
        }

        // 对logo图片实施压缩
        // 若logo图片宽高大于二维码宽高，必须对logo图片进行压缩
        // 否则，过大的照片会撑爆二维码，生成的二维码只能看到部分的logo图片信息
        if (logoWidth > 300 && logoHeight > 300) {
            logoWidth = 300;
            logoHeight = 300;
        }

        // 创建缩微版本的logo图片
        Image image = logoImg.getScaledInstance(logoWidth, logoHeight,
                Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(logoWidth, logoHeight,
                BufferedImage.TYPE_INT_RGB);

        // 绘制缩微版logo图片，并将新的logo图片赋给原logo图片变量
        Graphics logoGraphics = tag.getGraphics();
        logoGraphics.drawImage(image, 0, 0, null);
        logoGraphics.dispose();
        logoImg = image;

        // 在原二维码中计算位置，插入新的logo图片
        Graphics2D graph = btImage.createGraphics();
        int x = (300 - logoWidth) / 2;
        int y = (300 - logoHeight) / 2;
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        // 建立画布并设置背景色，一般设置为白色
        // 将二维码信息和备注信息均渲染在该画布上，作为最终的输出
        BufferedImage logoRemarkImage = new BufferedImage(300, 325, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 325);
        graphics.dispose();

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = btImage.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 写备注信息
        Graphics gText = logoRemarkImage.createGraphics();
        gText.setColor(Color.black);
        gText.setFont(new Font(null, Font.PLAIN, 24));
        gText.drawString(remark, 80, 320);
        gText.dispose();


        // 定义输出流，将二维缓存图片写到指定输出流
        // 将最终生成的二维码写到本地
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);

//        String path = "./" + File.separator + "target" + File.separator ;
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        File pathFile = new File(path + File.separator + picName + ".jpg");
        byte[] fileIo = out.toByteArray();
        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     * 生成二维码图片镶嵌logo字节流
     *
     * @param content  二维码携带内容信息
     * @param remark   二维码备注
     * @param logoPath logo路径
     */
    public static byte[] generateQRCodeLogoRemarkByte(String content, String remark, String logoPath) throws WriterException, IOException {

        // 设置二维码纠错信息
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 因为有备注信息
        // 这里不要设置二维码边距，否则二维码和备注信息之间会有明显间隔
        hints.put(EncodeHintType.MARGIN, 1);


        // 定义二维码位图矩阵
        BitMatrix bt = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300,
                hints);

        // 定义二维码图片，并将位图矩阵点渲染到二维码图片上
        BufferedImage btImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                btImage.setRGB(x, y, bt.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 读取logo图片，并判断logo图片的宽高必须相等
        Image logoImg = ImageIO.read(new File(logoPath));
        int logoWidth = logoImg.getWidth(null);
        int logoHeight = logoImg.getHeight(null);
        if (logoWidth != logoHeight) {
            return new byte[0];
        }

        // 对logo图片实施压缩
        // 若logo图片宽高大于二维码宽高，必须对logo图片进行压缩
        // 否则，过大的照片会撑爆二维码，生成的二维码只能看到部分的logo图片信息
        if (logoWidth > 300 && logoHeight > 300) {
            logoWidth = 300;
            logoHeight = 300;
        }

        // 创建缩微版本的logo图片
        Image image = logoImg.getScaledInstance(logoWidth, logoHeight,
                Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(logoWidth, logoHeight,
                BufferedImage.TYPE_INT_RGB);

        // 绘制缩微版logo图片，并将新的logo图片赋给原logo图片变量
        Graphics logoGraphics = tag.getGraphics();
        logoGraphics.drawImage(image, 0, 0, null);
        logoGraphics.dispose();
        logoImg = image;

        // 在原二维码中计算位置，插入新的logo图片
        Graphics2D graph = btImage.createGraphics();
        int x = (300 - logoWidth) / 2;
        int y = (300 - logoHeight) / 2;
        graph.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        // 建立画布并设置背景色，一般设置为白色
        // 将二维码信息和备注信息均渲染在该画布上，作为最终的输出
        BufferedImage logoRemarkImage = new BufferedImage(300, 325, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = logoRemarkImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 325);
        graphics.dispose();

        // 将生成的二维码转化为像素数组，并将数组写到画布logoRemarkImage上
        int[] imageNewArray = new int[300 * 300];
        imageNewArray = btImage.getRGB(0, 0, 300, 300, imageNewArray, 0, 300);
        logoRemarkImage.setRGB(0, 0, 300, 300, imageNewArray, 0, 300);

        // 写备注信息
        Graphics gText = logoRemarkImage.createGraphics();
        gText.setColor(Color.black);
        gText.setFont(new Font(null, Font.PLAIN, 24));
        gText.drawString(remark, 80, 320);
        gText.dispose();


        // 定义输出流，将二维缓存图片写到指定输出流
        // 将最终生成的二维码写到本地
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(logoRemarkImage, "jpg", out);
        return out.toByteArray();
    }
}

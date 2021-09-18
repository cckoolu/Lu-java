package lujava;

import com.google.zxing.WriterException;
import org.junit.Test;

import java.io.*;

public class QrcodeTest {
    @Test
    public void createQRCode() {
        // 生成纯二维码
        final String content = "https://github.com/cckoolu/Lu-java";
        final int width = 300;
        final int height = 300;
        final String fileDir = "./target/";
        final String picName = "testQRCode";
        final String picFormat = "png";

        Qrcode.createQRCode(content, width, height, fileDir, picName, picFormat);
        System.out.println("createQRCode    二维码生成成功，请到target目录下查看");
    }

    @Test
    public void createQRCodeByte() {
        final String content = "https://github.com/cckoolu/Lu-java";
        final int width = 300;
        final int height = 300;
        final String picFormat = "jpg";

        final String fileDir = "./target/";

        File pathDir = new File(fileDir);

        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        File pathFile = new File(fileDir + File.separator + "testQrcodeByte.jpg");
        byte[] fileIo = Qrcode.createQRCodeByte(content, width, height, picFormat);
        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("createQRCodeByte    二维码生成成功，请到target目录下查看");
    }

    @Test
    public void createQRCodeRemark() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String remark = "二维码备注";
        final String path = "./target";
        final String picName = "testQRCodeRemark";

        Qrcode.createQRCodeRemark(content, remark, path, picName);
        System.out.println("createQRCodeRemark  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void createQRCodeRemarkByte() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String remark = "二维码备注";
        final String path = "./target";
        final String picName = "testQRCodeRemarkByte";

        final String pathFile = path + File.separator + picName + ".jpg";

        byte[] fileIo = Qrcode.createQRCodeRemarkByte(content, remark);
        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("createQRCodeRemarkByte  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void generateQRCodeLogo() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String logoPath = "src/test/logoImage/logo.jpg";
        final String path = "./target";
        final String picName = "testGenerateQRCodeLogo";

       Qrcode.generateQRCodeLogo(content, logoPath, path ,picName);

        System.out.println("generateQRCodeLogo  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void generateQRCodeLogoByte() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String logoPath = "src/test/logoImage/logo.jpg";
        final String path = "./target";
        final String picName = "testGenerateQRCodeLogoByte";

        final String pathFile = path + File.separator + picName + ".jpg";

        byte[] fileIo = Qrcode.generateQRCodeLogoByte(content, logoPath);

        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("generateQRCodeLogoByte  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void generateQRCodeLogoRemark() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String remark = "二维码信息";
        final String logoPath = "src/test/logoImage/logo.jpg";
        final String path = "./target";
        final String picName = "testGenerateQRCodeLogoRemark";

        Qrcode.generateQRCodeLogoRemark(content,remark, logoPath, path ,picName);

        System.out.println("generateQRCodeLogoRemark  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void generateQRCodeLogoRemarkByte() throws IOException, WriterException {
        final String content = "https://github.com/cckoolu/Lu-java";
        final String remark = "二维码信息";
        final String logoPath = "src/test/logoImage/logo.jpg";

        final String path = "./target";
        final String picName = "testGenerateQRCodeLogoRemarkByte";

        final String pathFile = path + File.separator + picName + ".jpg";

        byte[] fileIo = Qrcode.generateQRCodeLogoRemarkByte(content, remark,logoPath);

        try {
            OutputStream os = new FileOutputStream(pathFile);
            os.write(fileIo);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("generateQRCodeLogoRemarkByte  二维码生成成功，请到target目录下查看");
    }

    @Test
    public void readQRCode() {

            String filepath = "." + File.separator + "target" + File.separator + "testQRCodeRemark.jpg";

            String content = Qrcode.readQRCode(filepath);
            System.out.println(content);


    }

}

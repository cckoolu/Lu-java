package lujava;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QrcodeTest {
    @Test
    public void createQRCode() {
        // 生成纯二维码
        final String content = "https://github.com/cckoolu/Lu-java";
        final int width = 300;
        final int height = 300;
        final String fileDir = "./target/";
        final String picName = "testQRCodePic";
        final String picFormat = "png";

        Qrcode.createQRCode(content, width, height, fileDir, picName, picFormat);
        System.out.println("二维码生成成功，请到target目录下查看");
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
        System.out.println("二维码生成成功，请到target目录下查看");
    }

    @Test
    public void readQRCode() {
        String filepath = "." + File.separator + "target" + File.separator + "testQrcodeByte.jpg";

        String content = Qrcode.readQRCode(filepath);
        System.out.println(content);
    }
}

package lujava;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtilTest {
    @Test
    public void writeToLocal() throws IOException {
        String filePath = "./target/text.txt";
        String content = "hello";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        FileUtil.writeToLocal(filePath,inputStream);
        System.out.println("文件创建成功，请到target目录下查看");
    }
}
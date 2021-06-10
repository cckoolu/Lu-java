package lujava;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtilTest {
    @Test
    public void writeToLocal() throws IOException {
        String filePath = "./target/text.txt";  //文件在target目录下
        String content = "hello";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        FileUtil.writeToLocal(filePath,inputStream);
    }
}
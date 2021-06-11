package lujava;

import org.junit.Test;
import java.text.ParseException;


public class TimeStampUtilTest {
    @Test
    public void timeStamp() throws ParseException {
        long nowTimeStamp = TimeStampUtil.getTimeStamp();
        String defaultTimeStampFormat = TimeStampUtil.getTimeStampFormat();
        String timeStampFormat = TimeStampUtil.getTimeStampFormat("yyyy年MM月dd日 HH:mm:ss");
        long defaultTimeStampSpecify = TimeStampUtil.getTimeStampSpecify();
        long timeStampSpecify = TimeStampUtil.getTimeStampSpecify("1999/09/90 18:26:30:233");

        System.out.println(nowTimeStamp);
        System.out.println(defaultTimeStampFormat);
        System.out.println(timeStampFormat);
        System.out.println(defaultTimeStampSpecify);
        System.out.println(timeStampSpecify);
    }
}

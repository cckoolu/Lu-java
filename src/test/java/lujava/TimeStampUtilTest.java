package lujava;

import org.junit.Test;
import java.text.ParseException;


public class TimeStampUtilTest {
    @Test
    public void timeStamp() throws ParseException {
        System.out.println(TimeStampUtil.getTimeStamp());
        System.out.println(TimeStampUtil.getTimeStampFormat());
        System.out.println(TimeStampUtil.getTimeStampFormat("yyyy年MM月dd日 HH:mm:ss"));
        System.out.println(TimeStampUtil.getTimeStampSpecify());
        System.out.println(TimeStampUtil.getTimeStampSpecify("1999/09/90 18:26:30:233"));
    }
}

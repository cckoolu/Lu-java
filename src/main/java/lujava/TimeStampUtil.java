package lujava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings(value = "unused")
public class TimeStampUtil {
    // 精确到毫秒
    // 获取当前时间戳
    public static long getTimeStamp(){
        return new Date().getTime();
    }

    // 精确到毫秒
    // 获取指定格式的时间戳
    public static String getTimeStampFormat(String... defaultFormat){
        String format = "yyyy-MM-dd HH:mm:ss";

        if (defaultFormat.length > 0){
            format = defaultFormat[0];
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        df.getCalendar();
        return df.format(new Date());
    }

    // 获取指定时间的时间戳
    public static Long getTimeStampSpecify(String... defaultDate) throws ParseException {
        String date = "2021/06/10 18:26:30:233";

        if (defaultDate.length > 0){
            date = defaultDate[0];
        }

        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").parse(date).getTime();
    }
}

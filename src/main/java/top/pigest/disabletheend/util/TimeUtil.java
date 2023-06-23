package top.pigest.disabletheend.util;

public class TimeUtil {
    public static final int DAY = 60 * 60 * 24;
    public static final int HOUR = 60 * 60;
    public static final int MINUTE = 60;
    public static String formatTime(int seconds) {
        int day = seconds / DAY;
        int hour = seconds % DAY / HOUR;
        int minute = seconds % HOUR / MINUTE;
        int second = seconds % MINUTE;
        String s = "";
        if(day > 0) {
            s += day + "天";
        }
        if(hour > 0) {
            s += hour + "小时";
        }
        if(minute > 0) {
            s += minute + "分钟";
        }
        if(second > 0) {
            s += second + "秒";
        }
        return s;
    }
}

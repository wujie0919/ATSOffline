package df.yyzc.com.yydf.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author zhangyu
 */
public class TimeDateUtil {

    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 将时间秒 转换成 X天X小时X分
     *
     * @param second
     * @return
     */
    public static String DHMTransform(int second) {

        if (second < 0) {
            return null;
        }
        second = second / 1000;

        int d, h, m, s, r;
        d = second / (24 * 3600);
        r = second % (24 * 3600);
        h = r / 3600;
        r = second % 3600;
        m = r / 60;
        s = r % 60;

        StringBuffer buffer = new StringBuffer();
        if (d > 0) {
            buffer.append(d).append("天 ");
        }
        if (h > 0) {
            buffer.append(h).append("小时 ");
        }
        // if (m > 0) {
        buffer.append(m).append("分 ");
        // }

        return buffer.toString();
    }

    /**
     * 将时间微秒 转换成小时
     *
     * @param second
     * @return
     */
    public static long HTransform(long second) {
        if (second < 0) {
            return 0;
        }
        second = second / 1000;
        long h;
        h = second / 3600;
        return h;
    }

    public static String MDHMTransform(long second) {

        if (second == 0) {
            return "";
        }

        String date = new SimpleDateFormat("MM月dd日 HH:mm")
                .format(new Date(second));

        return date;
    }

    /*
         * 毫秒转化
         */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;
        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strHour + "小时" + strMinute + " 分 ";
    }


    /**
     * @param ms     时间戳
     * @param format 格式
     * @return
     */
    public static String formatTime(long ms, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(ms));
    }

    /**
     * 转化 几天前几月前几年前
     */
    public static class RelativeDateFormat {

        private static final long ONE_MINUTE = 60000L;
        private static final long ONE_HOUR = 3600000L;
        private static final long ONE_DAY = 86400000L;
        private static final long ONE_WEEK = 604800000L;

        private static final String ONE_SECOND_AGO = "秒前";
        private static final String ONE_MINUTE_AGO = "分钟前";
        private static final String ONE_HOUR_AGO = "小时前";
        private static final String ONE_DAY_AGO = "天前";
        private static final String ONE_MONTH_AGO = "月前";
        private static final String ONE_YEAR_AGO = "年前";

        public static String format(long date) {
            long delta = new Date().getTime() - date;
            if (delta < 1L * ONE_MINUTE) {
                long seconds = toSeconds(delta);
                return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
            }
            if (delta < 45L * ONE_MINUTE) {
                long minutes = toMinutes(delta);
                return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
            }
            if (delta < 24L * ONE_HOUR) {
                long hours = toHours(delta);
                return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
            }
            if (delta < 48L * ONE_HOUR) {
                return "昨天";
            }
            if (delta < 30L * ONE_DAY) {
                long days = toDays(delta);
                return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
            }
            if (delta < 12L * 4L * ONE_WEEK) {
                long months = toMonths(delta);
                return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
            } else {
                long years = toYears(delta);
                return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
            }
        }

        private static long toSeconds(long date) {
            return date / 1000L;
        }

        private static long toMinutes(long date) {
            return toSeconds(date) / 60L;
        }

        private static long toHours(long date) {
            return toMinutes(date) / 60L;
        }

        private static long toDays(long date) {
            return toHours(date) / 24L;
        }

        private static long toMonths(long date) {
            return toDays(date) / 30L;
        }

        private static long toYears(long date) {
            return toMonths(date) / 365L;
        }

    }
}

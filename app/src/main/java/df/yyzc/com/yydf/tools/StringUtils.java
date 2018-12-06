package df.yyzc.com.yydf.tools;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class StringUtils {

    public static String formatPhone(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (isPhoneNumber(str)) {

            StringBuffer buffer = new StringBuffer();
            buffer.append(str.substring(0, 3)).append("****")
                    .append(str.substring(7, 11));
            return buffer.toString();
        }
        return str;
    }

    // 判断，返回布尔值
    public static boolean isPhoneNumber(String input) {
        String regex = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0,2,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(input).find();
    }

}

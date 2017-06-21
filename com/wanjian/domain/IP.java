package com.wanjian.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanjian on 2017/6/16.
 */

public class IP {
    static Pattern sPattern;

    static {
        String rexp = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        sPattern = Pattern.compile(rexp);
    }

    public static String getIp() {
        String result = HttpRequest.request("http://1212.ip138.com/ic.asp");
//        Logger.log(result);
        Matcher matcher = sPattern.matcher(result);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
    //test
    public static void main(String[] args) {
        Logger.log(isboolIp("cvrv23322.224.77.194csvfwfwe"));
    }
    //test
    public static Object isboolIp(String ipAddress) {
        String rexp = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(ipAddress);
        Logger.log(matcher);
        Logger.log(matcher.find());
        Logger.log(matcher.group());
        Logger.log(matcher.group(0));
        Logger.log(matcher.group(1));
        Logger.log(matcher.group(2));
        return matcher.groupCount();
    }
}

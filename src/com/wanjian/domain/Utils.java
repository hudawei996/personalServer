package com.wanjian.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wanjian on 2017/6/16.
 */

public class Utils {

    public static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String date = dateFormat.format(new Date());
        return date;
    }

    public static String formatJson(String json) {
	 Logger.log(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        return gson.toJson(je);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replace("%7E", "~").replace("+", "%20").replace("*", "%2A");
        } catch (UnsupportedEncodingException e) {
            Logger.log(e);
        }
        return null;
    }

    public static String hmacSHA1(String secret, String s) {
        Logger.log(s);
        // 以下是一段计算签名的示例代码
        final String ALGORITHM = "HmacSHA1";
        final String ENCODING = "UTF-8";
        final String key = secret + "&";

        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(
                    key.getBytes(ENCODING), ALGORITHM));
            byte[] signData = mac.doFinal(
                    s.getBytes(ENCODING));

            return new String(Base64.encodeBase64(signData));
        } catch (Exception e) {
           Logger.log(e);
        }

        return null;
    }


    public static void printMap(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Logger.log(entry.getKey() + " : " + entry.getValue());
        }
    }
}

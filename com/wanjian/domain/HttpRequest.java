package com.wanjian.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wanjian on 2017/6/16.
 */

public class HttpRequest {

    public static String request(String url) {
        try {
            HttpURLConnection connection = ((HttpURLConnection) new URL(url).openConnection());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            int respCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == respCode) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String readLine = null;
                StringBuffer response = new StringBuffer();
                while (null != (readLine = bufferedReader.readLine())) {
                    response.append(readLine);
                }
                bufferedReader.close();
                return response.toString();
            } else {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String readLine = null;
                StringBuffer response = new StringBuffer();
                while (null != (readLine = bufferedReader.readLine())) {
                    response.append(readLine);
                }
                Logger.log(Utils.formatJson(response.toString()));
                bufferedReader.close();
            }
        } catch (IOException e) {
            Logger.log(e);
        }
        return null;
    }
}

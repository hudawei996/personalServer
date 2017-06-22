package com.wanjian.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanjian on 2017/6/16.
 */

public class Logger {

    private static BufferedWriter sWriter;

    static {
        String name = "AutoBandDomain-" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + "-" + (int) (Math.random() * Integer.MAX_VALUE) + ".log";
        File file = new File(name);
        try {
            sWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public static void log(Object log) {
        String msg = new Date().toLocaleString() + " : " + log;

        System.out.println(msg);
        if (sWriter == null) {
            return;
        }

        try {
            sWriter.write(msg);
            sWriter.newLine();
            sWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(Exception e) {
        e.printStackTrace();
        if (sWriter == null) {
            return;
        }
        for (StackTraceElement element : e.getStackTrace()) {
            try {
                sWriter.write('\t');
                sWriter.write(element.toString());
                sWriter.newLine();
                sWriter.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        log(">>>>>>>>>>>");
        log(">>>>>>>>>>>");
        log(">>>>>>>>>>>");
        log(">>>>>>>>>>>");
    }
}

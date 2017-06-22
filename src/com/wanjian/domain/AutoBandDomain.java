package com.wanjian.domain;

/**
 * Created by wanjian on 2017/6/16.
 * <p>
 * 参考  https://help.aliyun.com/document_detail/29778.html?spm=5176.doc29776.6.620.r6SQl3
 * <p>
 *
 */

public class AutoBandDomain {
    /**
     * @param domain    域名，如 烫tang.xyz
     * @param subDomain 子域名前缀，例如要想解析  mail.烫tang.xyz ，则domain为 烫tang.xyz ，subDomain为  mail ，若不需要子域名直接传null即可
     */
    private static final String DOMAIN = "烫烫.xyz";
    private static final String SUB_DOMAIN = "auto";
    private static final int TIME = 60 * 60 * 1000;//每小时获取一次IP
    private static final int RETRY = 10 * 1000;//请求出错等待10秒后重试


    public static void main(String[] args) {
        String originIp = null;
        while (true) {
            Logger.log(">>>>>>获取IP....");
            String ip = null;
            try {
                ip = IP.getIp();
            } catch (Exception e) {
                Logger.log(e);
            }

            Logger.log("IP : " + ip);
            if (ip == null || "".equals(ip)) {
                pause(RETRY);
                continue;
            } else if (ip.equals(originIp)) {
                pause(TIME);
                continue;
            }
            originIp = ip;
            Logger.log(">>>>>>>绑定域名....");
            try {
                BandDomain.addDomainRecord(DOMAIN, SUB_DOMAIN, ip);
            } catch (InitException e) {
                throw e;
            } catch (Exception e) {
                Logger.log(e);
                pause(RETRY);
                continue;
            }
            pause(TIME);

        }
    }

    private static void pause(int time) {
        Logger.log("等待 " + (time / 1000 / 60f) + " 分钟");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Logger.log(e);
        }
    }
}

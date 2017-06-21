package com.wanjian.domain;


import com.google.gson.Gson;
import com.wanjian.domain.model.DomainRecorders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by wanjian on 2017/6/15.
 */

public class BandDomain {
    private static final String ID = "LTAIyiAFddm71X1N";//你的Access Key ID
    private static final String SECRET = "PZyGsbIklgqD47lf6MBqPRwNykqwKW";//你的Access Key Secret
    private static final String SERVER = "http://alidns.aliyuncs.com";//阿里云接口


    /**
     * @param domain    域名，如 烫tang.xyz
     * @param subDomain 子域名前缀，例如要想解析  mail.烫tang.xyz ，则domain为 烫tang.xyz ，subDomain为  mail ，若不需要子域名直接传null即可
     * @param ip        例如123.111.230.1
     */
    public static void addDomainRecord(String domain, String subDomain, String ip) {
        if (ID == null || "".equals(ID) || SECRET == null || "".equals(SECRET)) {
            throw new InitException("请设置 " + BandDomain.class.getSimpleName() + ".ID 和 " + BandDomain.class.getSimpleName() + ".SECRET 为你自己的，可去阿里云控制台中获取");
        }
        DomainRecorders domainRecorders = getDomainRecords(domain, subDomain);

        String recordId = null;
        List<DomainRecorders.Record> recordList = domainRecorders.DomainRecords.Record;
        for (DomainRecorders.Record record : recordList) {
            if (subDomain == null) {
                if (record.DomainName.equals(domain) && record.RR.equals("@")) {
                    if (record.Value.equals(ip)) {
                        Logger.log("无需修改");
                        return;
                    }
                    recordId = record.RecordId;
                    break;
                }
            } else {
                if (record.DomainName.equals(domain) && record.RR.equals(subDomain)) {
                    if (record.Value.equals(ip)) {
                        Logger.log("无需修改");
                        return;
                    }
                    recordId = record.RecordId;
                    break;
                }
            }
        }
        //存在解析记录，直接修改recordId对应的信息即可
        if (recordId != null) {
            Logger.log("修改解析>>>>>>>>>");
            modifyDomainRecord(subDomain, ip, recordId);
        } else {//不存在解析记录，需要添加
            // 添加解析
//       http://alidns.aliyuncs.com/?
//       Action=AddDomainRecord
//       DomainName=example.com
//       RR=www
//       Type=A
//       Value=202.106.0.20
//       TTL=600
//       Line=default
            Logger.log("添加解析>>>>>>>>>");
            Map<String, String> params = getCommonParam();
            params.put("Action", "AddDomainRecord");
            params.put("DomainName", domain);
            params.put("RR", subDomain == null ? "@" : subDomain);
            params.put("Type", "A");
            params.put("Value", ip);

            execute(params);
        }


    }

    private static void modifyDomainRecord(String subDomain, String ip, String recordId) {
        // 修改解析
//       http://alidns.aliyuncs.com/?
//       Action=UpdateDomainRecord
//       RecordId=123142342
//       RR=www
//       Type=A
//       Value=202.106.0.20
        Map<String, String> params = getCommonParam();
        params.put("Action", "UpdateDomainRecord");
        params.put("RecordId", recordId);
        params.put("RR", subDomain == null ? "@" : subDomain);
        params.put("Type", "A");
        params.put("Value", ip);

        execute(params);

    }

    private static DomainRecorders getDomainRecords(String domain, String subDomain) {

        Map<String, String> param = getCommonParam();

        if (subDomain == null) {
            param.put("Action", "DescribeDomainRecords");
            param.put("DomainName", domain);
        } else {
            param.put("Action", "DescribeSubDomainRecords");
            param.put("SubDomain", subDomain + "." + domain);

        }

        return new Gson().fromJson(execute(param), DomainRecorders.class);
    }

    private static String execute(Map<String, String> params) {
        String signature = getSignature(params);

//        Logger.log(signature);
//        https://alidns.aliyuncs.com/
//        ?Format=xml
//                &Version=2015-01-09
//                &Signature=Pc5WB8gokVn0xfeu%2FZV%2BiNM1dgI%3D
//                &SignatureMethod=HMAC-SHA1
//                &SignatureNonce=15215528852396
//                &SignatureVersion=1.0
//                &AccessKeyId=key-test
//                &Timestamp=2015-01-09T12:00:00Z
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(SERVER)
                .append('?');
        stringBuilder
                .append("Signature=")
                .append(Utils.urlEncode(signature));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuilder
                    .append('&')
                    .append(Utils.urlEncode(entry.getKey()))
                    .append('=')
                    .append(Utils.urlEncode(entry.getValue()));
        }
        Logger.log(stringBuilder.toString());
        String data = request(stringBuilder.toString());
        if (data == null) {
            return null;
        }

        Logger.log(Utils.formatJson(data));
        return data;
    }


    private static String getSignature(Map<String, String> parameters) {
        final String HTTP_METHOD = "GET";
        // 对参数进行排序，注意严格区分大小写
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        final String SEPARATOR = "&";

        // 生成stringToSign字符串
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(Utils.urlEncode("/")).append(SEPARATOR);

        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // 这里注意对key和value进行编码
            canonicalizedQueryString.append("&")
                    .append(Utils.urlEncode(key)).append("=")
                    .append(Utils.urlEncode(parameters.get(key)));
        }

        // 这里注意对canonicalizedQueryString进行编码
        return Utils.hmacSHA1(SECRET, stringToSign.append(Utils.urlEncode(canonicalizedQueryString.toString().substring(1))).toString());
    }


    private static String request(String url) {
        return HttpRequest.request(url);
    }


    private static Map<String, String> getCommonParam() {
        //   公共参数
//    https://alidns.aliyuncs.com/
//    Format=xml
//    Version=2015-01-09
//    Signature=Pc5WB8gokVn0xfeu%2FZV%2BiNM1dgI%3D
//    SignatureMethod=HMAC-SHA1
//    SignatureNonce=Math.random()
//    SignatureVersion=1.0
//    AccessKeyId=yourkey
//    Timestamp=2015-01-09T12:00:00Z
        Map<String, String> params = new TreeMap<>();
        //公共参数
        params.put("Format", "JSON");
        params.put("Version", "2015-01-09");
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("SignatureNonce", UUID.randomUUID().toString());
        params.put("SignatureVersion", "1.0");
        params.put("AccessKeyId", ID);
        params.put("Timestamp", Utils.getTimestamp());
        return params;
    }


    //test
    public static void main(String[] args) {
        addDomainRecord("烫烫.xyz", null, "115.199.103.133");
    }

}



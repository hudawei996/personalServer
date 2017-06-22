package com.wanjian.domain.model;

import java.util.List;

/**
 * Created by wanjian on 2017/6/16.
 */

public class DomainRecorders {

    public static class Record {
        public String RR;//:"mail",
        public String Status;//:"ENABLE",
        public String Value;//:"115.196.137.227",
        public int Weight;//:1,
        public String RecordId;//:"3403064388848640",
        public String Type;//:"A",
        public String DomainName;//:"烫烫.xyz",
        public boolean Locked;//:false,
        public String Line;//:"default",
        public String TTL;//:"600"

    }

    public static class DomainRecords {
        public List<Record> Record;//: [
    }

    public int PageNumber;//: 1,
    public int TotalCount;//: 2,
    public int PageSize;//: 20,
    public String RequestId;//: "3A97419B-8ABB-478C-B6B0-A4B277E12A2E",
    public DomainRecords DomainRecords;//:

}

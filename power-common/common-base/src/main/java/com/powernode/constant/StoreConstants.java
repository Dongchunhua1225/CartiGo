package com.powernode.constant;

public interface StoreConstants {

    //全国地区数据存放到redis中的key
    String ALL_AREA_KEY = "'areaList'";

    //IndexImg再redis中的key
    String WX_INDEX_IMG_KEY = "'wxIndexImg'";

    //wx小程序置顶公告redis中的key
    String WX_TOP_NOTICE_KEY = "'wxTopNotice'";

    //wx小程序所有公告redis中的key
    String WX_ALL_NOTICE_KEY = "'wxAllNotice'";
}

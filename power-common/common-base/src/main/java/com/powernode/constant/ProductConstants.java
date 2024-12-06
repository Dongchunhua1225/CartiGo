package com.powernode.constant;

//产品业务模块常量类
public interface ProductConstants {

    //商品所有类目存放到redis的key
    String ALL_CATEGORY_LIST_KEY = "'allCategory'";

    //商品所有类目存放到redis的key
    String FIRST_CATEGORY_LIST_KEY = "'firstCategory'";

    //状态正常的商品分组标签
    String PROD_TAG_NORMAL_KEY = "'prodTagNormal'";

    //商品属性数据存放在redis的key
    String PROD_PROP_KEY = "'prodProp'";

    //微信小程序商品分许标签再redis中的key
    String WX_PROD_TAG = "'wxProdTag'";
}

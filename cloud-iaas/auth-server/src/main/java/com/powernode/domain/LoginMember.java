package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
    * 用户表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`member`")
public class LoginMember implements Serializable {
    /**
     * 会员表的主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * ID
     */
    @TableField(value = "open_id")
    private String openId;
//
//    /**
//     * 用户昵称
//     */
//    @TableField(value = "nick_name")
//    private String nickName;
//
//    /**
//     * 真实姓名
//     */
//    @TableField(value = "real_name")
//    private String realName;
//
//    /**
//     * 用户邮箱
//     */
//    @TableField(value = "user_mail")
//    private String userMail;
//
//    /**
//     * 支付密码
//     */
//    @TableField(value = "pay_password")
//    private String payPassword;
//
//    /**
//     * 手机号码
//     */
//    @TableField(value = "user_mobile")
//    private String userMobile;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 注册时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 注册IP
     */
    @TableField(value = "user_regip")
    private String userRegip;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    private Date userLasttime;

    /**
     * 最后登录IP
     */
    @TableField(value = "user_lastip")
    private String userLastip;

//    /**
//     * M(男) or F(女)
//     */
//    @TableField(value = "sex")
//    private String sex;
//
//    /**
//     * 例如：2009-11-27
//     */
//    @TableField(value = "birth_date")
//    private String birthDate;
//
//    /**
//     * 头像图片路径
//     */
//    @TableField(value = "pic")
//    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    private Integer score;

    private static final long serialVersionUID = 1L;
}
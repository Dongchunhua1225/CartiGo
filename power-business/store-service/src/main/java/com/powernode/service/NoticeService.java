package com.powernode.service;

import com.powernode.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface NoticeService extends IService<Notice>{

    //新增公告
    Boolean saveNotice(Notice notice);

    //修改公告内容
    Boolean modifyNotice(Notice notice);

    //查询小程序置顶公告列表
    List<Notice> queryTopWXNoticeList();

    //查询小程序所有公告列表
    List<Notice> queryWXAllNoticeNotice();
}

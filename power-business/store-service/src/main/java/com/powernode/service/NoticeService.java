package com.powernode.service;

import com.powernode.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
public interface NoticeService extends IService<Notice>{

    //新增公告
    Boolean saveNotice(Notice notice);

    //修改公告内容
    Boolean modifyNotice(Notice notice);
}

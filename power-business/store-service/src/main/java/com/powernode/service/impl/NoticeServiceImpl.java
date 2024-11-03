package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Notice;
import com.powernode.mapper.NoticeMapper;
import com.powernode.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public Boolean saveNotice(Notice notice) {
        notice.setShopId(1L);
        notice.setCreateTime(new Date());
        notice.setUpdateTime(new Date());
        return noticeMapper.insert(notice) > 0;
    }

    @Override
    public Boolean modifyNotice(Notice notice) {
        notice.setUpdateTime(new Date());
        return noticeMapper.updateById(notice) > 0;
    }

}

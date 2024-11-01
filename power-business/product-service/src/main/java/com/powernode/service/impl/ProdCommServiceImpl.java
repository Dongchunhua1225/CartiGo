package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.ProdComm;
import com.powernode.mapper.ProdCommMapper;
import com.powernode.service.ProdCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService{

    //@Autowired
    //private ProdCommService prodCommService;

    @Autowired
    private ProdCommMapper prodCommMapper;

    //回复和审核商品评论
    @Override
    public Boolean replyAndExamineProdComm(ProdComm prodComm) {
        //获取商品评论内容
        String replyContent = prodComm.getReplyContent();

        //判断评论是否有值
        if(StringUtils.hasText(replyContent) && replyContent != null) {
            prodComm.setReplyTime(new Date());
            prodComm.setReplySts(1);
        }

        return prodCommMapper.updateById(prodComm) > 0;
    }
}

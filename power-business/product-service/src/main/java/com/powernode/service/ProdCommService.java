package com.powernode.service;

import com.powernode.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdCommService extends IService<ProdComm>{

    //回复和审核商品评论
    Boolean replyAndExamineProdComm(ProdComm prodComm);
}

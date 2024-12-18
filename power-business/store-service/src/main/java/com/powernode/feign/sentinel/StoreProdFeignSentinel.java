package com.powernode.feign.sentinel;

import com.powernode.domain.Prod;
import com.powernode.feign.StoreProdFeign;
import com.powernode.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

//门店业务模块调用商品业务模块feign接口的熔断处理
@Component
@Slf4j
public class StoreProdFeignSentinel implements StoreProdFeign {
    @Override
    public Result<List<Prod>> getProdListByIds(List<Long> prodIdList) {
        log.error("根据商品id集合查询商品对象集合，接口调用失败");
        return null;
    }
}

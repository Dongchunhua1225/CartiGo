package com.powernode.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.MemberMapper;
import com.powernode.domain.Member;
import com.powernode.service.MemberService;
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService{

}

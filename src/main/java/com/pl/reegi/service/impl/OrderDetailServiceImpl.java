package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.OrderDetailMapper;
import com.pl.reegi.pojo.OrderDetail;
import com.pl.reegi.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.ShoppingCartMapper;
import com.pl.reegi.pojo.ShoppingCart;
import com.pl.reegi.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}

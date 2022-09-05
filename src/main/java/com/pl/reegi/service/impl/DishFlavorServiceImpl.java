package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.DishFlavorMapper;
import com.pl.reegi.pojo.DishFlavor;
import com.pl.reegi.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

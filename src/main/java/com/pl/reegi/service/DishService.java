package com.pl.reegi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pl.reegi.pojo.Dish;
import com.pl.reegi.pojo.dto.DishDto;

public interface DishService extends IService<Dish> {
    public void saveFoodWithFlavor(DishDto dishDto);

    //根据id查询相应的菜品信息和口味信息
    public DishDto getFoodAndFlavor(Long id);

    //修改菜品信息
    public void updateDish(DishDto dishDto);
}

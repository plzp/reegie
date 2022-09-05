package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.common.CustomException;
import com.pl.reegi.mapper.CategoryMapper;
import com.pl.reegi.pojo.Category;
import com.pl.reegi.pojo.Dish;
import com.pl.reegi.pojo.Setmeal;
import com.pl.reegi.service.CategoryService;
import com.pl.reegi.service.DishService;
import com.pl.reegi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public DishService dishService;
    @Autowired
    public SetmealService setmealService;
    /**
     * 根据id查询是否关联菜品套餐
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) {
        //查询关联菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);

        if(count>0){
            throw new CustomException("删除失败，已关联菜品");
        }
        //查询关联套餐
        LambdaQueryWrapper<Setmeal> qu = new LambdaQueryWrapper<>();
        qu.eq(Setmeal::getCategoryId,id);

        int count1 = setmealService.count(qu);
        if(count1>0){
            throw new CustomException("删除失败，已关联套餐");
        }

        //正常删除
        super.removeById(id);
    }
}

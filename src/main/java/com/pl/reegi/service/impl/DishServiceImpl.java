package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.DishMapper;
import com.pl.reegi.pojo.Dish;
import com.pl.reegi.pojo.DishFlavor;
import com.pl.reegi.pojo.dto.DishDto;
import com.pl.reegi.service.DishFlavorService;
import com.pl.reegi.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;
    @Override
    public void saveFoodWithFlavor(DishDto dishDto) {
        //保存基本信息到Dish表中
        this.save(dishDto);


        List<DishFlavor> flavors = dishDto.getFlavors();
        Long id = dishDto.getId();
        //遍历集合添加dish_Id
        flavors.stream().forEach(dishFlavor -> dishFlavor.setDishId(id));

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询相应的食品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getFoodAndFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> qu = new LambdaQueryWrapper<>();
        qu.eq(DishFlavor::getDishId,id);

        List<DishFlavor> list = dishFlavorService.list(qu);
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void updateDish(DishDto dishDto) {
        //更新菜品表
        this.updateById(dishDto);
        //删除原有口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        //重新将数据加入数据库
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long ids = dishDto.getId();
        //遍历集合添加dish_Id
        flavors.stream().forEach(dishFlavor -> dishFlavor.setDishId(ids));
        dishFlavorService.saveBatch(flavors);
    }
}

package com.pl.reegi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pl.reegi.Utils.CastList;
import com.pl.reegi.common.R;
import com.pl.reegi.pojo.Category;
import com.pl.reegi.pojo.Dish;
import com.pl.reegi.pojo.DishFlavor;
import com.pl.reegi.pojo.dto.DishDto;
import com.pl.reegi.service.CategoryService;
import com.pl.reegi.service.DishFlavorService;
import com.pl.reegi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取所有菜品
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);

        queryWrapper.orderByAsc(Dish::getId);

        dishService.page(dishPage,queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        //获得查询的菜品集合
        List<Dish> records = dishPage.getRecords();
        //新的集合
        List<DishDto> dishDtos = new ArrayList<>();

        records.stream().forEach(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        });

        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        //清理redis里的菜品缓存
        String keys = "dish_"+dishDto.getCategoryId()+"1";
        redisTemplate.delete(keys);

        dishService.saveFoodWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    /**
     * 删除菜品
     *
     * @param ids 菜品id
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {

        //清理redis里的菜品缓存
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        for (Long id : ids) {
            dishService.removeById(id);
        }

        return R.success("删除成功");
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        //清理redis里的菜品缓存
        String keys = "dish_"+dishDto.getCategoryId()+"1";
        redisTemplate.delete(keys);
        dishService.updateDish(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据id获得菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getFood(@PathVariable Long id) {
        DishDto food = dishService.getFoodAndFlavor(id);
        return R.success(food);
    }

    @PostMapping("/status/{state}")
    public R<String> updateState(@RequestParam List<Long> ids, @PathVariable int state) {

        for (Long id : ids) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId, id);
            updateWrapper.set(Dish::getStatus, state);

            dishService.update(updateWrapper);
        }

        return R.success("修改成功");
    }
/*
    @GetMapping("/list")
    public R<List<Dish>> getList(Long categoryId){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,categoryId);
        dishLambdaQueryWrapper.orderByAsc(Dish::getPrice);

        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return R.success(list);
    }*/
@GetMapping("/list")
public R<List<DishDto>> list(Dish dish){
    List<DishDto> dishDtoList=null;
    //先从redis中获取缓存数据
    String key = "dish_"+dish.getCategoryId()+dish.getStatus();
    Object o = redisTemplate.opsForValue().get(key);

    dishDtoList = CastList.castList(o, DishDto.class);

    //如果存在，则直接返回
    if (dishDtoList!=null){
        return R.success(dishDtoList);
    }

    //构造查询条件
    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
    //添加条件，查询状态为1（起售状态）的菜品
    queryWrapper.eq(Dish::getStatus,1);

    //添加排序条件
    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

    List<Dish> list = dishService.list(queryWrapper);

    dishDtoList = list.stream().map((item) -> {
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(item,dishDto);

        Long categoryId = item.getCategoryId();//分类id
        //根据id查询分类对象
        Category category = categoryService.getById(categoryId);

        if(category != null){
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
        }

        //当前菜品的id
        Long dishId = item.getId();
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        //SQL:select * from dish_flavor where dish_id = ?
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }).collect(Collectors.toList());

    //如果不存在，则从数据库查询，然后将数据存入redis中
    redisTemplate.opsForValue().set(key,dishDtoList);
    return R.success(dishDtoList);
}


}

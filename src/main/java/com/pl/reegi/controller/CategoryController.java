package com.pl.reegi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pl.reegi.common.R;
import com.pl.reegi.pojo.Category;
import com.pl.reegi.service.CategoryService;
import com.pl.reegi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishService dishService;

    /**
     * 新增菜品分类
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("分类："+category);
        categoryService.save(category);
        return R.success("新增成功");
    }

    /**
     * 获取所有菜品分类
     * @return
     */
    @GetMapping("/page")
    public R<Page> getAll(int page,int pageSize){

        Page<Category> pageInfo = new Page<>(page,pageSize);
        //构造分页构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //调用service层执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        //判断该分类下面是否有菜品
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody Category category){
       categoryService.updateById(category);
       return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}

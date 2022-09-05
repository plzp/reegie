package com.pl.reegi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pl.reegi.common.R;
import com.pl.reegi.pojo.Category;
import com.pl.reegi.pojo.Setmeal;
import com.pl.reegi.pojo.dto.SetmealDto;
import com.pl.reegi.service.CategoryService;
import com.pl.reegi.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 获得所有套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        List<SetmealDto> list = new ArrayList<>();

        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByAsc(Setmeal::getPrice);

        //获取setmeal对象页面
        setmealService.page(setmealPage,queryWrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        //遍历Records集合取出分类id查出分类名称
        setmealPage.getRecords().stream().forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            list.add(setmealDto);
        });
        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealAndSdish(setmealDto);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> update(@RequestParam List<Long> ids){
        setmealService.deleteSDishAndSetmeal(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids,@PathVariable int status){
        ids.stream().forEach(id->{
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId,id);
            updateWrapper.set(Setmeal::getStatus,status);
            setmealService.update(updateWrapper);
        });
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealByid(id);

        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

}

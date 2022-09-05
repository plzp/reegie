package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.SetmealMapper;
import com.pl.reegi.pojo.Setmeal;
import com.pl.reegi.pojo.SetmealDish;
import com.pl.reegi.pojo.dto.SetmealDto;
import com.pl.reegi.service.SetmealDishService;
import com.pl.reegi.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void saveSetmealAndSdish(SetmealDto setmealDto) {
        this.save(setmealDto);
        //套餐菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getName,setmealDto.getName());

        Setmeal setmeal = this.getOne(setmealLambdaQueryWrapper);

        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteSDishAndSetmeal(List<Long> ids) {
       ids.stream().forEach(id->{
           this.removeById(id);
           LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(SetmealDish::getSetmealId,id);
           setmealDishService.remove(queryWrapper);
       });
    }

    @Override
    public SetmealDto getSetmealByid(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        setmealDishes.stream().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
        });
        setmealDishService.saveBatch(setmealDishes);
    }
}

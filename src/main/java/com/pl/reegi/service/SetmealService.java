package com.pl.reegi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pl.reegi.pojo.Setmeal;
import com.pl.reegi.pojo.dto.SetmealDto;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveSetmealAndSdish(SetmealDto setmealDto);

    void deleteSDishAndSetmeal(List<Long> ids);

    SetmealDto getSetmealByid(Long id);

    void updateSetmeal(SetmealDto setmealDto);
}

package com.pl.reegi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pl.reegi.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
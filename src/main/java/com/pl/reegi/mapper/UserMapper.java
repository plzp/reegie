package com.pl.reegi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pl.reegi.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

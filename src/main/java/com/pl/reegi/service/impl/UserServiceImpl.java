package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.UserMapper;
import com.pl.reegi.pojo.User;
import com.pl.reegi.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

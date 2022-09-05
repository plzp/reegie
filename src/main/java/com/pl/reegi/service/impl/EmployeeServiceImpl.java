package com.pl.reegi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.EmployeeMapper;
import com.pl.reegi.pojo.Employee;
import com.pl.reegi.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}

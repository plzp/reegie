package com.pl.reegi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pl.reegi.common.R;
import com.pl.reegi.pojo.Employee;
import com.pl.reegi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/employee")
@ResponseBody
public class EmployeeController {

    @Autowired
    private EmployeeService em;

    /*员工登录*/
    @PostMapping("/login")
    public R login(HttpServletRequest request,@RequestBody Employee employee){
        //将密码进行md5加密处理
         String  password = employee.getPassword();
         password = DigestUtils.md5DigestAsHex(password.getBytes());

         //根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee user = em.getOne(queryWrapper);

        //用户名比对
        if(user ==null){
            return R.error("登录失败");
        }
        //密码比对
        if(!user.getPassword().equals(password)){
            return R.error("登陆失败");
        }
        //状态比对
        if(user.getStatus() == 0){
            return R.error("账号已禁用");
        }
        //登录成功，将员工存入session中并返回登陆结果
        request.getSession().setAttribute("USER",user.getId());
        return R.success(user);
    }
    //登出功能实现
    @PostMapping("/logout")
    public R<String> loginOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("USER");
        return R.success("退出成功");
    }

    //添加员工功能实现
    @PostMapping
    public R addUser(@RequestBody Employee employee,HttpServletRequest request){
        //设置员工初始密码并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("1234567".getBytes()));

  /*      employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long userId = (Long) request.getSession().getAttribute("USER");

        employee.setCreateUser(userId);
        employee.setUpdateUser(userId);*/

        em.save(employee);

        return R.success("添加成功");
    }

    /**
     * 员工信息的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
   @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        Page pageInfo = new Page(page, pageSize);
        //构造分页构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询条件
        em.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
   }

   @PutMapping
   public R<String> update(@RequestBody Employee employee,HttpServletRequest request){

       /*employee.setUpdateTime(LocalDateTime.now());
       employee.setUpdateUser((Long)request.getSession().getAttribute("USER"));*/
       em.updateById(employee);
       return R.success("员工信息修改成功");
   }

    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
   public R<Employee> getById(@PathVariable Long id){
        Employee employee = this.em.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
     return R.error("没有查询到员工信息");
    }
}

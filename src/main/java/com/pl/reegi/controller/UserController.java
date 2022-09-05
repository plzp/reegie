package com.pl.reegi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pl.reegi.Utils.SMSUtils;
import com.pl.reegi.Utils.ValidateCodeUtils;
import com.pl.reegi.common.R;
import com.pl.reegi.pojo.User;
import com.pl.reegi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){

            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //将验证码存入session
            request.getSession().setAttribute(phone,code);

           /* //调用阿里云api发送短信
            SMSUtils.sendMessage("DSAA","",phone,code);*/
           log.info(code);

            return R.success("短信发送成功");
        }
        return R.error("发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map user,HttpServletRequest request){

        String phone = user.get("phone").toString();
        String code =  user.get("code").toString();

        //获得Session中存的code
        String code1 = request.getSession().getAttribute(phone).toString();

        if(!code1.equals(code)){
            return R.error("验证码输入错误");
        }

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone,phone);

        User use = userService.getOne(userLambdaQueryWrapper);
        if (use == null){
            use = new User();
            use.setPhone(phone);
            use.setStatus(1);
            userService.save(use);
        }
        request.getSession().setAttribute("user",use.getId());
        return R.success(use);
    }
}

package com.pl.reegi.common;

/**
 * 基于ThreadLocal,用于保存和获取当前用户的id
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrent(){
        return threadLocal.get();
    }
}

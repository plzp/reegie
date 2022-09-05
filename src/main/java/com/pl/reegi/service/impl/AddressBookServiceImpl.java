package com.pl.reegi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pl.reegi.mapper.AddressBookMapper;
import com.pl.reegi.pojo.AddressBook;
import com.pl.reegi.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}

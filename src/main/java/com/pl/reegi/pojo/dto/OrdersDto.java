package com.pl.reegi.pojo.dto;


import com.pl.reegi.pojo.OrderDetail;
import com.pl.reegi.pojo.Orders;
import lombok.Data;

import java.util.List;


@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}

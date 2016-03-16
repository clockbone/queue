package com.clockbone.service;

import com.clockbone.mapper.OrderMapper;
import com.clockbone.model.Order;
import com.clockbone.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by qinjun on 2016/3/16.
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Response<List<Order>> getOrder(String name){
        List<Order> orders = orderMapper.selectByName(name);
        Response<List<Order>> response = new Response<List<Order>>(orders);
        return response;
    }

    public Response<Boolean> updateOrder(Integer id){
        Order order = new Order();
        order.setId(id);
        order.setUpdateTime(new Date());
        order.setOrderStatus("0");
        int re = orderMapper.updateByPrimaryKeySelective(order);
        if(re==1){
            return new Response<Boolean>(true);
        }
        return new Response<Boolean>(false);
    }


}

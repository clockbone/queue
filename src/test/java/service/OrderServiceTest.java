package service;

import com.clockbone.mapper.OrderMapper;
import com.clockbone.model.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by qinjun on 2016/3/10.
 */
public class OrderServiceTest extends BaseTest {

    @Autowired
    public OrderMapper orderMapper;


    public void MapperTest(){
        for(int i=0;i<10;i++){
            Order o = new Order();
            o.setCreatetime(new Date());
            o.setOrderId(UUID.randomUUID().toString());
            o.setUserName("user"+i+1);
            orderMapper.insert(o);
        }
    }

    @Test
    public void allTest(){
        selectByNameTest();
    }

    public void selectByNameTest(){
        String name="user11";
        List<Order> o = orderMapper.selectByName(name);

        Order order = new Order();
        order.setId(11);
        order.setOrderStatus("0");
        orderMapper.updateByPrimaryKeySelective(order);
        System.out.println(o);
    }
}

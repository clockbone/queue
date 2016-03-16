package com.clockbone;

import com.clockbone.model.Order;
import com.clockbone.model.Response;
import com.clockbone.service.MainService;
import com.clockbone.service.OrderService;
import com.clockbone.until.ConfigManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by qinjun on 2016/3/14.
 */
public class QueueCustomer  extends Thread{

    private QueueStorage queueStorage;

    private int sleepTime;

    private volatile boolean RUN = true;

    private static  String cof;

    public QueueCustomer(QueueStorage queueStorage,int sleepTime){
        this.queueStorage=queueStorage;
        this.sleepTime=sleepTime;

    }

    //private static MainService mainService;

    private static OrderService orderService;

    static{
        //mainService = (MainService) Bootstrap.context.getBean("mainService");
        orderService = (OrderService) Bootstrap.context.getBean("orderService");

        cof= ConfigManager.getProperty("cofig");
    }

    @Override
    public void run() {
        while (RUN){
            Order order = queueStorage.get();
            System.out.println("消费者==========================="+order);
            if(null != order){
                execute(order);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

        }

    }
    public void execute(Order order){
        if(null!=order&&null!=order.getId()){
            Response<Boolean> response = orderService.updateOrder(order.getId());
            if(response.isSuccess()){
                System.out.println("处理更新第成功。。。"+order.getOrderId());
            }
            System.out.println(response);
        }
    }

    /**
     * @Description: 虚拟机停止前执行
     * @param @param wr
     * @return void
     * @throws
     */
    public  void doShutdownHook(final QueueCustomer wr){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                System.out.println("【队列正在退出...】");
                RUN=false;
                int attempCount=3;
                while(attempCount>0&&wr.isAlive()){
                    try {
                        wr.join();
                    } catch (InterruptedException e) {
                    }
                    attempCount--;
                }
                System.out.println("【队列已退出】");
            }
        });
    }
}

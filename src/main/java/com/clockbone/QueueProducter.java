package com.clockbone;

import com.clockbone.model.Order;
import com.clockbone.exception.ConfigErrorException;
import com.clockbone.model.Response;
import com.clockbone.service.MainService;
import com.clockbone.service.OrderService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by qinjun on 2016/3/14.
 */
public class QueueProducter extends Thread {

    private QueueStorage queueStorage;

    private int storageSize;

    private int sleepTime;


    //private final static MainService mainService;

    private final static OrderService orderService;

    private volatile boolean RUN = true;

    public QueueProducter(QueueStorage queueStorage,int size,int sleepTime){
        super();
        this.queueStorage=queueStorage;
        this.storageSize=size;
        this.sleepTime=sleepTime;
    }

    static {
        //applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        if(null == Bootstrap.context){
             throw new ConfigErrorException("check the xml");
        }
        //mainService =(MainService) Bootstrap.context.getBean("mainService");
        orderService=(OrderService)Bootstrap.context.getBean("orderService");

        //if(null == mainService){
        //    throw new ConfigErrorException("mainService is null,check the xml");
        //}
    }
    @Override
    public void run() {
        while(RUN){

            List<Order> list = null;
            Response<List<Order>> response = orderService.getOrder("user11");
            System.out.println("===============生产者数据=======================");
            System.out.println(response);
            System.out.println("========================生产者数据vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv==============");
            if(response.isSuccess()){
                list = response.getResult();
            }
            if(null != list||list.size()>0){
                queueStorage.put(list);

            }
            try{
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    *
    *  public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadTesterA());
        Thread t2 = new Thread(new ThreadTesterB());
        t1.start();
        t1.join(); // wait t1 to be finished
        t2.start();
        t2.join(); // in this program, this may be removed
    }
    * */

    public void doShutdownHook(final QueueProducter wr){
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                System.out.println("is out...");
                RUN=false;
                int attempCount=3;

                while(attempCount>0&&wr.isAlive()){
                    try {
                        wr.join();// 前面判断wr.siAlive是否存活，如何存活，调用wr.join 即是等wr运行完，在执行run()方法后面的方法（重试三次）
                        //避免手动停止项目时，即停止jvm时 而线程的run方法还没执行完。影响业务
                    } catch (InterruptedException e) {
                    }
                    attempCount--;
                }
                System.out.println("is out ready...");
            }
        });
    }
}

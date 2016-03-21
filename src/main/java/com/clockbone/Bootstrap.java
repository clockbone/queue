package com.clockbone;

import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by qinjun on 2016/3/14.
 */
public class Bootstrap extends AbstractIdleService {

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    public static ClassPathXmlApplicationContext context;

    public static void main(String[] args){
        Bootstrap queueStarter = new Bootstrap();
        queueStarter.startAsync();
        try {
            Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch (InterruptedException ex) {
            log.error("ignore interruption");
        }



    }

    @Override
    protected void startUp() throws Exception {
        context = new ClassPathXmlApplicationContext(new String[] {"/application-context.xml"});
        context.start();
        context.registerShutdownHook();
        log.info("service started successfully");

        QueueStorage queueStorage = new QueueStorage(20);

        QueueProducter queueProducter = new QueueProducter(queueStorage,20,2000);
        queueProducter.doShutdownHook(queueProducter);
        //启动一个生产者
        queueProducter.start();


        //ExecutorService executorService = ThreadPoolExecutor
        //程池	ExecutorService executorService = Executors.newCachedThreadPool();
        //启动4个消费者
        for(int i=0;i<4;i++){
            QueueCustomer queueCustomer = new QueueCustomer(queueStorage,1000);
            queueCustomer.doShutdownHook(queueCustomer);
            queueCustomer.start();
        }

    }

    @Override
    protected void shutDown() throws Exception {
        context.stop();
        log.info("service stopped successfully");

    }
}

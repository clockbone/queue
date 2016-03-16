package com.clockbone;

import com.clockbone.model.Order;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qinjun on 2016/3/14.
 */
public class QueueStorage {

    public volatile  int size = 50;//队列初始化大小
    private LinkedList<Order> container = new LinkedList<Order>();//容器

    private Lock lock = new ReentrantLock();//lock
    private Condition getCondition = lock.newCondition();//读线程锁
    private Condition putCondition = lock.newCondition();//写线程锁

    public QueueStorage(int size){
        this.size = size;
    }


    /**
     * When a thread calls the await() method of a condition,
     * it automatically frees the control of the lock,
     * so that another thread can get it and begin the execution of the same,
     * or another critical section protected by that lock.
     */
    public void put(List<Order> queueList) {
        lock.lock();
        try {
            while (container.size() >= size) {
                putCondition.await();
            }
            for (Order alQueue : queueList) {
                container.addLast(alQueue);
            }
            getCondition.signalAll();
        } catch (Exception e) {
            //throw new Exception( e);
        } finally {
            lock.unlock();
        }
    }


    /**
     * 获取一条记录
     */
    public Order get() {
        lock.lock();
        Order alQueue = null;
        try {
            while (container.size() == 0) {
                getCondition.await();
            }
            alQueue = container.removeFirst();
            putCondition.signal();
        } catch (InterruptedException e) {
            //throw new BusinessException(e);
        } finally {
            lock.unlock();
        }
        return alQueue;
    }

}

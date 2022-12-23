package com.vamshi.morgan.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//https://www.youtube.com/watch?v=N0mMm5PF5Ow&ab_channel=DefogTech
public class ReentrantWithCondition {

    private Lock lock = new ReentrantLock();
    private Condition conditionMet = lock.newCondition();

    public void method1() throws InterruptedException{
        lock.lock();
        try{

            conditionMet.await(); // suspend here, await() method releases the lock for thread scheduling purposes
                                  // hover over await for docs
            System.out.println("Doing Dependent tasks second");
        }finally {
            lock.unlock();
        }
    }

    public void method2() throws InterruptedException{
        lock.lock();
        try{

            // do independent operations here
            System.out.println("Doing Independent tasks first");
            conditionMet.signal();
//            conditionMet.signalAll(); this is needed when you have more more threads t3,t4 etc.,
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        ReentrantWithCondition commonObject = new ReentrantWithCondition();

        System.out.println("Starting main thread");
        Thread t1 = new Thread(() -> {
            try {
                commonObject.method1();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
        });

        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                commonObject.method2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
        });

        t2.start();

        System.out.println("Ending main thread");

    }

}

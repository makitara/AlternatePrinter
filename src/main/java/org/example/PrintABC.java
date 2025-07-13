package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class PrintABC {

    // 要打印的次数
    public int count = 1000;

    // 用于标记要第一个打印的字母
    public int flag = 0;

    // 要打印的字母个数
    public final static int NUMS = 3;


    /*
     * 打印字母
     * param c 要打印的字母
     * param rank 线程编号(排序)
     */
    public void myPrint(char c, int rank) {
        for(int i = 0; i < count; i++) {
            synchronized (this) {
                while (flag % NUMS != rank) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(c);
                // 延时1ms方便观察
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                flag++;
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();
        //调用的顺序不影响输出结果
        new Thread(() -> printABC.myPrint('b', 1)).start();
        new Thread(() -> printABC.myPrint('a', 0)).start();
        new Thread(() -> printABC.myPrint('c', 2)).start();
    }
}

package com.witcher.lib;

//测试指令重排
public class ZhiLingChongPai {

    private static boolean isReady;
    private static int number;

    public static void main(String[] args){
        //1
        new ReaderThread().start();
        //2
        number = 42;
        //3
        isReady = true;
        // 结果1 123 然后输出42
        // 结果2 132 然后输出0
        // 结果3 无限循环 没有输出
    }

    private static class ReaderThread extends Thread{
        @Override
        public void run(){
            while (!isReady){
//                Thread.yield();
            }
            System.out.println(number);
        }
    }

}

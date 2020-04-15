package com.example.mvvmapp;

/**
 * Created by yzh on 2020/1/7 15:00.
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(    ArithUtils.roundSub("11.0",0));
//        System.out.println(    ArithUtils.roundSub("11.1",0));
//        System.out.println(    ArithUtils.roundSub("11.6",0));
//        String s="the number one is-good!";
//        System.out.println("s="+s.indexOf("one is"));
//        System.out.println("s:"+s.charAt(s.indexOf("one is")+"one is".length()));


        String s1="aa   bb  ccc d!";
        System.out.println("s1:"+s1);
        String s2=s1.replaceAll(" {2,}"," ");//将2个以上的空格，替换为1个空格(即多个空格只保留1个)
        System.out.println("s2:"+s2);
    }
}

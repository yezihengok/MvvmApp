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

//
//        List<String> strings=new ArrayList<>();
//        strings.add("aa");
//        strings.add("bb");
//        strings.add("cc");
//        strings.add("dd");
//        strings.add("ee");
//
//        for (int i = 0; i <strings.size() ; i++) {
//            System.out.println("ii:"+i);
//            if(strings.get(i).equals("ee")){
//                System.out.println("移除的数据:"+strings.get(i));
//                strings.remove(strings.get(i));
//                i--;
//            }
//        }
//        for (String s:strings){
//            System.out.println("ss:"+s);
//        }

        String ss="\n";
        String ss1="ss\n";
        String ss2="ss\nss";
        System.out.println(String.format("ss %s ss1 %s ss2 %s",ss.length(),ss1.length(),ss2.length()));
    }
}

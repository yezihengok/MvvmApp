package com.example.mvvmapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by yzh on 2020/1/7 15:00.
 */
public class Test1 {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {

/*        String msg="今天(天气)不（错的）岑参听说要出差#%cai1%#去上海（第一）(第2)";

        List<String> stringList= CommUtils.getSubUtil3(msg,"(?<=（)(.+?)(?=）)","（","）");
        boolean has=false;
        for (int i = 0; i <stringList.size() ; i++) {
            String target=stringList.get(i);
            String last=msg.substring(msg.length()-target.length());
            System.out.println(String.format("target:%s last%s",target,last));
            if(target.equals(last)){
                has=true;
                msg=msg.substring(0,msg.length()-target.length());
            }
        }

        if(!has){
            List<String> stringList2= CommUtils.getSubUtil3(msg,"(?<=\\()(.+?)(?=\\))","(",")");
            for (int i = 0; i <stringList2.size() ; i++) {
                String target=stringList2.get(i);
                String last=msg.substring(msg.length()-target.length());
                System.out.println(String.format("target2:%s last%s",target,last));
                if(target.equals(last)){
                    msg=msg.substring(0,msg.length()-target.length());
                }
            }
        }


        System.out.println("msg: "+msg);*/

        //先匹配 末尾是否有（二） 的内容
/*        String msg="今天(天气)不（错的）岑参听说要出差#%cai1%#去上海(一)（三）(五)";
        boolean has=false;
        List<String> list= CommUtils.getSubUtil2(msg,"(?<=（)(.+?)(?=）)");
        if(CommUtils.isListNotNull(list)){
            List<String> stringList= CommUtils.getSubUtil3(msg,"(?<=（)(.+?)(?=）)","（","）");

            for (int i = 0; i <stringList.size() ; i++) {
                String target=stringList.get(i);
                String last=msg.substring(msg.length()-target.length());
                System.out.println(String.format("target:%s last%s",target,last));
                if(target.equals(last)){
                    has=true;
                    msg=msg.substring(0,msg.length()-target.length());
                }
            }
        }

        //先匹配 末尾是否有(二) 的内容
        List<String> list2= CommUtils.getSubUtil2(msg,"(?<=\\()(.+?)(?=\\))");
        if(!has&&CommUtils.isListNotNull(list2)){
            List<String> stringList2= CommUtils.getSubUtil3(msg,"(?<=\\()(.+?)(?=\\))","(",")");
            for (int i = 0; i <stringList2.size() ; i++) {
                String target=stringList2.get(i);
                String last=msg.substring(msg.length()-target.length());
                System.out.println(String.format("target2:%s last%s",target,last));
                if(target.equals(last)){
                    msg=msg.substring(0,msg.length()-target.length());
                }
            }
        }

        System.out.println("msg: "+msg);*/
        //System.out.print();
    }


}

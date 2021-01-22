package com.springcloud.test;

public class StringTableTest {

    public static void main(String[] args) {
        String s1="a";
        String s2="b";
        String s3="ab";
        String s4=s1+s2;
        String s5="a"+"b";
        String s6=s4.intern();
        //判断输出结果？
        System.out.println(s3==s4);
        System.out.println(s3==s5);
        System.out.println(s3==s6);

    }

}

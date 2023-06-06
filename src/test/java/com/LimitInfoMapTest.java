package com;

import com.codingcube.properties.LimitInfoMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class LimitInfoMapTest {
    public static void main(String[] args) {
        baseTest();
        System.out.println();
    }

    public static void baseTest(){
        int i = 0;
        while (true){
            i++;
            final Boolean aBoolean = LimitInfoMap.addRecord("check", "CodingCube", 10, 20, 60);
            System.out.println(aBoolean);
        }
    }

//    public static void JUCTest(){
//        new Thread(()->{
//            final Boolean aBoolean = LimitInfoMap.addRecord("check", "CodingCube", 10, 2000);
//            final Boolean bBoolean = LimitInfoMap.addRecord("check", "CodingCube2", 10, 2000);
//            System.out.println(aBoolean);
//        }).start();
//        new Thread(()->{
//            final Boolean aBoolean = LimitInfoMap.addRecord("check", "CodingCube", 10, 2000);
//            final Boolean bBoolean = LimitInfoMap.addRecord("check", "CodingCube2", 10, 2000);
//            System.out.println(aBoolean);
//        }).start();
//    }

}

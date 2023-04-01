package com.codingcube.aspect;

import com.codingcube.properties.Proper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    @Autowired
    private Proper proper;

    public String getTest(){
        return proper.toString();
    }
}

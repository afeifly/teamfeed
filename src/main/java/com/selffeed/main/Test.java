package com.selffeed.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test {
    @Value("${private.testx}")
    private int test;
    @Value("${max:200}")
    private int max;

    public int test(){
        log.info("hhh");
        log.info("test===="+test);
        return test;
    }
}

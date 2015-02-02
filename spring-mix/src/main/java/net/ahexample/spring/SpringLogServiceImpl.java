package net.ahexample.spring;

import net.ahexample.nonspring.LogService;


public class SpringLogServiceImpl implements LogService {
    public void log(String message) {
        System.out.println("message = " + message);
    }
}

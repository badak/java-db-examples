package net.ahexample.nonspring;


import net.ahexample.spring.SpringLogServiceLink;


public class LogUser {
    public LogService getLogService() {
        return SpringLogServiceLink.getLogService();
    }
}

package net.ahexample.spring;


import net.ahexample.nonspring.LogService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringLogServiceLink implements ApplicationContextAware {
    /** Singleton instance of Spring bean. */
    private static LogService logService = null;

    public static LogService getLogService() {
        return logService;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logService = applicationContext.getBean("springLogServiceImpl", LogService.class);
    }
}

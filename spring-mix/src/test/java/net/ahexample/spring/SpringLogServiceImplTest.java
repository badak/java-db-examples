package net.ahexample.spring;


import net.ahexample.nonspring.LogService;
import net.ahexample.nonspring.LogUser;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;


public class SpringLogServiceImplTest {
    @SuppressWarnings("resource")
    @Test
    public void logServiceInLogUserInitialisedWhenSpringBeanReferenced() {
        ApplicationContext appContext;
        LogService logService;
        LogUser logUser = new LogUser();

        // logService in logUser not initialised before Spring application context is loaded
        assertThat(logUser.getLogService(), nullValue());
        appContext = new ClassPathXmlApplicationContext("spring-config.xml");
        // logService in logUser initialised after Spring application context is loaded
        assertThat(logUser.getLogService(), notNullValue());

        logService = appContext.getBean("springLogServiceImpl", SpringLogServiceImpl.class);
        // logService in logUser is identical to Spring bean
        assertThat(logUser.getLogService(), equalTo(logService));
    }
}
package sa.com.is.imapp.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by snouto on 28/11/15.
 */
@Component("springSystemBridge")
public class SpringSystemBridge implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        context = applicationContext;
    }

    public static SystemService services() throws Exception{

        SystemService systemService =  context.getBean(SystemService.class);

        return systemService;
    }
}

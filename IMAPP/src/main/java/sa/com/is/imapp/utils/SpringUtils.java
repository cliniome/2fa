package sa.com.is.imapp.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Created by snouto on 25/11/15.
 */


public class SpringUtils {


    public static Object getSpringBean(String name)
    {
        try
        {
            FacesContext context = FacesContext.getCurrentInstance();

            ApplicationContext springContext = WebApplicationContextUtils
                    .getWebApplicationContext((ServletContext) context
                            .getExternalContext().getContext());


            return springContext.getBean(name);

        }catch(Exception s)
        {

            return null;
        }
    }

    public static Object getSpringBean(Class<?> className)
    {
        return getSpringBean(className.getName());
    }

}

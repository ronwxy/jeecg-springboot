package org.jeecgframework;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.web.system.listener.InitListener;
import org.jeecgframework.web.system.listener.OnlineListener;
import org.jeecgframework.web.system.service.impl.RedisCacheService;
import org.jeecgframework.web.system.servlet.RandCodeImageServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

@Configuration
@ImportResource({"classpath:redis.xml", "classpath:spring-mvc-cgform.xml", "classpath:config/spring-config-p3.xml"})
public class JeecgAutoConfig {


//    @Bean
//    public ApplicationContextUtil applicationContextUtil() {
//        return new ApplicationContextUtil();
//    }

    @Bean
    public ServletRegistrationBean randCodeImageServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new RandCodeImageServlet());
        servletRegistrationBean.addUrlMappings("/randCodeImage");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean penSessionInViewFilter(){
//        OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new OpenSessionInViewFilter());
        filterRegistrationBean.addUrlPatterns("*.do");
        filterRegistrationBean.addUrlPatterns("/rest/*");
        return filterRegistrationBean;
    }

//    @Bean
//    public FilterRegistrationBean encodingFilter(){
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        filter.setEncoding("UTF-8");
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }


    @Bean
    public ServletListenerRegistrationBean initListener(){
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean(new InitListener());
        return listenerRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean onlineListener(){
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean(new OnlineListener());
        return listenerRegistrationBean;
    }

    @Bean
    public RedisCacheService redisCacheService(){
        return new RedisCacheService();
    }

    @Bean
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    @Bean
    public org.jeecgframework.p3.core.utils.common.ApplicationContextUtil p3ApplicationContextUtil(){
        return new org.jeecgframework.p3.core.utils.common.ApplicationContextUtil();
    }

//    @Bean
//    public StringHttpMessageConverter stringHttpMessageConverter(){
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        List<MediaType> mediaTypes = Lists.newArrayList();
//        mediaTypes.add(MediaType.TEXT_PLAIN);
//        converter.setSupportedMediaTypes(mediaTypes);
//        return converter;
//    }
}

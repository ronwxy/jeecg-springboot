package org.jeecgframework;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import org.jeecgframework.core.extend.datasource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnMissingBean(DruidDataSource.class)
@EnableConfigurationProperties(DruidDataSourceProperties.class)
//@EnableTransactionManagement
public class DruidDataSourceAutoConfig {
    @Autowired
    private DruidDataSourceProperties dataSourceProperties;

    public DruidDataSourceAutoConfig(){
        System.out.println("abc");
    }

    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean(DataSource.class)
    @Primary
    public DruidDataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(dataSourceProperties.getFilters());
        } catch (SQLException e) {
            //TODO 异常处理
        }
        datasource.setConnectProperties(dataSourceProperties.getConnectionProperties());
        datasource.setUseGlobalDataSourceStat(dataSourceProperties.isUseGlobalDataSourceStat());
        return datasource;
    }

    /**
     * 注册一个StatViewServlet
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource.druid", name = "druidServletSettings")
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", dataSourceProperties.getDruidServletSettings().getOrDefault("allow", "127.0.0.1"));
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", dataSourceProperties.getDruidServletSettings().getOrDefault("deny", "192.168.1.73"));
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", dataSourceProperties.getDruidServletSettings().getOrDefault("loginUsername", "admin"));
        servletRegistrationBean.addInitParameter("loginPassword", dataSourceProperties.getDruidServletSettings().getOrDefault("loginPassword", "Passw0rd"));
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", dataSourceProperties.getDruidServletSettings().getOrDefault("resetEnable", "false"));// 禁用HTML页面上的“Reset All”功能
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource.druid", name = "druidFilterSettings")
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.setName("druidWebStatFilter");
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", dataSourceProperties.getDruidFilterSettings().getOrDefault("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"));
        return filterRegistrationBean;
    }


    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSource dynamicDataSource(DataSource dataSource){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<String, Object> targetDataSources = Maps.newHashMap();
        targetDataSources.put("dataSource_jeecg", dataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSource);
        return dynamicDataSource;
    }


    @Bean(name = "sessionFactory")
    @ConditionalOnMissingBean
    public LocalSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setEntityInterceptor();
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto","none");
        hibernateProperties.put("hibernate.show_sql","true");
        hibernateProperties.put("hibernate.format_sql","true");
        hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        sessionFactory.setHibernateProperties(hibernateProperties);
        sessionFactory.setPackagesToScan("org.jeecgframework.web.system.pojo.*",
                "org.jeecgframework.web.test.entity.*",
                "org.jeecgframework.web.autoform.*",
                "org.jeecgframework.web.cgform.entity.*",
                "org.jeecgframework.web.cgreport.entity.*",
                "org.jeecgframework.web.cgdynamgraph.entity.*",
                "org.jeecgframework.web.graphreport.entity.*",
                "org.jeecgframework.web.system.sms.*",
                "org.jeecgframework.web.black.*",
                "org.jeecgframework.web.superquery.*",
                "com.jeecg.*",
                "com.cnbot.*"
                );
        return sessionFactory;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(){
    return new LocalValidatorFactoryBean();
    }


//    @Bean
//    public MiniDaoBeanScannerConfigurer miniDaoBeanScannerConfigurer(){
//        MiniDaoBeanScannerConfigurer daoBeanScannerConfigurer = new MiniDaoBeanScannerConfigurer();
//        daoBeanScannerConfigurer.setKeyType("lower");
//        daoBeanScannerConfigurer.setFormatSql(false);
//        daoBeanScannerConfigurer.setShowSql(false);
//        daoBeanScannerConfigurer.setDbType("mysql");
//        daoBeanScannerConfigurer.setBasePackage("org.jeecgframework.web,com.jeecg,com.cnbot");
//        daoBeanScannerConfigurer.setAnnotation(Repository.class);
//        return daoBeanScannerConfigurer;
//    }


}

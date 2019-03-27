package org.jeecgframework;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;

@ConfigurationProperties(prefix = "spring.datasource.druid")
@Getter
@Setter
public class DruidDataSourceProperties {

    public DruidDataSourceProperties(){
        System.out.println("bbb");
    }
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 1; // 初始化大小
    private int minIdle = 1; // 最小连接
    private int maxActive = 5; // 最大连接
    private int maxWait = 30 * 1000; // 配置获取连接等待超时的时间
    private int timeBetweenEvictionRunsMillis = 60 * 1000; // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private int minEvictableIdleTimeMillis = 10 * 60 * 1000; // 配置一个连接在池中最小生存的时间，单位是毫秒
    private String validationQuery; // SELECT 'x'
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements; // 打开PSCache，并且指定每个连接上PSCache的大小
    private int maxPoolPreparedStatementPerConnectionSize;
    private String filters; //stat,wall # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    private Properties connectionProperties; // 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private boolean useGlobalDataSourceStat; // 合并多个DruidDataSource的监控数据

    private Map<String, String> druidServletSettings;
    private Map<String, String> druidFilterSettings;
}

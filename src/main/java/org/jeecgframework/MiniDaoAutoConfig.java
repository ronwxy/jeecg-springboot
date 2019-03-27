package org.jeecgframework;

import org.jeecgframework.minidao.factory.MiniDaoBeanScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

@Configuration
public class MiniDaoAutoConfig {

    @Bean
    public MiniDaoBeanScannerConfigurer miniDaoBeanScannerConfigurer(){
        MiniDaoBeanScannerConfigurer daoBeanScannerConfigurer = new MiniDaoBeanScannerConfigurer();
        daoBeanScannerConfigurer.setKeyType("lower");
        daoBeanScannerConfigurer.setFormatSql(false);
        daoBeanScannerConfigurer.setShowSql(false);
        daoBeanScannerConfigurer.setDbType("mysql");
        daoBeanScannerConfigurer.setBasePackage("org.jeecgframework.web,com.jeecg,com.cnbot");
        daoBeanScannerConfigurer.setAnnotation(Repository.class);
        return daoBeanScannerConfigurer;
    }
}

package edu.uta.cse5381.assignment3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

    @Bean("fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(5);
    }

    /*@Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.getJpaPropertyMap().put("hibernate.generate_statistics", "true");
        return jpaVendorAdapter;
    }*/

}

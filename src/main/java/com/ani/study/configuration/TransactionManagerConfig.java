/*
 * You are strictly prohibited to copy, disclose, distribute, modify, or use this program in part
 * or as a whole without the prior written consent of Starbucks Coffee Company.
 * Starbucks Coffee Company owns the intellectual property rights in and to this program.
 *
 * (Copyright ⓒ2022 Starbucks Coffee Company. All Rights Reserved | Confidential)
 */

package com.ani.study.configuration;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Lazy
@EnableJpaRepositories(
    entityManagerFactoryRef = "batchEntityManagerFactory",
    transactionManagerRef = "batchTransactionManager",
    basePackages = "com.ani.study.domain"
)
public class TransactionManagerConfig {

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Value("${spring.jpa.properties.hibernate.storage-engine}")
    private String storageEngine;

    @Value("${spring.jpa.properties.hibernate.show-sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.hbm2ddl-auto}")
    private String hbm2ddlAuto;

    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }

    @Bean(name = "batchEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(@Qualifier("batchDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(hibernateJpaProperties());
        em.setPackagesToScan("com.ani.study.domain.entity");  // JPA 엔티티 위치 설정
        return em;
    }

    private Map<String, ?> hibernateJpaProperties() {
        HashMap<String, String> properties = new HashMap<>();

        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.dialect.storage_engine", storageEngine);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        return properties;
    }

    @Bean(name = "batchTransactionManager")
    @Primary
    public PlatformTransactionManager batchTransactionManager(@Qualifier("batchEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

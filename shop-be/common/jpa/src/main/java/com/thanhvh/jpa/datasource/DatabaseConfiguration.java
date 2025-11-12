package com.thanhvh.jpa.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * DatabaseConfiguration
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({HibernateProperties.class, JpaProperties.class})
public class DatabaseConfiguration implements BeanFactoryAware {
    private final HibernateProperties hibernateProperties;
    private final JpaProperties jpaProperties;

    private ConfigurableListableBeanFactory beanFactory;

    /**
     * @param hibernateProperties {@link HibernateProperties}
     * @param jpaProperties       {@link JpaProperties}
     */
    public DatabaseConfiguration(HibernateProperties hibernateProperties, JpaProperties jpaProperties) {
        this.hibernateProperties = hibernateProperties;
        this.jpaProperties = jpaProperties;
    }

    /**
     * Create hikari datasource
     *
     * @param dataSourceProperties datasource properties
     * @param hikariConfig         hikari config
     * @return hikari datasource
     */
    protected static HikariDataSource createHikariDataSource(DataSourceProperties dataSourceProperties, HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(dataSourceProperties.determineUrl());
        hikariConfig.setUsername(dataSourceProperties.determineUsername());
        hikariConfig.setPassword(dataSourceProperties.determinePassword());
        hikariConfig.setDriverClassName(dataSourceProperties.determineDriverClassName());
        hikariConfig.setDataSourceJNDI(dataSourceProperties.getJndiName());
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return dataSource;
    }

    /**
     * Replica datasource Properties
     *
     * @return replicaDataSourceProperties
     */
    @ConfigurationProperties("common.datasource.replica")
    @Bean("replicaDataSourceProperties")
    public DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Master datasource Properties
     *
     * @return masterDataSourceProperties
     */
    @ConfigurationProperties("common.datasource.master")
    @Bean("masterDataSourceProperties")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * @return master hikari config
     */
    @ConfigurationProperties("common.datasource.master.hikari")
    @Bean("masterHikariConfig")
    public HikariConfig masterHikariConfig() {
        return new HikariConfig();
    }

    /**
     * @return replica hikari config
     */
    @ConfigurationProperties("common.datasource.replica.hikari")
    @Bean("replicaHikariConfig")
    public HikariConfig replicaHikariConfig() {
        return new HikariConfig();
    }

    /**
     * @param masterDataSource  masterDataSource
     * @param replicaDataSource replicaDataSource
     * @return routingDataSource
     */
    @Bean
    @Primary
    public DataSource routingDataSource(
            HikariDataSource masterDataSource,
            HikariDataSource replicaDataSource
    ) {
        return new MasterReplicaRoutingDataSource(
                masterDataSource,
                replicaDataSource
        );
    }

    /**
     * masterDataSource
     *
     * @param masterDataSourceProperties DataSourceProperties
     * @param masterHikariConfig         hikari config
     * @return masterDataSource
     */
    @Bean("masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public HikariDataSource masterDataSource(DataSourceProperties masterDataSourceProperties, HikariConfig masterHikariConfig) {
        return createHikariDataSource(masterDataSourceProperties, masterHikariConfig);
    }

    /**
     * ReplicaDataSource
     *
     * @param replicaDataSourceProperties DataSourceProperties
     * @param replicaHikariConfig         hikari config
     * @return replicaDataSource
     */

    @Bean("replicaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    public HikariDataSource replicaDataSource(DataSourceProperties replicaDataSourceProperties, HikariConfig replicaHikariConfig) {
        return createHikariDataSource(replicaDataSourceProperties, replicaHikariConfig);
    }

    /**
     * LocalContainerEntityManagerFactoryBean
     *
     * @param builder           {@link EntityManagerFactoryBuilder}
     * @param routingDataSource {@link #routingDataSource(HikariDataSource, HikariDataSource)}
     * @return {@link LocalContainerEntityManagerFactoryBean}
     */

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource routingDataSource
    ) {
        return builder
                .dataSource(routingDataSource)
                .packages(getPackagesToScan())
                .build();
    }

    /**
     * Transaction manager
     *
     * @param emf {@link  EntityManagerFactory}
     * @return {@link ReplicaAwareTransactionManager}
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new ReplicaAwareTransactionManager(new JpaTransactionManager(emf));
    }

    /**
     * @param jpaVendorAdapter       {@link JpaVendorAdapter}
     * @param persistenceUnitManager {@link PersistenceUnitManager}
     * @return EntityManagerFactoryBuilder
     */
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            JpaVendorAdapter jpaVendorAdapter,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager
    ) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, getVendorProperties(), persistenceUnitManager.getIfAvailable());
    }

    /**
     * Vendor properties
     *
     * @return vendor properties
     */
    protected Map<String, Object> getVendorProperties() {
        Supplier<String> defaultDdlMode = () -> "create-drop";
        return new LinkedHashMap<>(this.hibernateProperties
                .determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings()
                        .ddlAuto(defaultDdlMode)));
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    /**
     * Packages to scan
     *
     * @return list packages
     */
    protected String[] getPackagesToScan() {
        List<String> packages = EntityScanPackages.get(this.beanFactory).getPackageNames();
        if (packages.isEmpty() && AutoConfigurationPackages.has(this.beanFactory)) {
            packages = AutoConfigurationPackages.get(this.beanFactory);
        }
        return StringUtils.toStringArray(packages);
    }

    /**
     * Jpa vendor adaptor
     *
     * @return vendor adaptor
     */

    @Bean
    @ConditionalOnMissingBean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();
        adapter.setShowSql(this.jpaProperties.isShowSql());
        if (this.jpaProperties.getDatabase() != null) {
            adapter.setDatabase(this.jpaProperties.getDatabase());
        }
        if (this.jpaProperties.getDatabasePlatform() != null) {
            adapter.setDatabasePlatform(this.jpaProperties.getDatabasePlatform());
        }
        adapter.setGenerateDdl(this.jpaProperties.isGenerateDdl());
        return adapter;
    }

    /**
     * Create jpa vendor adaptor
     *
     * @return {@link AbstractJpaVendorAdapter}
     */
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
}

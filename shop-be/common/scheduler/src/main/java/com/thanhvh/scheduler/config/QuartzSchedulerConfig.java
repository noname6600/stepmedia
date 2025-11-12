package com.thanhvh.scheduler.config;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.factory.DefaultSchedulerFactory;
import com.thanhvh.scheduler.factory.ISchedulerFactory;
import com.thanhvh.scheduler.job.DefaultAutoJobHandler;
import com.thanhvh.scheduler.job.IAutoJobHandler;
import com.thanhvh.scheduler.listener.JobsListener;
import com.thanhvh.scheduler.listener.TriggersListener;
import com.thanhvh.scheduler.service.IJobService;
import com.thanhvh.scheduler.service.JobService;
import com.zaxxer.hikari.HikariDataSource;
import org.quartz.JobListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * QuartzSchedulerConfig
 */
@EnableConfigurationProperties(JobConfig.class)
public class QuartzSchedulerConfig {

    private static DataSource determineDataSource(DataSource dataSource, String username, String password) {
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            return DataSourceBuilder.derivedFrom(dataSource).username(username).password(password)
                    .type(SimpleDriverDataSource.class).build();
        }
        return dataSource;
    }

    /**
     * jobsListener
     *
     * @return JobsListener
     */
    @Bean
    @ConditionalOnMissingBean
    public JobListener jobsListener() {
        return new JobsListener();
    }

    /**
     * triggerListener
     *
     * @return TriggerListener
     */
    @Bean
    @ConditionalOnMissingBean
    public TriggerListener triggersListener() {
        return new TriggersListener();
    }

    /**
     * jobFactory
     *
     * @param applicationContextProvider applicationContextProvider
     * @return JobFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public JobFactory jobFactory(ApplicationContext applicationContextProvider) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContextProvider);
        return jobFactory;
    }

    /**
     * jobService
     *
     * @param applicationContext   applicationContext
     * @param schedulerFactoryBean schedulerFactoryBean
     * @return IJobService
     */
    @Bean
    @ConditionalOnMissingBean
    public IJobService jobService(ApplicationContext applicationContext,
                                  SchedulerFactoryBean schedulerFactoryBean) {
        return new JobService(schedulerFactoryBean, applicationContext);
    }

    /**
     * schedulerFactoryBean
     *
     * @param quartzProperties quartzProperties
     * @param dataSource       dataSource
     * @param triggersListener triggersListener
     * @param jobsListener     jobsListener
     * @param jobFactory       jobFactory
     * @return SchedulerFactoryBean
     */
    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties,
                                                     @Qualifier("masterDataSource") HikariDataSource dataSource,
                                                     TriggerListener triggersListener,
                                                     JobListener jobsListener,
                                                     JobFactory jobFactory
    ) {
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(properties);
        factory.setGlobalTriggerListeners(triggersListener);
        factory.setGlobalJobListeners(jobsListener);
        factory.setJobFactory(jobFactory);
        return factory;
    }

    /**
     * dataSourceScriptDatabaseInitializer
     *
     * @param dataSource DataSource
     * @param properties SqlInitializationProperties
     * @return SqlDataSourceScriptDatabaseInitializer
     */
    @Bean
    @ConditionalOnMissingBean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(@Qualifier("masterDataSource") DataSource dataSource,
                                                                               SqlInitializationProperties properties) {
        return new SqlDataSourceScriptDatabaseInitializer(
                determineDataSource(dataSource, properties.getUsername(), properties.getPassword()), properties);
    }

    @Bean
    @ConditionalOnMissingBean
    ISchedulerFactory iSchedulerFactory(IJobService iJobService) {
        return new DefaultSchedulerFactory(iJobService);
    }

    @Bean
    @ConditionalOnMissingBean
    IAutoJobHandler autoJobHandler(ApplicationContext applicationContext, IJobService iJobService) throws InvalidException {
        return new DefaultAutoJobHandler(applicationContext, iJobService);
    }
}

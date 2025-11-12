package com.thanhvh.scheduler.job;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.scheduler.config.JobConfig;
import com.thanhvh.scheduler.model.TimeTrigger;
import com.thanhvh.scheduler.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AutoJobHandler
 */
@Slf4j
public class DefaultAutoJobHandler implements IAutoJobHandler {

    private final Map<Class<? extends Job>, JobConfig.AutoJob> autoJobs = new HashMap<>();
    private final ApplicationContext applicationContext;
    private final IJobService iJobService;

    /**
     * @param applicationContext applicationContext
     * @param iJobService        iJobService
     * @throws InvalidException InvalidException
     */
    public DefaultAutoJobHandler(ApplicationContext applicationContext,
                                 IJobService iJobService) throws InvalidException {
        this.applicationContext = applicationContext;
        this.iJobService = iJobService;
        initAutoJobs();
        handleAutoJob();
    }

    private void initAutoJobs() {
        String[] names = applicationContext.getBeanNamesForType(IAutoJob.class);
        for (String i : names) {
            initIndividualAutoJobBean(i);
        }
    }

    private void initIndividualAutoJobBean(String beanName) {
        Class<? extends Job> autoJobClass = (Class<? extends Job>) applicationContext.getType(beanName);
        if (autoJobClass != null) {
            IAutoJob iAutoJob = (IAutoJob) applicationContext.getBean(beanName);
            this.autoJobs.put(autoJobClass, iAutoJob.getJobConfig());
        } else {
            log.error("Job bean not found. JobBean: {}", beanName);
        }
    }

    /**
     * handleAutoJob
     *
     * @throws InvalidException InvalidException
     */
    @Async
    public void handleAutoJob() throws InvalidException {
        for (Map.Entry<Class<? extends Job>, JobConfig.AutoJob> job : this.autoJobs.entrySet()) {
            handle(job.getKey(), job.getValue());
        }
    }

    private void handle(Class<? extends Job> qualifyClass, JobConfig.AutoJob autoJob) throws InvalidException {
        if (autoJob instanceof JobConfig.CronAutoJob) {
            if (iJobService.isJobWithNamePresent(autoJob.getJobName(), autoJob.getGroupKey())) {
                iJobService.deleteJob(autoJob.getJobName(), autoJob.getGroupKey());
            }
            iJobService.scheduleJob(
                    autoJob.getJobName(),
                    autoJob.getGroupKey(),
                    qualifyClass,
                    Set.of(
                            TimeTrigger
                                    .builder()
                                    .releaseTime(LocalDateTime.now())
                                    .cronExpression(autoJob.getCronExpression())
                                    .build()
                    ),
                    new JobDataMap(),
                    null
            );
        }
    }
}

package com.thanhvh.shopmanagement.modules.order.scheduler;

import com.thanhvh.scheduler.config.JobConfig;
import com.thanhvh.scheduler.job.BaseSchedulerJob;
import com.thanhvh.scheduler.job.IAutoJob;
import com.thanhvh.shopmanagement.modules.order.factory.IPersistOrderFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActiveOrderJob extends BaseSchedulerJob implements IAutoJob {
    private static final String ACTIVE_ORDERS = "active-orders";

    @Autowired
    private IPersistOrderFactory iPersistOrderFactory;

    @Autowired
    private JobConfig jobConfig;

    @Override
    protected void job(JobDetail jobDetail) {
        try {
            iPersistOrderFactory.activeOrders();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public JobConfig.AutoJob getJobConfig() {
        return jobConfig.getCronJobs().get(ACTIVE_ORDERS);
    }
}

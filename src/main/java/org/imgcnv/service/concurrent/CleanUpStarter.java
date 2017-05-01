package org.imgcnv.service.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.imgcnv.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class start Cleanup starter.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class CleanUpStarter {

    /**
     * JobMapConfig.
     */
    private JobMapWrapper jobMap;

    /**
     * ScheduledExecutorService executor for clean job.
     */
    private ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(1);

    /**
     *
     * @return jobMap JobMapConfig.
     */
    public final JobMapWrapper getJobMap() {
        return jobMap;
    }

    /**
     *
     * @param jobMapParam
     *            the JobMapConfig to set.
     */
    public final void setJobMap(final JobMapWrapper jobMapParam) {
        this.jobMap = jobMapParam;
    }

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @return logger Logger
     */
    public final Logger getLogger() {
        return logger;
    }

    /**
     *
     * @return ScheduledExecutorService.
     */
    public final ScheduledExecutorService getExecutor() {
        return executor;
    }

    /**
     *
     * @param executorParam
     *            the ScheduledExecutorService to set.
     */
    public final void setExecutor(final ScheduledExecutorService
            executorParam) {
        this.executor = executorParam;
    }

    /**
     * Constructor for this class.
     *
     * @param jobMapParam the
     *            JobMapConfig to set.
     */
    public CleanUpStarter(final JobMapWrapper jobMapParam) {

        Runnable clean = new Runnable() {

            @Override
            public void run() {
                jobMapParam.cleanJobs();
            }
        };
        logger.info("Start thread with JobCleaner");
        @SuppressWarnings("unused")
        Future<?> cleanService = executor.scheduleAtFixedRate(clean,
                Consts.CLEAN_PERIOD, Consts.CLEAN_PERIOD,
                TimeUnit.SECONDS);

    }

}

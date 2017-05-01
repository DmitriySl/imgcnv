package org.imgcnv.service.concurrent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.imgcnv.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class holds JobMap for store id of job.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class JobMapWrapper {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Map for store Job.
     */
    private Map<Long, List<JobFutureObject>> map =
            new ConcurrentHashMap<Long, List<JobFutureObject>>();

    /**
     * Getter for this field.
     *
     * @return map.
     */
    public final Map<Long, List<JobFutureObject>> getMap() {
        return map;
    }

    /**
     * Return status of job.
     *
     * @param id
     *            job id
     * @return true when job is ready.
     */
    public final boolean isReadyJob(final Long id) {
        List<JobFutureObject> tasks;
        tasks = map.get(id);
        if (tasks == null) {
            return false;
        }

        for (JobFutureObject item : tasks) {
            if (!item.getFuture().isDone()) {
                return false;
            }

            List<Future<Boolean>> list = item.getFutureImages();
            if (list.size() == 0) {
                return false;
            } else {
                for (Future<Boolean> child : list) {
                    if (!child.isDone()) {
                        return false;
                    }
                }
            }
        }

        return true;

    }

    /**
     * Method for clean old records in jobMap.
     */
    public final void cleanJobs() {
        long sizeBefore = getMap().size();
        if (sizeBefore == 0) {
            return;
        }

        //traditional
        /*
        Iterator<Long> it = getMap().keySet().iterator();
        while (it.hasNext()) {
            Long index = it.next();
            List<JobFutureObject> object = getMap().get(index);
            long seconds = 0;
            if (object.get(0) != null) {
                seconds = object.get(0).getDuration();
                //logger.info("CleanUp Progress seconds: {}", seconds);
            }
            if (seconds > Consts.CLEAN_PERIOD && isReadyJob(index)) {
                it.remove();
            }
        }
        */

        //java8
        getMap().entrySet().removeIf(entry -> entry.getValue().get(0) != null
                && entry.getValue().get(0).getDuration() > Consts.CLEAN_PERIOD
                && isReadyJob(entry.getKey()));

        long sizeAfter = getMap().size();
        logger.info("CleanUp Result before: {}, after: {} ", sizeBefore,
                sizeAfter);
    }
}

package org.imgcnv.service.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.imgcnv.utils.Consts;

/**
 * Class hold blockingQueue.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class QueueWrapper {

    /**
     * blockingQueue which link producer and consumer.
     */
    private BlockingQueue<ImageObject> blockingQueue =
            new LinkedBlockingQueue<ImageObject>(
            Consts.QUEUE_CAPACITY);

    /**
     *
     * @return Queue<ImageObject>.
     */
    public final BlockingQueue<ImageObject> getBlockingQueue() {
        return blockingQueue;
    }



}

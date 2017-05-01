package org.imgcnv.service.concurrent;

import java.util.List;

/**
 * Interface for Producer.
 * @author Dmitry_Slepchenkov
 * @param <T>
 *
 */
public interface Producer<T> {

    /**
     * Creates tasks for download files from url list.
     * @param <T>
     *
     * @param resource List<T>
     *            resource with urls.
     * @return long job id.
     */
    long addToProducer(final List<T> resource);

}

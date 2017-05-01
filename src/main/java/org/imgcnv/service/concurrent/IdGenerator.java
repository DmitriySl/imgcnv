package org.imgcnv.service.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class holds generator for unique index.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class IdGenerator {

    /**
     * Id for generator for job index.
     */
    private AtomicLong id = new AtomicLong(1L);

    /**
     * Id for generator with step.
     *
     * @param granularity
     *            step value, if differ when 1.
     * @return next index for generator.
     */

    public final long getNextId(final int granularity) {
        return id.getAndAdd(granularity);
    }

    /**
     * Id for generator.
     *
     * @return next index for generator.
     */

    public final long getNextId() {
        return id.getAndAdd(1L);
    }
}

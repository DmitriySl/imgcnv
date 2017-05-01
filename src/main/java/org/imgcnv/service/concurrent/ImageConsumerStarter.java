package org.imgcnv.service.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class start imageConsumer as Thread.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageConsumerStarter {

    /**
     * ImageConsumer as Runneble.
     */
    private ImageConsumer imageConsumer;

    /**
     *
     * @return ImageConsumer getter.
     */
    public final ImageConsumer getImageConsumer() {
        return imageConsumer;
    }

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor for this class.
     *
     * @param imageConsumerParam
     *            ImageConsumer to set.
     */
    public ImageConsumerStarter(final ImageConsumer imageConsumerParam) {
        Thread thread = new Thread(imageConsumerParam);
        thread.start();
        logger.info("Start thread in imageConsumerStarter");
    }

}

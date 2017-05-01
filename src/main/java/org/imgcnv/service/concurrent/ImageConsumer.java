package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.imgcnv.exception.ApplicationException;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageService;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageServiceScalrImpl;
import org.imgcnv.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer which consume downloaded images.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageConsumer implements Runnable {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * List with thumbails params.
     */
    private final List<Integer> thumbails = Arrays.asList(
            Consts.SIZE_THUMB_1,
            Consts.SIZE_THUMB_2,
            Consts.SIZE_THUMB_3);

    /**
     * JobMapWrapper jobMap for storage job information.
     */
    private JobMapWrapper jobMap;

    /**
     * QueueWrapper blockQueue for storage task.
     */
    private QueueWrapper itemQueue;

    /**
     * ExecutorService for execute tasks.
     */
    private ExecutorService executorService;

    /**
     * Resize service for image convert.
     */
    private ResizeBufferedImageService resizeService;


    /**
     * Constructor for this class, using Builder.
     *
     * @param builder
     *            to set to.
     */
    public ImageConsumer(final Builder builder) {
        itemQueue = builder.itemQueue;
        jobMap = builder.jobMap;
        executorService = builder.executorService;
        resizeService = builder.resizeService;
    }

    /**
     * Class for builder constructor.
     *
     * @author Dmitry_Slepchenkov
     *
     */
    public static class Builder {
        // Required params
        /**
         * JobMapConfig.
         */
        private JobMapWrapper jobMap;

        /**
         * QueueConfig blockQueue for storage task.
         */
        private QueueWrapper itemQueue;

        // Optional params
        /**
         * ExecutorService for execute tasks.
         */
        private ExecutorService executorService = Executors
                .newFixedThreadPool(Consts.RESIZE_THREADS);

        /**
         * Resize service for image convert.
         */
        private ResizeBufferedImageService resizeService =
                new ResizeBufferedImageServiceScalrImpl();

        /**
         * Builder constructor.
         *
         * @param jobMapParam
         *            as JobMapWrapper to set.
         * @param itemQueueParam
         *            as QueueWrapper to set.
         */
        public Builder(final JobMapWrapper jobMapParam,
                final QueueWrapper itemQueueParam) {
            this.jobMap = jobMapParam;
            this.itemQueue = itemQueueParam;
        }

        /**
         * Used for set resizeService.
         *
         * @param resizeServiceParam
         *            as ResizeBufferedImageService for image resize.
         * @return Builder for constructor.
         */
        public final Builder resizeService(final ResizeBufferedImageService
                resizeServiceParam) {
            resizeService = resizeServiceParam;
            return this;
        }

        /**
         * Used for set executorService.
         *
         * @param executorServiceParam
         *            as ExecutorService for callable submit.
         * @return Builder for constructor.
         */
        public final Builder executorService(final ExecutorService
                executorServiceParam) {
            executorService = executorServiceParam;
            return this;
        }

        /**
         * Used for build constructor in builder pattern.
         *
         * @return ConvertImageCallable object.
         */
        public final ImageConsumer build() {
            return new ImageConsumer(this);
        }
    }

    /**
     * method, which prepare image resizing.
     */
    public final void consume() {
        ImageObject imageObject = null;
        BufferedImage image = null;
        try {
            imageObject = itemQueue.getBlockingQueue().take();
            if (imageObject != null) {
                image = imageObject.getImage();

                logger.info("Take image for id {} url {}", imageObject.getId(),
                        imageObject.getResource().getUrl());
            }
        } catch (InterruptedException e) {
            throw new ApplicationException(e);
        }

        if (imageObject == null) {
            return;
        }

        BufferedImage imageConst = image;
        Long id = imageObject.getId();
        String url = imageObject.getResource().getUrl();
        List<JobFutureObject> tasks = jobMap.getMap().get(id);

        /*
        for (JobFutureObject fo : tasks) {
            if (fo.getResource().getUrl().equals(url)) {
                List<Future<Boolean>> futureImages =
                        fo.getFutureImages();

                for (Integer thumbail : thumbails) {

                    ConvertImageCallable callable =
                            new ConvertImageCallable.Builder(image, url)
                            .resolution(thumbail)
                            .jobId(id)
                            .resizeService(resizeService)
                            .build();
                    futureImages.add(executorService.submit(callable));
                }
            }
        }*/


        //j8
        tasks.stream().filter(fo -> fo.getResource().getUrl().equals(url))
        .forEach(fo -> {
            thumbails.stream().forEach(thumbail -> {
                ConvertImageCallable callable =
                        new ConvertImageCallable.Builder(imageConst, url)
                        .resolution(thumbail)
                        .jobId(id)
                        .resizeService(resizeService)
                        .build();
                fo.getFutureImages().add(executorService.submit(callable));
            });
        });

    }

    @Override
    /**
     * Runnable interface implement.
     */
    public final void run() {
        while (true) {
            consume();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new ApplicationException(e);
            }
        }

    }

}

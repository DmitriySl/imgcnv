package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.exception.ApplicationException;
import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.service.concurrent.download.DownloadServiceImpl;
import org.imgcnv.service.concurrent.read.ReadService;
import org.imgcnv.service.concurrent.read.ReadServiceImpl;
import org.imgcnv.utils.Consts;
import org.imgcnv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class produce task for download file.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ImageProducer implements Producer<ImageResource>, ImageCallback {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Generator for unique index.
     */
    private IdGenerator idGenerator;

    /**
     * Queue for link producer with consumer.
     */
    private QueueWrapper itemQueue;

    /**
     * Map for store jobs.
     */
    private JobMapWrapper jobMap;

    /**
     * ExecutorService to start tasks for download.
     */
    private ExecutorService executorService;

    /**
     * Download service.
     */
    private DownloadService downloadService;

    /**
     * Read image service.
     */
    private ReadService readService;

    /**
     * Constructor for this class, using Builder.
     *
     * @param builder
     *            to set to.
     */
    public ImageProducer(final Builder builder) {
        idGenerator = builder.idGenerator;
        jobMap = builder.jobMap;
        itemQueue = builder.itemQueue;
        executorService = builder.executorService;
        downloadService = builder.downloadService;
        readService = builder.readService;
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
         * IdGenerator idGenerator for unique id.
         */
        private IdGenerator idGenerator;

        /**
         * JobMapWrapper jobMap for storage job information.
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
        private ExecutorService executorService =
                Executors.newFixedThreadPool(Consts.DOWNLOAD_THREADS);

        /**
         * Download service.
         */
        private DownloadService downloadService = new DownloadServiceImpl();

        /**
         * Read image service.
         */
        private ReadService readService = new ReadServiceImpl();

        /**
         * Builder constructor.
         *
         * @param jobMapParam
         *            as JobMapWrapper to set.
         * @param itemQueueParam
         *            as QueueWrapper to set.
         * @param idGeneratorParam
         *            as IdGenerator to set.
         */
        public Builder(final JobMapWrapper jobMapParam,
                final QueueWrapper itemQueueParam,
                final IdGenerator idGeneratorParam) {
            this.jobMap = jobMapParam;
            this.itemQueue = itemQueueParam;
            this.idGenerator = idGeneratorParam;
        }

        /**
         * Used for set downloadService.
         *
         * @param downloadServiceParam
         *            as DownloadService for image download.
         * @return Builder for constructor.
         */
        public final Builder downloadService(final DownloadService
                downloadServiceParam) {
            downloadService = downloadServiceParam;
            return this;
        }

        /**
         * Used for set readService.
         * @param readServiceParam
         *           as ReadService for image file read.
         * @return Builder for constructor.
         */
        public final Builder readService(final ReadService
                readServiceParam) {
            readService = readServiceParam;
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
         * @return ImageProducer object.
         */
        public final ImageProducer build() {
            return new ImageProducer(this);
        }
    }

    /**
     * Creates tasks for download files from url list.
     *
     * @param resource
     *            List<ImageResource> resource with urls.
     * @return long job id.
     */
    @Override
    public final long addToProducer(final List<ImageResource> resource) {
        long id = idGenerator.getNextId();

        List<JobFutureObject> tasks =
                new CopyOnWriteArrayList<JobFutureObject>();

        /*
        for (ImageResource item : resource) {
            // image object
            ImageObject imageObject = new ImageObject.Builder(item)
                    .jobId(id)
                    .build();

            DownloadImageCallable callable =
                    new DownloadImageCallable.Builder(imageObject, this)
                    .downloadService(downloadService)
                    .build();

            // assamble futureObject for jobMap
            List<Future<Boolean>> futureImages =
                    new CopyOnWriteArrayList<Future<Boolean>>();

            //ZonedDateTime.now().toLocalDateTime();

            JobFutureObject futureObject = new JobFutureObject
                    .Builder(item)
                    .dateTime(LocalDateTime.now())
                    .futureImages(futureImages)
                    .future(executorService.submit(callable))
                    .build();
            tasks.add(futureObject);

            logger.info("Put item with id={}, date={} to futureObject.", id,
                    futureObject.getDateTime());
        }*/

        //j8
        resource.stream().forEach(item -> {
         // image object
            ImageObject imageObject = new ImageObject.Builder(item)
                    .jobId(id)
                    .build();

            DownloadImageCallable callable =
                    new DownloadImageCallable.Builder(imageObject, this)
                    .downloadService(downloadService)
                    .build();

            // assamble futureObject for jobMap
            List<Future<Boolean>> futureImages =
                    new CopyOnWriteArrayList<Future<Boolean>>();

            //ZonedDateTime.now().toLocalDateTime();

            JobFutureObject futureObject = new JobFutureObject
                    .Builder(item)
                    .dateTime(LocalDateTime.now())
                    .futureImages(futureImages)
                    .future(executorService.submit(callable))
                    .build();
            tasks.add(futureObject);

            logger.info("Put item with id={}, date={} to futureObject.", id,
                    futureObject.getDateTime());
        });
        //
        jobMap.getMap().put(id, tasks);
        logger.info("Put tasks id={} to map", id);

        return id;
    }

    /**
     * Callback function.
     *
     * @param imageObject
     *            ImageObject
     */
    @Override
    public final void callFinished(final ImageObject imageObject) {

        //Replace Image, disadvantage is one thread only.
        Path path = Utils.getImagePath(imageObject.getId(),
                imageObject.getResource().getUrl());
        BufferedImage image = readService.read(path);
        //Create new object with BufferedImage
        ImageObject modifiedImageObject =
                new ImageObject.Builder(imageObject.getResource())
                .jobId(imageObject.getId())
                .image(image)
                .build();

        try {
            itemQueue.getBlockingQueue().put(modifiedImageObject);
            logger.info("Put imageObject id={} url={} into Queue",
                    modifiedImageObject.getId(),
                    modifiedImageObject.getResource().getUrl());

        } catch (InterruptedException e) {
            throw new ApplicationException(e);
        }

    }

}

package org.imgcnv.spring;

import java.util.concurrent.Executors;

import org.imgcnv.service.concurrent.CleanUpStarter;
import org.imgcnv.service.concurrent.IdGenerator;
import org.imgcnv.service.concurrent.ImageConsumer;
import org.imgcnv.service.concurrent.ImageConsumerStarter;
import org.imgcnv.service.concurrent.ImageProducer;
import org.imgcnv.service.concurrent.JobMapWrapper;
import org.imgcnv.service.concurrent.QueueWrapper;
import org.imgcnv.service.concurrent.download.DownloadService;
import org.imgcnv.service.concurrent.download.DownloadServiceImpl;
import org.imgcnv.service.concurrent.read.ReadService;
import org.imgcnv.service.concurrent.read.ReadServiceImpl;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageService;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageServiceScalrImpl;
import org.imgcnv.utils.Consts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Class for configured Spring Bean for application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class Beans {
    /**
     *
     * @return LoggingAspect.
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    /**
     *
     * @return QueueConfig.
     */
    @Bean
    public QueueWrapper queueWrapper() {
        return new QueueWrapper();
    }

    /**
     *
     * @return IdGenerator.
     */
    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    /**
     *
     * @return ResizeBufferedImageService.
     */
    @Bean
    public ResizeBufferedImageService resizeService() {
        return new ResizeBufferedImageServiceScalrImpl();
                //ResizeBufferedImageServiceTwelveImpl();
                //ResizeBufferedImageServiceThumbnailatorImpl();
    }

    /**
     *
     * @return DownloadService.
     */
    @Bean
    public DownloadService downloadService() {
        return new DownloadServiceImpl();
    }

    /**
    *
    * @return ReadService.
    */
   @Bean
   public ReadService readService() {
       return new ReadServiceImpl();
   }
    /**
     *
     * @return JobMapConfig.
     */
    @Bean
    public JobMapWrapper jobMapWrapper() {
        return new JobMapWrapper();
    }

    /**
     *
     * @return ImageProducer.
     */
    @Bean
    public ImageProducer imageProducer() {
        ImageProducer bean = new ImageProducer
                .Builder(jobMapWrapper(),  queueWrapper(), idGenerator())
                .downloadService(downloadService())
                .readService(readService())
                .build();
        return bean;
    }

    /**
     *
     * @return ImageConsumer.
     */
    @Bean
    public ImageConsumer imageConsumer() {
        ImageConsumer bean = new ImageConsumer
                .Builder(jobMapWrapper(), queueWrapper())
                .resizeService(resizeService())
                .executorService(Executors
                .newFixedThreadPool(Consts.RESIZE_THREADS))
                .build();
        return bean;
    }

    /**
     *
     * @return ImageConsumerStarter.
     */
    @Bean
    public ImageConsumerStarter imageConsumerStarter() {
        return new ImageConsumerStarter(imageConsumer());
    }

    /**
    *
    * @return CleanUpStarter.
    */
   @Bean
    public CleanUpStarter cleanUpStarter() {
        return new CleanUpStarter(jobMapWrapper());
    }
}

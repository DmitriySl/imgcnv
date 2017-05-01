package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.imgcnv.service.concurrent.resize.ResizeBufferedImageService;
import org.imgcnv.service.concurrent.resize.ResizeBufferedImageServiceScalrImpl;
import org.imgcnv.utils.Consts;
import org.imgcnv.utils.Utils;

/**
 * Class implements callable interface for task to convert image.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class ConvertImageCallable implements Callable<Boolean> {

    /**
     * BufferedImage.
     */
    private final BufferedImage image;

    /**
     * url, associated with image file.
     */
    private final String url;

    /**
     * Image resolution in pixels.
     */
    private final int resolution;

    /**
     * Resize service for image convert.
     */
    private final ResizeBufferedImageService resizeService;

    /**
     * index which define job id (number) and folder to store images.
     */
    private final long jobId;

    /**
     * Constructor for this class, using Builder.
     *
     * @param builder
     *            to set to.
     */
    private ConvertImageCallable(final Builder builder) {
        image = builder.image;
        url = builder.url;
        resizeService = builder.resizeService;
        resolution = builder.resolution;
        jobId = builder.jobId;
    }

    /**
     * Class for builder constructor.
     *
     * @author Dmitry_Slepchenkov
     *
     */
    public static class Builder {
        //Required params
        /**
         * BufferedImage.
         */
        private final BufferedImage image;

        /**
         * url, associated with image file.
         */
        private final String url;

        //Optional params
        /**
         * Image resolution in pixels.
         */
        private int resolution = Consts.SIZE_THUMB_1;

        /**
         * Resize service for image convert.
         */
        private ResizeBufferedImageService resizeService =
                new ResizeBufferedImageServiceScalrImpl();

        /**
         * index which define job id (number) and folder to store images.
         */
        private long jobId = 0L;

        /**
         * Builder constructor.
         *
         * @param imageParam
         *            BufferedImage for constructor
         * @param urlParam
         *            url for constructor
         */
        public Builder(final BufferedImage imageParam, final String urlParam) {
            this.image = imageParam;
            this.url = urlParam;
        }

        /**
         * Used for set resolution.
         *
         * @param resolutionParam
         *            int resolution value
         * @return Builder for constructor.
         */
        public final Builder resolution(final int resolutionParam) {
            resolution = resolutionParam;
            return this;
        }

        /**
         * Used for set jobId.
         *
         * @param jobIdParam
         *            long for jobId
         * @return Builder for constructor.
         */
        public final Builder jobId(final long jobIdParam) {
            jobId = jobIdParam;
            return this;
        }

        /**
         * Used for set resizeService.
         *
         * @param resizeServiceParam
         *                  as ResizeBufferedImageService for image resize.
         * @return Builder for constructor.
         */
        public final Builder resizeService(final ResizeBufferedImageService
                resizeServiceParam) {
            resizeService = resizeServiceParam;
            return this;
        }

        /**
         * Used for build constructor in builder pattern.
         *
         * @return ConvertImageCallable object.
         */
        public final ConvertImageCallable build() {
            return new ConvertImageCallable(this);
        }
    }

    /**
     * @return result of resize image operation. If operation was successful
     *         result is true.
     * @throws Exception
     *             if unable to compute a result
     */
    @Override
    public Boolean call() throws Exception {

        String copyPath = Utils.getCopyPath();
        Utils.createDir(copyPath);

        String targetFolderLink = copyPath + File.separator
                + jobId;
        Utils.createDir(targetFolderLink);

        String convPathLink = targetFolderLink + File.separator + "thmb";
        Utils.createDir(convPathLink);

        Path convPath = new File(convPathLink).toPath();

        long resizeResult = resizeService.createResizedCopy(resolution,
                resolution, image, url, convPath);

        return resizeResult > 0;
    }

}

package org.imgcnv.service.concurrent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import java.util.concurrent.Future;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.utils.Consts;

/**
 * Class for hold Futures objects.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class JobFutureObject {

    /**
     * ImageResource url wrapper.
     */
    private ImageResource resource;

    /**
     * Future<Boolean> holds image download result.
     */
    private Future<Boolean> future;

    /**
     * List Future<Boolean> holds images links for convert (resize).
     */
    private List<Future<Boolean>> futureImages;

    /**
     * Date hold date for clean up operations.
     */
    private LocalDateTime dateTime;

    /**
     *
     * @return ImageResource.
     */
    public ImageResource getResource() {
        return resource;
    }

    /**
     *
     * @return Future<Boolean>.
     */
    public Future<Boolean> getFuture() {
        return future;
    }

    /**
     *
     * @return List Future<Boolean>.
     */
    public List<Future<Boolean>> getFutureImages() {
        return futureImages;
    }

   /**
    *
    * @return date LocalDateTime for clean up.
    */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Used for cleanup.
     * @return seconds long in duration.
     */
    public long getDuration() {
        LocalDateTime now = LocalDateTime.now();
        long seconds = (now.toInstant(ZoneOffset.UTC).toEpochMilli()
                - dateTime.toInstant(ZoneOffset.UTC)
                .toEpochMilli()) / Consts.MILLIS_IN_SEC;
        return seconds;
    }

    /**
     * Class for Builder pattern (constructor for DownloadImageCallable).
     *
     * @author Dmitry_Slepchenkov
     *
     */
    public static class Builder {

        /**
         * ImageResource url wrapper.
         */
        private final ImageResource resource;

        /**
         * Future<Boolean> holds image download result.
         */
        private Future<Boolean> future;

        /**
         * List Future<Boolean> holds images links for convert (resize).
         */
        private List<Future<Boolean>> futureImages;

        /**
         * Date hold date for clean up operations.
         */
        private LocalDateTime dateTime = LocalDateTime.now();

        /**
         *  Builder constructor with params.
         * @param resourceParam as ImageResource to set.
         */
        public Builder(final ImageResource resourceParam) {
            resource = resourceParam;
        }

        /**
         * Used to set future  Future<Boolean>.
         * @param futureParam as Future<Boolean>.
         * @return Builder.
         */
        public final Builder future(final Future<Boolean> futureParam) {
            future = futureParam;
            return this;
        }

        /**
         * Used to set CopyOnWriteArrayList<Future<Boolean>>.
         * @param futureImagesArg as List<Future<Boolean>>.
         * @return Builder.
         */
        public final Builder futureImages(
                final List<Future<Boolean>> futureImagesArg) {
            futureImages = futureImagesArg;
            return this;
        }

        /**
         * Used to set LocalDateTime dateTime.
         * @param dateTimeParam as LocalDateTime.
         * @return Builder.
         */
        public final Builder dateTime(final  LocalDateTime dateTimeParam) {
            dateTime = dateTimeParam;
            return this;
        }

        /**
         * build method of Builder pattern.
         *
         * @return JobFutureObject.
         */
        public final JobFutureObject build() {
            return new JobFutureObject(this);
        }

    }

    /**
     * Constructor for this class, for builder.
     *
     * @param builder
     *            to set Builder
     */
    private JobFutureObject(final Builder builder) {
        this.resource = builder.resource;
        this.dateTime = builder.dateTime;
        this.future = builder.future;
        this.futureImages = builder.futureImages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (dateTime != null) {
            result += dateTime.hashCode();
        }
        if (future != null) {
            result += future.hashCode();
        }
        if (futureImages != null) {
            result += futureImages.hashCode();
        }
        if (resource != null) {
            result += resource.hashCode();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JobFutureObject other = (JobFutureObject) obj;
        if (dateTime == null) {
            if (other.dateTime != null) {
                return false;
            }
        } else if (!dateTime.equals(other.dateTime)) {
            return false;
        }
        if (future == null) {
            if (other.future != null) {
                return false;
            }
        } else if (!future.equals(other.future)) {
            return false;
        }
        if (futureImages == null) {
            if (other.futureImages != null) {
                return false;
            }
        } else if (!futureImages.equals(other.futureImages)) {
            return false;
        }
        if (resource == null) {
            if (other.resource != null) {
                return false;
            }
        } else if (!resource.equals(other.resource)) {
            return false;
        }
        return true;
    }

}

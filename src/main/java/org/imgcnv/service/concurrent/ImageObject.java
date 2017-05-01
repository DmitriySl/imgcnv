package org.imgcnv.service.concurrent;

import java.awt.image.BufferedImage;

import org.imgcnv.entity.ImageResource;

/**
 * Class store object for Queue.
 * @author Dmitry_Slepchenkov
 *
 */
public final class ImageObject {

    /**
     * id for store job number.
     */
    private long jobId;

    /**
     * ImageResource for store url.
     */
    private ImageResource resource;

    /**
     * BufferedImage for hold image.
     */
    private BufferedImage image;

    /**
     * @return id of job.
     */
    public long getId() {
        return jobId;
    }

    /**
     * @return ImageResource.
     */
    public ImageResource getResource() {
        return resource;
    }

    /**
     * BufferedImage for hold image.
     * @return image as BufferedImage
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Class for builder pattern implementation.
     * @author Dmitry_Slepchenkov
     *
     */
    public static class Builder {

        /**
         * ImageResource for store url.
         */
        private ImageResource resource;

        /**
         * id for store job number.
         */
        private long jobId = 0L;

        /**
         * BufferedImage for hold image.
         */
        private BufferedImage image;

        /**
         * Constructor for Builder class.
         * @param resourceParam ImageResource to set.
         */
        public Builder(final ImageResource resourceParam) {
            this.resource = resourceParam;
        }

        /**
         * Build jobId.
         * @param jobIdParam jobId to set.
         * @return Builder
         */
        public final Builder jobId(final long jobIdParam) {
            jobId = jobIdParam;
            return this;
        }

        /**
         * Build image.
         * @param imageParam as BufferedImage to set.
         * @return Builder
         */
        public final Builder image(final BufferedImage imageParam) {
            image = imageParam;
            return this;
        }

        /**
         * method for build object.
         * @return ImageObject
         */
        public final ImageObject build() {
            return new ImageObject(this);
        }
    }

    /**
     * Constructor for this class, use Builder.
     * @param builder Builder
     */
    private ImageObject(final Builder builder) {
        resource = builder.resource;
        image = builder.image;
        jobId = builder.jobId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int shift = 32;
        int result = 1;
        if (image != null) {
            result = prime * result + image.hashCode();
        }
        if (resource != null) {
            result = prime * result + resource.hashCode();
        }
        result = prime * result + (int) (jobId ^ (jobId >>> shift));
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
        ImageObject other = (ImageObject) obj;
        if (image == null) {
            if (other.image != null) {
                return false;
            }
        } else if (!image.equals(other.image)) {
            return false;
        }
        if (jobId != other.jobId) {
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

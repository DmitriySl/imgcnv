package org.imgcnv.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.imgcnv.utils.Consts;

/**
 * Class wrapper for url resource. Created by Dmitry_Slepchenkov on 2/1/2017.
 */
public class ImageResource implements Serializable {

    /**
     * UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * url on image file.
     */
    private String url;

    /**
     *
     * @return String url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Constructor from superclass.
     */
    public ImageResource() {
    }

    /**
     * Constructor with field.
     *
     * @param urlParam
     *            String url on image file.
     */
    public ImageResource(final String urlParam) {
        this.url = urlParam;
    }

    /**
     * Convert string with urls to Set<ImageResource>.
     *
     * @param params
     *            String with list of url with delimiters
     * @return Set<ImageResource>
     */
    public static Set<ImageResource> imageResourceSetFromString(
            final String params) {

        Set<ImageResource> set = Stream.of(params.split(Consts.DELIMITER))
                .map(ImageResource::new)
                .collect(Collectors.toSet());
        return set;
    }
    /*
   public static Set<ImageResource> imageResourceSetFromString(
            final String params) {
        Set<ImageResource> set = new HashSet<ImageResource>();
        String[] str = params.split(Consts.DELIMITER);
        for (String url : str) {
            set.add(new ImageResource(url));
        }
        return set;
    }*/
    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return new StringBuilder("ImageResource [url=")
                .append(getUrl())
                .append("]")
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (url != null) {
            result += url.hashCode();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImageResource other = (ImageResource) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    /**
     * Convert Collection to String.
     * @param collection Collection of elements
     * @return String representation of List
     */
    public static final String asString(final Collection<ImageResource>
    collection) {
        return collection.stream()
                .map(ImageResource::getUrl)
                .collect(Collectors.joining(",\n", "[", "]"));
    }
}

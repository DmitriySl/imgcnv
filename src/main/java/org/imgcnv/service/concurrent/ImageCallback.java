package org.imgcnv.service.concurrent;

/**
 * Interface for callback function.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public interface ImageCallback {
    /**
     * Callback function.
     *
     * @param imageObject
     *            the ImageObject.
     */
    void callFinished(ImageObject imageObject);
}

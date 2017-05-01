package org.imgcnv.service.concurrent.resize;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

/**
 * Service for image scaling.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public interface ResizeBufferedImageService {
    /**
     * Method create a scaling image.
     *
     * @param scaledWidth
     *            int The width of scaling image
     * @param scaledHeight
     *            int The height of scaling image
     * @param bufferedImage
     *            BufferedImage of input pic
     * @param destination
     *            Path Destination for result image
     * @param url
     *            url of image
     * @return long Result of operation. If operation is successful return size
     *         of file
     */
    long createResizedCopy(int scaledWidth, int scaledHeight,
            BufferedImage bufferedImage, String url,
            Path destination);

}

package org.imgcnv.service.concurrent.resize;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import org.imgcnv.utils.Utils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;


/**
 * Service for image scaling. Use org.imgscalr library for image conversion
 * Support sharp filter
 *
 * @author Dmitry_Slepchenkov
 */
public class ResizeBufferedImageServiceScalrImpl implements
     ResizeBufferedImageService {

    /**
     * {@inheritDoc}
     */
    @Override
    public final long createResizedCopy(final int scaledWidth,
            final int scaledHeight,
            final BufferedImage bufferedImage,
            final String url,
            final Path destination) {


        String fullFileName = Utils.getImageName(Utils.getFileName(url),
                destination, Integer.toString(scaledWidth) + "imgsr");

        long result = -1;
        if (bufferedImage == null) {
            return result;
        }


        BufferedImage modifiedImage = null;
        synchronized (bufferedImage) {
            modifiedImage = Scalr.resize(bufferedImage,
                Method.ULTRA_QUALITY, scaledWidth, scaledHeight,
                Utils.OP_SHARP_MIDDLE);
        }
        result = Utils.saveBufferedImage(modifiedImage, fullFileName);

        return result;
    }

}

package org.imgcnv.service.concurrent.read;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgcnv.exception.ApplicationException;

/**
 * Service implementation for read image from file.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public class ReadServiceImpl implements ReadService {

    /**
     * {@inheritDoc}
     */
    @Override
    public final BufferedImage read(final Path destination) {
        BufferedImage image = null;

        File file = new File(destination.toString());
        if (file.exists()) {
            synchronized (file) {
                try {
                    image = ImageIO.read(file);
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }

            }
        }

        return image;
    }
}

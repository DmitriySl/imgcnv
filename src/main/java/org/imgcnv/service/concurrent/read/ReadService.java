package org.imgcnv.service.concurrent.read;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

/**Service for download images from Internet.
*
* @author Dmitry_Slepchenkov
*
*/
public interface ReadService {

    /**Method for read image from file.
     *
     * @param destination Path for save image
     * @return BufferedImage value, must not be null.
     */
    BufferedImage read(Path destination);

}

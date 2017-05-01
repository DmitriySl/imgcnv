package org.imgcnv.service.concurrent.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.imgcnv.exception.ApplicationException;

import org.apache.commons.validator.routines.UrlValidator;

/**Service implementation for download images from Internet.
*
* @author Dmitry_Slepchenkov
*
*/
public class DownloadServiceImpl implements DownloadService {

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean download(
            final String url, final Path destination) {
        boolean result = false;
        UrlValidator urlValidator = new UrlValidator();
        URL uri = null;
        if (urlValidator.isValid(url)) {
            try {
                uri = new URL(url);
            } catch (MalformedURLException e) {
                throw new ApplicationException(e);
            }
        }

        if (uri != null) {
            try (InputStream in = uri.openStream()) {


                Files.copy(in, destination,
                        StandardCopyOption.REPLACE_EXISTING);

                result = true;

            } catch (IOException e) {
                throw new ApplicationException(e);
            }

        }

        return result;
    }
}

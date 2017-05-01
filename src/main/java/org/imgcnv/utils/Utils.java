package org.imgcnv.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.time.Duration;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgcnv.exception.ApplicationException;

/**
 * Utility class for application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class Utils {

    /**
     * Get path to web application directory.
     *
     * @return path to web application directory
     */
    public static String getPath() {

        String path = Utils.class.getClassLoader().getResource("").getPath();
        String fullPath;
        String reponsePath = "";
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
            String[] pathArr = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            reponsePath = new File(fullPath).getPath() + File.separatorChar;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reponsePath;
    }

    /**
     * Create directory from string with path.
     *
     * @param dirPath
     *            String Path to directory
     */
    public static void createDir(final String dirPath) {
        File dirPathFolder = new File(dirPath);
        if (!dirPathFolder.exists()) {
            dirPathFolder.mkdir();
        }

    }

    /**
     * Get file extension.
     *
     * @param fileName
     *            String Name of file
     * @return String File extension
     */
    public static String getFileExt(final String fileName) {
        String newFileExt = fileName.substring(fileName.lastIndexOf('.') + 1,
                fileName.length());
        return newFileExt;
    }

    /**
     * Construct file name, using index for path.
     *
     * @param fileName
     *            String Name of file
     * @param destination
     *            Path for destination
     * @param index
     *            used for creating additional folder
     * @return String full filename for image.
     */
    public static String getImageName(final String fileName,
            final Path destination, final String index) {
        String newFileName = fileName.substring(
                fileName.lastIndexOf(File.separator) + 1,
                fileName.lastIndexOf('.'))
                + index;
        String newFileExt = getFileExt(fileName);
        String fullFileName = destination.toAbsolutePath() + File.separator
                + newFileName + "." + newFileExt;
        return fullFileName;
    }

    /**
     * Return path to directory on web application with images.
     *
     * @return String path to directory on web application with images
     */
    public static String getCopyPath() {
        String path = getPath() + File.separator + "pic";
        Utils.createDir(path);
        return path;
    }

   /**
     * Return path to image on web application with download image.
     * @param id jobId
     * @param url url to image in Internet
     * @return Path path to directory on web application with images
     */
    public static Path getImagePath(final long id, final String url) {
        String copyPath = Utils.getCopyPath();
        String fileName = Utils.getFileName(url);
        String targetFolderLink = copyPath + File.separator
                + id;
        Utils.createDir(targetFolderLink);
        Path targetPath = new File(targetFolderLink + File.separator + fileName)
                .toPath();
        return targetPath;
    }

    /**
     * Return filename only from url.
     *
     * @param url
     *            String url with filename
     * @return String filename only
     */
    public static String getFileName(final String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    /**
     * A {@link ConvolveOp} using a very strong "sharp" kernel that acts
     * (softens the image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_HARD = new ConvolveOp(
            new Kernel(3, 3, new float[]
                    {-1.f, -1.f, -1.f, -1.f, 9.0f, -1.f, -1.f, -1.f, -1.f}),
            ConvolveOp.EDGE_NO_OP, null);

    /**
     * A {@link ConvolveOp} using a middle "sharp" kernel that acts (softens the
     * image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_MIDDLE = new ConvolveOp(
            new Kernel(3, 3, new float[]
                    {0.f, -1.f, 0.f, -1.f, 5.0f, -1.f, 0.f, -1.f, 0.f}),
            ConvolveOp.EDGE_NO_OP,
            null);

    /**
     * A {@link ConvolveOp} using a middle "sharp" kernel that acts (softens the
     * image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_PREMIDDLE = new ConvolveOp(
            new Kernel(5, 5, new float[]
                       {-.125f, -.125f, -.125f, -.125f, -.125f,
                        -.125f, .25f,  .25f, .25f, -.125f,
                        -.125f, .25f,  1f, .25f, -.125f,
                        -.125f, .25f,  .25f, .25f, -.125f,
                        -.125f, -.125f, -.125f, -.125f, -.125f}),
            ConvolveOp.EDGE_NO_OP,
            null);

     /**
     * A {@link ConvolveOp} using a light "sharp" kernel that acts (softens the
     * image a bit) when applied to an image.
     */
    public static final ConvolveOp OP_SHARP_LIGHT = new ConvolveOp(
            new Kernel(3, 3, new float[]
                    {0.f, -.125f, 0.f, -.125f, 1.5f, -.125f, 0.f, -.125f, 0.f}),
            ConvolveOp.EDGE_NO_OP,
            null);

    /**
     * Sharper example filter.
     *
     * @param originalPic
     *            input BufferedImage
     * @param convolve
     *            input ConvolveOp
     * @return BufferedImage
     *            Pic with sharp filter
     */
    public static BufferedImage sharper(final BufferedImage originalPic,
            final ConvolveOp convolve) {

        BufferedImage newPic = new BufferedImage(originalPic.getWidth(),
                originalPic.getHeight(), originalPic.getType());

        convolve.filter(originalPic, newPic);

        return newPic;
    }

    /**
     * Save BufferedImage into file.
     *
     * @param image
     *            BufferedImage Input BufferedImage
     * @param fileName
     *            String Filename of file to save.
     * @return If operation successful value more 0.
     */
    public static int saveBufferedImage(final BufferedImage image,
            final String fileName) {

        int result = -1;
        String fileExt = Utils.getFileExt(fileName);
        if ("JPEG".equals(fileExt.toUpperCase())
                || "JPG".equals(fileExt.toUpperCase())) {
            File outPutImage = new File(fileName);
            try (ImageOutputStream ios =
                    ImageIO.createImageOutputStream(outPutImage)) {
                ImageWriter writer =
                        ImageIO.getImageWritersByFormatName("jpeg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(1.0F);
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), param);
                writer.dispose();
                ImageIO.write(image, fileExt, ios);
                // ios.close();
                result = 1;
            } catch (IOException e) {
                result = -1;
                throw new ApplicationException(e);
            }
        } else {
            try {
                ImageIO.write(image, fileExt, new File(fileName));
                result = 1;
            } catch (IOException e) {
                result = -1;
                throw new ApplicationException(e);
            }
        }
        return result;
    }

    /**
     * Clane analog for BufferedImage.
     *
     * @param  image
     *            BufferedImage image
     * @return BufferedImage clone
     */
    public static BufferedImage duplicate(final BufferedImage image) {
        if (image == null) {
            throw new ApplicationException("BufferedImage == null");
        }

        BufferedImage j = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        j.setData(image.getData());
        return j;
        }

    /**
     * Convert Duration to string.
     * @param duration Duration to convert
     * @return String
     */
    public static String durationToString(final Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * Constructor for this class.
     *
     */
    private Utils() {
    }

}

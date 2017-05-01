package org.imgcnv.utils;

/**
 * Class hold constant for application.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public final class Consts {

    /**
     * The size of thumbnails image of first type.
     *
     */
    public static final Integer SIZE_THUMB_1 = 300;

    /**
     * The size of thumbnails image of second type.
     *
     */
    public static final Integer SIZE_THUMB_2 = 400;

    /**
     * The size of thumbnails image of third type.
     *
     */
    public static final Integer SIZE_THUMB_3 = 500;

    /**
     * The number of threads for multitreading resize process.
     *
     */
    public static final int RESIZE_THREADS =
            Runtime.getRuntime().availableProcessors();

    /**
     * The number of threads for multitreading download process.
     *
     */

    public static final int DOWNLOAD_THREADS =
            5 * Runtime.getRuntime().availableProcessors();
    /**
     * The delimiter for split url in url list.
     *
     */

    public static final int QUEUE_CAPACITY =
            2 * DOWNLOAD_THREADS;
    /**
     * The number of threads for multitreading download process.
     *
     */

    public static final String DELIMITER = ";";

    /**
     * Period for Clean up job map (in seconds).
     */
    public static final long CLEAN_PERIOD = 1 * 60;

    /**
     * Millis in second.
     */
    public static final long MILLIS_IN_SEC = 1000;
    /**
     * Constructor for this class.
     *
     */
    private Consts() {
    }

}

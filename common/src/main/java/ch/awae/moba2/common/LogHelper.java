package ch.awae.moba2.common;

import java.util.logging.Logger;

public class LogHelper {

    public static Logger getLogger() {
        return Logger.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

}

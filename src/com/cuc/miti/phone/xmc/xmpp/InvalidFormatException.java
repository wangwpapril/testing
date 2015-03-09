package com.cuc.miti.phone.xmc.xmpp;

/** 
 * Runtime exceptions produced by wrong meta-data settings.
 *
 * @author SongQing
 */
public class InvalidFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}

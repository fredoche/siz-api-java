package io.siz.exception;

/**
 *
 * @author fred
 */
public class SizException extends RuntimeException {

    public SizException() {
    }

    public SizException(String message) {
        super(message);
    }

    public SizException(String message, Throwable cause) {
        super(message, cause);
    }

    public SizException(Throwable cause) {
        super(cause);
    }

    public SizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

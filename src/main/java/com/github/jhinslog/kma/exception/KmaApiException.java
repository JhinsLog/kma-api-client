package com.github.jhinslog.kma.exception;

import java.io.IOException;

public class KmaApiException extends IOException {
    public KmaApiException(String message) {
        super(message);
    }

    public KmaApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

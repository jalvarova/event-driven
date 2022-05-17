package org.walavo.consumer.listeners.transport;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Builder
@Data
public class EventError {

    private String idTransaction;

    private String provider;

    private String exception;

    private String type;

    private String payload;

    private int status;

    private HttpMethod httpMethod;

    private String uri;

}

package org.walavo.web.reactive.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Slf4j
public class PubSubComposite {

    public SuccessCallback<String> getSuccessCallback(String message) {
        return (result) -> {
            log.info(message + " -> Successfully sent message to topic " + result);
        };
    }

    public FailureCallback getFailureCallback(String message) {
        return (error) -> {
            log.error(message + " -> Error occurred trying to publish to Google Pub sub " + error.getMessage(), error);

        };
    }
}


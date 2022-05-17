package org.walavo.consumer.config;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.walavo.consumer.listeners.transport.EventError;
import org.walavo.consumer.util.ConvertUtil;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DefaultRetryListener extends RetryListenerSupport {

    private final PubSubTemplate pubSubTemplate;

    private final String topic;

    public DefaultRetryListener(PubSubTemplate pubSubTemplate, String topic) {
        this.topic = topic;
        this.pubSubTemplate = pubSubTemplate;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("onClose");
        if (throwable instanceof FeignException) {
            log.error("Error Event Handler");
            FeignException feignException = (FeignException) context.getLastThrowable();
            if (feignException.hasRequest())
                sendMessagePubSub(feignException);
        }
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.info("onError");
        log.info(context.getRetryCount() + " Retry Callback, {} ", context.getLastThrowable() == null ? "ERROR" : context.getLastThrowable().getMessage());
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        log.info("onOpen");
        return super.open(context, callback);
    }

    private void sendMessagePubSub(FeignException exception) {
        log.info("Begin publishSendMessage");
        EventError eventError = buildError(exception);
        String message = ConvertUtil.jsonToString(eventError);
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        pubSubTemplate.publish(topic, pubsubMessage);
        log.info("End publishSendMessage");
    }

    private EventError buildError(FeignException feignException) {
        String payload = new String(feignException.request().body(), StandardCharsets.UTF_8);
        String idTransaction = feignException.request().headers().get("idTransaction").stream().findFirst().get();
        int status = feignException.status();
        String uri = feignException.request().url();
        String httpMethod = feignException.request().httpMethod().name();
        return EventError
                .builder()
                .idTransaction(idTransaction)
                .provider("DAD")
                .exception(feignException.getMessage())
                .type("HTTP")
                .status(status)
                .httpMethod(HttpMethod.valueOf(httpMethod))
                .uri(uri)
                .payload(payload)
                .build();
    }
}

package org.walavo.consumer.config;

import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpClientConfiguration {

    @Value("${inventory.uri}")
    private String uri;

    @Value("${inventory.auth-basic}")
    private String authCredentials;

    @Bean("inventoryWebApi")
    public WebClient inventoryWebApi() {
        return WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, getAuthenticationBasic())
                .build();
    }

    private synchronized String getAuthenticationBasic() {
        byte[] authBytes = Base64.encodeBase64(authCredentials.getBytes());
        String authStringEnc = new String(authBytes);
        return "Basic ".concat(authStringEnc);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        String[] auth = authCredentials.split(":");
        return new BasicAuthRequestInterceptor(auth[0], auth[1]);
    }

    private final ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

    @Bean
    public Decoder feignFormDecoder() {
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}

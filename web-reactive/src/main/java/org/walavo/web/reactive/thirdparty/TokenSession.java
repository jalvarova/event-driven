package org.walavo.web.reactive.thirdparty;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

public class TokenSession {

    @Value("${product.auth-basic}")
    private String authCredentials;

    public synchronized String getAuthenticationBasic() {
        byte[] authBytes = Base64.encodeBase64(authCredentials.getBytes());
        String authStringEnc = new String(authBytes);
        return "Basic ".concat(authStringEnc);
    }

    @Value("${inventory.auth-basic}")
    private String authInventoryCredentials;

    public synchronized String getInventoryAuthenticationBasic() {
        byte[] authBytes = Base64.encodeBase64(authInventoryCredentials.getBytes());
        String authStringEnc = new String(authBytes);
        return "Basic ".concat(authStringEnc);
    }}

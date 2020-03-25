package io.micronaut.bots.slack.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMAC {

    public static final String UTF_8 = "UTF-8";
    public static final String HMAC_SHA_256 = "HmacSHA256";

    public static String hexHmacSha256(String secret, String msg)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        byte[] hmacSha256 = calculateHmacSha256(secret.getBytes(UTF_8), msg.getBytes(UTF_8));
        return String.format("%032x", new BigInteger(1, hmacSha256));

    }

    public static byte[] calculateHmacSha256(byte[] secretKey, byte[] message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] hmacSha256 = null;
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);

        return hmacSha256;
    }
}

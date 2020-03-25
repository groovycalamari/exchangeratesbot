/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

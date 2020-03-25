package io.micronaut.bots.slack.security;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.slack.conf.SlackConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

/**
 * {@link io.micronaut.context.annotation.DefaultImplementation} of {@link RequestVerifier}.
 */
@Singleton
public class DefaultRequestVerifier implements RequestVerifier {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRequestVerifier.class);
    public static final String UTF_8 = "UTF-8";
    public static final String COLON = ":";
    public static final String VERSION_0 = "v0";

    protected final Collection<SlackConfiguration> slackConfigurationCollection;

    public DefaultRequestVerifier(Collection<SlackConfiguration> slackConfigurationCollection) {
        this.slackConfigurationCollection = slackConfigurationCollection;
    }

    @Override
    public Optional<SlackConfiguration> verify(@NonNull @NotBlank String slackSignature,
                                               @NonNull @NotBlank String slackRequestTimestamp,
                                               @NonNull @NotBlank String requestBody) {
            return slackConfigurationCollection.stream()
                    .filter(conf -> {
                        try {
                            String computedSignature = computeSignature(conf.getSigningSecret(), slackRequestTimestamp, requestBody);
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("computed signature: {} slack signature {}", computedSignature, slackSignature);
                            }
                            return MessageDigest.isEqual(computedSignature.getBytes(UTF_8), slackSignature.getBytes(UTF_8));

                        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error("error computing signature {}", e.getMessage());
                            }
                        }
                        return false;
                    }).findFirst();
    }

    private String computeSignature(@NonNull @NotBlank String signingSecret,
                                    @NonNull @NotBlank String slackRequestTimestamp,
                                    @NonNull @NotBlank String requestBody)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        return VERSION_0 + "=" + HMAC.hexHmacSha256(signingSecret, String.join(COLON, VERSION_0, slackRequestTimestamp,requestBody));
    }
}

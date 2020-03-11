package groovycalamari.exchangeratesbot.telegram;

import io.micronaut.bots.telegram.httpclient.TelegramBotConfiguration;
import io.micronaut.context.annotation.Requires;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

@Requires(classes = TelegramBotConfiguration.class)
@Named(ForeignExchangeRatesTelegramBotConfiguration.NAME)
@Singleton
public class ForeignExchangeRatesTelegramBotConfiguration implements TelegramBotConfiguration {
    public static final String NAME = "foreignexchangeratesbot";
    public static final String AT_USERNAME = "@ForeignExchangeRatesBot";
    public static final String ENV_TOKEN = "TOKEN";

    @Nonnull
    @Override
    public String getToken() {
        return System.getenv(ENV_TOKEN);
    }

    @Nonnull
    @Override
    public String getAtUsername() {
        return AT_USERNAME;
    }
    @Nonnull
    @Override
    public String getName() {
        return ForeignExchangeRatesTelegramBotConfiguration.NAME;
    }
}

package groovycalamari.exchangerates.telegram.commands;

import io.micronaut.bots.telegram.httpclient.TelegramBotConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

@Named(ForeignExchangeRatesTelegramBotConfiguration.NAME)
@Singleton
public class ForeignExchangeRatesTelegramBotConfiguration implements TelegramBotConfiguration {
    public static final String NAME = "foreignexchangeratesbot";
    public static final String ENV_TOKEN = "TOKEN";

    @Nonnull
    @Override
    public String getToken() {
        return System.getenv(ENV_TOKEN);
    }

    @Nonnull
    @Override
    public String getName() {
        return ForeignExchangeRatesTelegramBotConfiguration.NAME;
    }
}

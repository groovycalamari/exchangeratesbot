package groovycalamari.exchangeratesbot.googlechat;

import io.micronaut.bots.googlechat.core.GoogleChatBotConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

@Named(DefaultGoogleChatBotConfiguration.GOOGLE_CHAT_BOT_EXCHANGERATES)
@Singleton
public class DefaultGoogleChatBotConfiguration implements GoogleChatBotConfiguration {
    public static final String GOOGLE_CHAT_BOT_EXCHANGERATES = "exchangerates";
    @Nonnull
    @Override
    public String getProjectId() {
        return System.getenv("PROJECT_ID");
    }

    @Nonnull
    @Override
    public String getAtUsername() {
        return "@ExchangeRates";
    }

    @Nonnull
    @Override
    public String getName() {
        return GOOGLE_CHAT_BOT_EXCHANGERATES;
    }
}

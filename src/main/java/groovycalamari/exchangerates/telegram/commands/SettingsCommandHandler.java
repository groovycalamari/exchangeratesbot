package groovycalamari.exchangerates.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.InlineKeyboardButton;
import io.micronaut.bots.telegram.core.InlineKeyboardMarkup;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.DefaultUpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.InlineKeyboardHandler;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("settings")
@Singleton
public class SettingsCommandHandler extends InlineKeyboardHandler {

    public static final String BASE_CURRENCY = "Base Currency";
    public static final String TARGET_CURRENCY = "Target Currency";
    private final UserRepository userRepository;

    protected SettingsCommandHandler(ObjectMapper objectMapper,
                                     UpdateParser updateParser,
                                     UserRepository userRepository) {
        super(objectMapper, updateParser);
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    protected String messageText(@NonNull TelegramBot telegramBot,
                                 @NonNull Update update) {
        String defaultText = "Configure your default base and target currencies";
        Optional<Integer> userIdOptional = updateParser.parseUserId(update);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            Optional<Settings> settingsOptional = userRepository.findByUserId(userId);
            if (!settingsOptional.isPresent()) {
                userRepository.save(userId);
                settingsOptional = userRepository.findByUserId(userId);
            }
            if (settingsOptional.isPresent()) {
                Settings settings = settingsOptional.get();
                return "Your base currency is: " + settings.getBase().getCode() + " and your target currency is: " + settings.getTarget().getCode() + "\n" + defaultText;
            }
        }
        return defaultText;
    }

    @Override
    @NonNull
    protected InlineKeyboardMarkup createInlineKeyboardMarkup(@NonNull TelegramBot telegramBot,
                                                            @NonNull Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton baseBtn = new InlineKeyboardButton();
        baseBtn.setCallbackData(telegramBot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseBaseCommandHandler.COMMAND_CHOOSEBASE);
        baseBtn.setText(BASE_CURRENCY);
        row.add(baseBtn);

        InlineKeyboardButton targetBtn = new InlineKeyboardButton();
        targetBtn.setCallbackData(telegramBot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseTargetCommandHandler.COMMAND_CHOOSETARGET);
        targetBtn.setText(TARGET_CURRENCY);
        row.add(targetBtn);
        keyboard.add(row);
        inlineKeyboardMarkup.setInlineKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}

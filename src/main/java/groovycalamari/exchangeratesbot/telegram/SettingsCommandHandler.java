package groovycalamari.exchangeratesbot.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.NonNull;
import groovycalamari.exchangeratesbot.ChooseBaseCommand;
import groovycalamari.exchangeratesbot.ChooseTargetCommand;
import groovycalamari.exchangeratesbot.Settings;
import groovycalamari.exchangeratesbot.UserRepository;
import io.micronaut.bots.core.ChatBotMessageParser;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.core.MessageComposer;
import io.micronaut.bots.telegram.core.InlineKeyboardButton;
import io.micronaut.bots.telegram.core.InlineKeyboardMarkup;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.InlineKeyboardHandler;
import io.micronaut.bots.telegram.httpclient.TelegramBot;
import io.micronaut.context.annotation.Requires;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Requires(classes = InlineKeyboardHandler.class)
@Named(SettingsCommandHandler.COMMAND_SETTINGS)
@Singleton
public class SettingsCommandHandler extends InlineKeyboardHandler {

    public static final String COMMAND_SETTINGS = "settings";
    public static final String BASE_CURRENCY = "Base Currency";
    public static final String TARGET_CURRENCY = "Target Currency";
    private final UserRepository userRepository;
    protected final ChatBotMessageParser messageParser;

    protected SettingsCommandHandler(ObjectMapper objectMapper,
                                     MessageComposer messageComposer,
                                     ChatBotMessageParser messageParser,
                                     UserRepository userRepository) {
        super(objectMapper, messageComposer);
        this.messageParser = messageParser;
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    protected String messageText(@NonNull TelegramBot telegramBot,
                                 @NonNull Update update) {
        String defaultText = "Configure your default base and target currencies";
        Optional<Serializable> userIdOptional = messageParser.parseUserId(update);
        if (userIdOptional.isPresent()) {
            Serializable userId = userIdOptional.get();
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
        baseBtn.setCallbackData(telegramBot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseBaseCommand.COMMAND_CHOOSEBASE);
        baseBtn.setText(BASE_CURRENCY);
        row.add(baseBtn);

        InlineKeyboardButton targetBtn = new InlineKeyboardButton();
        targetBtn.setCallbackData(telegramBot.getAtUsername() + " " + CommandHandler.COMMAND_PREFIX + ChooseTargetCommand.COMMAND_CHOOSETARGET);
        targetBtn.setText(TARGET_CURRENCY);
        row.add(targetBtn);
        keyboard.add(row);
        inlineKeyboardMarkup.setInlineKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}

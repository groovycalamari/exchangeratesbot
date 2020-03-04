package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.bots.telegram.core.Update;
import io.micronaut.bots.telegram.dispatcher.FileCommandHandler;
import io.micronaut.bots.telegram.dispatcher.ParseMode;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import io.micronaut.bots.telegram.httpclient.TelegramBot;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named(StartCommandHandler.COMMAND_START)
@Singleton
public class StartCommandHandler extends FileCommandHandler {

    public static final String COMMAND_START = "start";
    private UserRepository userRepository;

    public StartCommandHandler(UpdateParser updateParser, UserRepository userRepository) {
        super(updateParser, ParseMode.MARKDOWN, "classpath:help.md");
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    protected Optional<String> replyUpdate(@NonNull TelegramBot telegramBot,
                                           @NonNull Update update) {
        Optional<String> reply = super.replyUpdate(telegramBot, update);

        Optional<Integer> userIdOptional = updateParser.parseUserId(update);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            if(userRepository.findByUserId(userId).isEmpty()){
                userRepository.save(userId);
            }
        }
        return reply;
    }
}

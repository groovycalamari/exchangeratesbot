package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.bots.telegram.dispatcher.CommandHandler;
import io.micronaut.bots.telegram.dispatcher.DefaultUpdateDispatcher;
import io.micronaut.bots.telegram.dispatcher.UpdateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named(SetBaseCommandHandler.COMMAND_SETBASE)
public class SetBaseCommandHandler extends SetCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SetBaseCommandHandler.class);

    public static final String COMMAND_SETBASE = "setbase";
    protected final UserRepository userRepository;

    public SetBaseCommandHandler(UpdateParser updateParser,
                                 UserRepository userRepository) {
        super(updateParser);
        this.userRepository = userRepository;
    }

    @Override
    protected String successMessage(@NonNull Currency currency) {
        return "Base currency set to " + currency.getCode();
    }

    protected String getCommandName() {
        return CommandHandler.COMMAND_PREFIX + COMMAND_SETBASE;
    }

    @Override
    protected void updateAttribute(@NonNull Integer userId, @NonNull Currency currency) {
        userRepository.updateBase(userId, currency);
    }


}

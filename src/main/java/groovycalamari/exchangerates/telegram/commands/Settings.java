package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Settings {
    @NonNull
    private Currency base;

    @NonNull
    private Currency target;

    public Settings() {
    }

    @NonNull
    public Currency getBase() {
        return base;
    }

    public void setBase(@NonNull Currency base) {
        this.base = base;
    }

    @NonNull
    public Currency getTarget() {
        return target;
    }

    public void setTarget(@NonNull Currency target) {
        this.target = target;
    }
}

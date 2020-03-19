package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;

import io.exchangeratesapi.Currency;

import java.io.Serializable;
import java.util.Optional;

public interface UserRepository {
    static final Currency DEFAULT_BASE = Currency.EUR;
    static final Currency DEFAULT_TARGET = Currency.USD;

    default Settings defaultSettings() {
        Settings settings = new Settings();
        settings.setBase(DEFAULT_BASE);
        settings.setTarget(DEFAULT_TARGET);
        return settings;
    }

    Optional<Settings> findByUserId(@NonNull Serializable userid);

    void updateBase(@NonNull Serializable userid, @NonNull Currency base);

    void updateTarget(@NonNull Serializable userid, @NonNull Currency target);

    void save(@NonNull Serializable userid, @NonNull Currency base, @NonNull Currency target);

    void save(@NonNull Serializable userId);
}

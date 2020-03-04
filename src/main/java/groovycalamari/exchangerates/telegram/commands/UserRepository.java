package groovycalamari.exchangerates.telegram.commands;

import edu.umd.cs.findbugs.annotations.NonNull;

import io.exchangeratesapi.Currency;

import java.util.Optional;

public interface UserRepository {

    Optional<Settings> findByUserId(@NonNull Integer userid);

    void updateBase(@NonNull Integer userid, @NonNull Currency base);

    void updateTarget(@NonNull Integer userid, @NonNull Currency target);

    void save(@NonNull Integer userid, @NonNull Currency base, @NonNull Currency target);

    void save(@NonNull Integer userId);
}

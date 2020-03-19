package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.context.annotation.Secondary;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Optional;

@Secondary
@Singleton
public class FallbackUserRepository implements UserRepository {
    @Override
    public Optional<Settings> findByUserId(@NonNull Serializable userid) {
        return Optional.of(defaultSettings());
    }

    @Override
    public void updateBase(@NonNull Serializable userid, @NonNull Currency base) {

    }

    @Override
    public void updateTarget(@NonNull Serializable userid, @NonNull Currency target) {

    }

    @Override
    public void save(@NonNull Serializable userid, @NonNull Currency base, @NonNull Currency target) {

    }

    @Override
    public void save(@NonNull Serializable userId) {

    }
}

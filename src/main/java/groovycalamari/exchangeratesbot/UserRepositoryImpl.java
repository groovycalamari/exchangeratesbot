package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class UserRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final Currency DEFAULT_BASE = Currency.EUR;
    private static final Currency DEFAULT_TARGET = Currency.USD;

    private final String TABLENAME = System.getenv("DYNAMODB_TABLE");
    public static final String KEY = "userid";
    public static final String ATTRIBUTE_BASE = "base";
    public static final String ATTRIBUTE_TARGET = "target";
    private final DynamoDbClient ddb;

    public UserRepositoryImpl() {
        this.ddb = DynamoDbClient.builder().region(Region.of(System.getenv("DYNAMODB_REGION"))).build();
    }

    private AttributeValue attributeValueForUserId(@NonNull Serializable userId) {
        if (userId instanceof Integer) {
            return AttributeValue.builder().n(String.valueOf(userId)).build();
        }
        return AttributeValue.builder().s(String.valueOf(userId)).build();
    }

    private AttributeValueUpdate attributeValueUpdate(Currency currency) {
        return AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(currency.getCode()).build())
                .action(AttributeAction.PUT)
                .build();
    }

    private void updateRow(@NonNull Serializable userId, @Nullable Currency base, @Nullable Currency target) {
        try {
            if (existsByUserId(userId)) {
                HashMap<String, AttributeValue> itemKey = new HashMap<>();
                itemKey.put(KEY, attributeValueForUserId(userId));
                HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
                if (target != null) {
                    updatedValues.put(ATTRIBUTE_TARGET, attributeValueUpdate(target));
                }
                if (base != null) {
                    updatedValues.put(ATTRIBUTE_BASE, attributeValueUpdate(base));
                }
                UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                        .tableName(TABLENAME)
                        .key(itemKey)
                        .attributeUpdates(updatedValues)
                        .build();
                ddb.updateItem(updateItemRequest);

            } else {
                HashMap<String, AttributeValue> itemValues = new HashMap<>();
                itemValues.put(KEY, attributeValueForUserId(userId));
                final Currency userBase = base != null ? base : DEFAULT_BASE;
                itemValues.put(ATTRIBUTE_BASE, AttributeValue.builder().s(userBase.getCode()).build());
                final Currency userTarget = target != null ? target : DEFAULT_TARGET;
                itemValues.put(ATTRIBUTE_TARGET, AttributeValue.builder().s(userTarget.getCode()).build());

                PutItemRequest putItemRequest = PutItemRequest.builder()
                        .tableName(TABLENAME)
                        .item(itemValues)
                        .build();
                ddb.putItem(putItemRequest);
            }
        } catch (DynamoDbException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("{}", e);
            }
        }
    }

    @Override
    public Optional<Settings> findByUserId(@NonNull Serializable userid) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Finding user {}", userid);
        }
        GetItemRequest request = findByUserIdGetItemRequest(userid);
        try {
            Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();
            if (request != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("User found");
                }
                return parseSettings(returnedItem);
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("User not found");
                }
            }

        } catch (DynamoDbException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("dynamo db exception {}", e.getMessage());
            }
        }
        save(userid, DEFAULT_BASE, DEFAULT_TARGET);
        return Optional.of(defaultSettings());
    }

    private boolean existsByUserId(@NonNull Serializable userid) {
        GetItemRequest request = findByUserIdGetItemRequest(userid);
        try {
            Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();
            return returnedItem != null;
            } catch(DynamoDbException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("dynamo db exception {}", e);
            }
        }
        return false;
    }
    private GetItemRequest findByUserIdGetItemRequest(@NonNull Serializable userid) {
        final String tableName = TABLENAME;
        HashMap<String, AttributeValue> keyToGet = new HashMap<String,AttributeValue>();
        keyToGet.put(KEY, attributeValueForUserId(userid));
        return GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();
    }

    private Settings defaultSettings() {
        Settings settings = new Settings();
        settings.setBase(DEFAULT_BASE);
        settings.setTarget(DEFAULT_TARGET);
        return settings;
    }

    private Optional<Settings> parseSettings(Map<String, AttributeValue> returnedItem) {
        Settings settings = new Settings();
        if (LOG.isInfoEnabled()) {
            LOG.info("returned items {}", String.join(",", returnedItem.keySet()));
        }
        if (returnedItem.containsKey(ATTRIBUTE_BASE)) {
            Stream.of(Currency.values()).filter(it -> it.getCode().equals(returnedItem.get(ATTRIBUTE_BASE).s())).findFirst().ifPresent(settings::setBase);
        } else {
            settings.setBase(DEFAULT_BASE);
        }
        if (returnedItem.containsKey(ATTRIBUTE_TARGET)) {
            Stream.of(Currency.values()).filter(it -> it.getCode().equals(returnedItem.get(ATTRIBUTE_TARGET).s())).findFirst().ifPresent(settings::setTarget);
        } else {
            settings.setTarget(DEFAULT_TARGET);
        }
        if (settings.getBase() != null && settings.getTarget() != null) {
            return Optional.of(settings);
        }
        return Optional.empty();
    }

    @Override
    public void updateBase(@NonNull Serializable userid, @NonNull Currency base) {
        updateRow(userid, base, null);
    }


    @Override
    public void updateTarget(@NonNull Serializable userid, @NonNull Currency target) {
        updateRow(userid, null, target);
    }

    @Override
    public void save(@NonNull Serializable userid, @NonNull Currency base, @NonNull Currency target) {
        updateRow(userid, base, target);
    }

    @Override
    public void save(@NonNull Serializable userId) {
        save(userId, DEFAULT_BASE, DEFAULT_TARGET);
    }
}

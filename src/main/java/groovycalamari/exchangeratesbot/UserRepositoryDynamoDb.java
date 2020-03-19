package groovycalamari.exchangeratesbot;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.exchangeratesapi.Currency;
import io.micronaut.context.annotation.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Requires(classes = DynamoDbClient.class)
@Singleton
public class UserRepositoryDynamoDb implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryDynamoDb.class);


    private final String TABLENAME = System.getenv("DYNAMODB_TABLE");
    public static final String KEY = "userid";
    public static final String ATTRIBUTE_BASE = "base";
    public static final String ATTRIBUTE_TARGET = "target";
    private final DynamoDbClient ddb;

    public UserRepositoryDynamoDb() {
        this.ddb = DynamoDbClient.builder().region(Region.of(System.getenv("DYNAMODB_REGION"))).build();
    }

    private AttributeValue attributeValueForUserId(@NonNull Serializable userId) {
        if (userId instanceof Integer) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Setting user id attribute value as number");
            }
            return AttributeValue.builder().n(String.valueOf(userId)).build();
        } else if (userId instanceof String) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Setting user id attribute value as string");
            }
            return AttributeValue.builder().s((String) userId).build();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("Setting user id attribute value as string calling String.valueOf");
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
            LOG.info("Finding user by id {}", userid);
        }
        GetItemRequest request = findByUserIdGetItemRequest(userid);
        try {
            GetItemResponse response = ddb.getItem(request);
            if (response.hasItem()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("response has item");
                }
                Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();
                if (returnedItem != null) {
                    return parseSettings(returnedItem);
                } else {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("returned item is null");
                    }
                }
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
            GetItemResponse response = ddb.getItem(request);
            return response.hasItem();
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

    private Optional<Settings> parseSettings(Map<String, AttributeValue> returnedItem) {
        Settings settings = new Settings();
        if (LOG.isWarnEnabled() && returnedItem.keySet().isEmpty()) {
            LOG.info("returned items is empty");
        }
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

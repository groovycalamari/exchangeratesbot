package groovycalamari.exchangerates.telegram.commands;

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

    private void updateRow(@NonNull Integer userId, @Nullable Currency base, @Nullable Currency target) {
        try {
            if (findByUserId(userId).isPresent()) {
                HashMap<String, AttributeValue> itemKey = new HashMap<>();
                itemKey.put(KEY, AttributeValue.builder().n(String.valueOf(userId)).build());


                String attribute = null;
                Currency attributeValue = null;
                if (target != null) {
                    attribute = ATTRIBUTE_TARGET;
                    attributeValue = target;
                } else if (base != null) {
                    attribute = ATTRIBUTE_BASE;
                    attributeValue = base;
                }

                if (attribute != null && attributeValue != null) {
                    HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
                    updatedValues.put(attribute, AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(attributeValue.getCode()).build())
                            .action(AttributeAction.PUT)
                            .build());

                    UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                            .tableName(TABLENAME)
                            .key(itemKey)
                            .attributeUpdates(updatedValues)
                            .build();
                    ddb.updateItem(updateItemRequest);
                }

            } else {
                HashMap<String, AttributeValue> itemValues = new HashMap<>();
                itemValues.put(KEY, AttributeValue.builder().n(String.valueOf(userId)).build());
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
                LOG.error("{}" , e);
            }
        }
    }

    @Override
    public Optional<Settings> findByUserId(@NonNull Integer userid) {
        final String tableName = TABLENAME;
        HashMap<String, AttributeValue> keyToGet = new HashMap<String,AttributeValue>();
        keyToGet.put(KEY, AttributeValue.builder().n(String.valueOf(userid)).build());
        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();
        try {
            Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();
            if (request == null) {
                return Optional.empty();
            }
            Settings settings = new Settings();
            if (returnedItem.containsKey(ATTRIBUTE_BASE)) {
                Stream.of(Currency.values()).filter(it -> it.getCode().equals(returnedItem.get(ATTRIBUTE_BASE).s())).findFirst().ifPresent(settings::setBase);
            }
            if (returnedItem.containsKey(ATTRIBUTE_TARGET)) {
                Stream.of(Currency.values()).filter(it -> it.getCode().equals(returnedItem.get(ATTRIBUTE_TARGET).s())).findFirst().ifPresent(settings::setTarget);
            }
            if (settings.getBase() != null && settings.getTarget() != null) {
                return Optional.of(settings);
            }
            return Optional.empty();

        } catch (DynamoDbException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("{}" , e);
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateBase(@NonNull Integer userid, @NonNull Currency base) {
        updateRow(userid, base, null);
    }


    @Override
    public void updateTarget(@NonNull Integer userid, @NonNull Currency target) {
        updateRow(userid, null, target);
    }

    @Override
    public void save(@NonNull Integer userid, @NonNull Currency base, @NonNull Currency target) {
        updateRow(userid, base, target);
    }

    @Override
    public void save(@NonNull Integer userId) {
        save(userId, DEFAULT_BASE, DEFAULT_TARGET);
    }
}

package groovycalamari.exchangeratesbot

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse

class SetBaseCommandHandleSpec extends ApplicationContextSpecification {


    void "it is possible to update dynamodbtable"() {
        when:
        System.out.println("Your DynamoDB tables:\n");

        // snippet-start:[dynamodb.java2.list_tables.main]

        // Create the DynamoDbClient object
        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        boolean moreTables = true;
        String lastName = null;

        while(moreTables) {
            try {
                ListTablesResponse response = null;
                if (lastName == null) {
                    ListTablesRequest request = ListTablesRequest.builder().build();
                    response = ddb.listTables(request);
                } else {
                    ListTablesRequest request = ListTablesRequest.builder()
                            .exclusiveStartTableName(lastName).build();
                    response = ddb.listTables(request);
                }

                List<String> tableNames = response.tableNames();

                if (tableNames.size() > 0) {
                    for (String curName : tableNames) {
                        System.out.format("* %s\n", curName);
                    }
                } else {
                    System.out.println("No tables found!");
                    System.exit(0);
                }

                lastName = response.lastEvaluatedTableName();
                if (lastName == null) {
                    moreTables = false;
                }
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        // snippet-end:[dynamodb.java2.list_tables.main]
        System.out.println("\nDone!");

        then:
        true
    }
}

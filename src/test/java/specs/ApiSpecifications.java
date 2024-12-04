package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class ApiSpecifications {
    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:9997/api/v1/")
                .addHeader("Content-Type", "application/json")
                .build();
    }
}

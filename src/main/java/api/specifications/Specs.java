package api.specifications;

import api.config.TestConfig;
import api.helpersAndUtils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {

    public static RequestSpecification getRequestSpec() {
        if (RestAssured.requestSpecification == null) { // Проверка на существование спецификации
            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .setBaseUri(TestConfig.getBaseUrl())
                    .addHeader("Content-Type", "application/json")
                    .addFilter((req, res, ctx) -> {
                        LogUtil.logRequest((FilterableRequestSpecification) req); //req приводится к типу FilterableRequestSpecification
                        Response response = ctx.next(req, res);
                        LogUtil.logResponse(response);
                        return response;
                    })
                    .build();
        }
        return RestAssured.requestSpecification;
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .build();
    }

    public static void setSpecs(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }
}

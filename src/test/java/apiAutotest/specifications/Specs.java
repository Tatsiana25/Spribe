package apiAutotest.specifications;

import apiAutotest.config.TestConfig;
import apiAutotest.helpersAndUtils.LogUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.getBaseUrl())
                .addHeader("Content-Type", "application/json")
                //.setRelaxedHTTPSValidation() // Отключение проверки SSL
                .addFilter((req, res, ctx) -> {
                    LogUtil.logRequest((FilterableRequestSpecification) req); //req приводится к типу FilterableRequestSpecification
                    Response response = ctx.next(req, res);
                    LogUtil.logResponse(response);
                    return response;
                })
                .build();
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

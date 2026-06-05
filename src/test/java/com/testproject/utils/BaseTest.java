package com.testproject.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.Matchers.lessThan;

public abstract class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected static RequestSpecification requestSpec;
    protected static RequestSpecification requestSpecNoFail;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    static void configureRestAssured() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();

        // 404 gibi hata kodlarında exception fırlatmayan spec
        requestSpecNoFail = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .setConfig(RestAssuredConfig.config().httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 5000)
                ))
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(ApiConfig.MAX_RESPONSE_TIME_MS))
                .build();
    }
}
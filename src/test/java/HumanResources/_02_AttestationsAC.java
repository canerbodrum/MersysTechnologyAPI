package HumanResources;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.*;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;

public class _02_AttestationsAC {


    RequestSpecification recSpec;
    Faker faker = new Faker();

    String attestationsID;
    String attestationsName;

    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().detailedCookies();
        recSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createAttestations() {
        Map<String, String> attestation = new HashMap<>();
        attestationsName = faker.name() + faker.number().digits(3);
        attestation.put("name", attestationsName);

        attestationsID =
                given()
                        .spec(recSpec)
                        .body(attestation)
                        .when()
                        .post("school-service/api/attestation")
                        .then()
                        .statusCode(201)
                        .log().all()
                        .extract().path("id")

        ;
    }

    @Test(dependsOnMethods = "createAttestations")
    public void createAttestationsNegative() {

        Map<String, String> attestation = new HashMap<>();
        attestation.put("name", attestationsName);

        given()
                .spec(recSpec)
                .body(attestation)
                .when()
                .post("school-service/api/attestation")
                .then()
                .statusCode(400)
                .log().all()

        ;
    }

    @Test(dependsOnMethods = "createAttestationsNegative")
    public void updateAttestations() {

        Map<String, String> attestation = new HashMap<>();
        attestation.put("id",attestationsID);
        attestation.put("name", "ahmetcaner"+faker.number().digits(3));

        given()
                .spec(recSpec)
                .body(attestation)
                .when()
                .put("school-service/api/attestation")
                .then()
                .statusCode(200)
                .log().all()

        ;
    }

    @Test(dependsOnMethods = "updateAttestations")
    public void deleteAttestations() {

        given()
                .spec(recSpec)
                .pathParam("id",attestationsID)
                .when()
                .delete("school-service/api/attestation/{id}")
                .then()
                .statusCode(204)
                .log().all()

        ;
    }

    @Test(dependsOnMethods = "deleteAttestations")
    public void deleteAttestationsNegative() {

        given()
                .spec(recSpec)
                .pathParam("id",attestationsID)
                .when()
                .delete("school-service/api/attestation/{id}")
                .then()
                .statusCode(400)
                .log().all()
        ;
    }
}

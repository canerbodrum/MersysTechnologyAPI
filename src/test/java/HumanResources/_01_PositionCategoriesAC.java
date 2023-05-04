package HumanResources;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;

public class _01_PositionCategoriesAC {

    RequestSpecification recSpec;
    Faker faker = new Faker();

    String positionID;
    String positionName;
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
    public void create() {
        Map<String, String> information = new HashMap<>();
        positionName=faker.name()+faker.number().digits(3);
        information.put("name",positionName );

        positionID=
                given()
                        .spec(recSpec)
                        .body(information)
                .when()
                        .post("school-service/api/position-category")
                .then()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods ="create" )
    public void createNegative() {
        Map<String, String> information = new HashMap<>();
        information.put("name",positionName );


                given()
                        .spec(recSpec)
                        .body(information)
                        .when()
                        .post("school-service/api/position-category")
                        .then()
                        .statusCode(400)
        ;

    }

    @Test(dependsOnMethods ="createNegative" )
    public void update() {
        Map<String, String> information = new HashMap<>();
        information.put("id",positionID);
        information.put("name","ahmetcaner"+faker.number().digits(3) );


                given()
                        .spec(recSpec)
                        .body(information)
                        .when()
                        .put("school-service/api/position-category")
                        .then()
                        .statusCode(200)
                        .log().all()
        ;
        System.out.println("information = " + information);
    }

    @Test(dependsOnMethods = "update" )
    public void delete() {

        given()
                .spec(recSpec)
                .when()
                .pathParam("positionID",positionID)
                .delete("school-service/api/position-category/{positionID}")
                .then()
                .statusCode(204)
                .log().all()
        ;
    }

    @Test(dependsOnMethods = "delete" )
    public void deleteNegative() {

        given()
                .spec(recSpec)
                .when()
                .pathParam("positionID",positionID)
                .delete("school-service/api/position-category/{positionID}")
                .then()
                .statusCode(400)
                .log().all()
                .body("message",containsString("not  found"))
        ;
    }

}

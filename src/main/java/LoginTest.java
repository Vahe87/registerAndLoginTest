import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class LoginTest {

    private String login = "{\n" +
            "  \"name\": \"Alexandr\",\n" +
            "  \"email\": \"Alexandr@gmail.com\",\n" +
            "  \"password\": \"1234567\" \n}";

    private String anotherPasswordLogin = "{\n" +
            "  \"name\": \"Alexandr\",\n" +
            "  \"email\": \"Alexandr@gmail.com\",\n" +
            "  \"password\": \"654321\" \n}";

    String baseUri = "http://restapi.adequateshop.com";
    String token = "d7f28bb7-625c-4381-a27c-3336aa33536c";

    RequestSpecification reqSpec = given()
            .contentType(ContentType.JSON)
            .baseUri(baseUri)
            .auth()
            .preemptive()
            .basic("required_email", "required_password")
            .body(login);

    @Test
    public void loginTest() {
        given()
                .spec(reqSpec)
                .when()
                .post("/api/authaccount/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    public void anotherPasswordLoginTest() {
        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .auth()
                .preemptive()
                .basic("required_email", "required_password")
                .body(anotherPasswordLogin)
                .when()
                .post("/api/authaccount/login")
                .then().log().all()
                .statusCode(200)
                .body("message", equalTo("invalid username or password"));
    }

    @Test
    public void bodyLogin() {

        given()
                .spec(reqSpec)
                .when()
                .post("/api/authaccount/login")
                .then().log().all()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.Id", notNullValue())
                .body("data.Name", notNullValue())
                .body("data.Email", notNullValue())
                .body("data.Token", notNullValue());

    }

    @Test
    public void notNullLogin() {

        int id = 135715;
        String name = "Alexandr";
        String email = "Alexandr@gmail.com";

        Response response = given()
                .spec(reqSpec)
                .when()
                .post("/api/authaccount/login")
                .then().log().all()
                .statusCode(200)
                .body("data.Email", endsWith("@gmail.com"))
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        int loginId = jsonPath.getInt("data.Id");
        String loginName = jsonPath.get("data.Name");
        String loginEmail = jsonPath.get("data.Email");

        Assert.assertEquals(id, loginId);
        Assert.assertEquals(name, loginName);
        Assert.assertEquals(email, loginEmail);

    }

    @Test
    public void mailTest() {

        given()
                .spec(reqSpec)
                .when()
                .post("/api/authaccount/login")
                .then().log().all().statusCode(200)
                .body("data.Email", endsWith("@gmail.com"));

    }

    @Test
    public void tokenTest() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .post("/api/authaccount/login")
                .then().log().all().statusCode(200)
                .body("data", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String newLoginToken = jsonPath.get("data.Token");
        Assert.assertNotSame(token, newLoginToken);

    }
}

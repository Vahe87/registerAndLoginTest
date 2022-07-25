import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RegisterTest {

    private String validRequest = "{\n" +
            "  \"name\": \"Jhon\",\n" +
            "  \"email\": \"jhon6666@gmail.com\",\n" +
            "  \"password\": \"123456\" \n}";

    private String checkMail = "{\n" +
            "  \"name\": \"arkadi\",\n" +
            "  \"email\": \"arkadi5555@gmail.com\",\n" +
            "  \"password\": \"123456\" \n}";

    private String notName = "{\n" +
            "  \"name\": \"\",\n" +
            "  \"email\": \"Jonson@gmail.com\",\n" +
            "  \"password\": \"123456\" \n}";

    private String notEmail = "{\n" +
            "  \"name\": \"Karina\",\n" +
            "  \"email\": \"\",\n" +
            "  \"password\": \"123456\" \n}";

    private String notPassword = "{\n" +
            "  \"name\": \"arthur\",\n" +
            "  \"email\": \"arthur@gmail.com\",\n" +
            "  \"password\": \"\" \n}";

    private String shortPassword = "{\n" +
            "  \"name\": \"Anna\",\n" +
            "  \"email\": \"Anna44@gmail.com\",\n" +
            "  \"password\": \"12345\" \n}";

    String baseUri = "http://restapi.adequateshop.com/";
    String basePath = "api/authaccount/";

    @Test
    public void registerTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_email", "required_password")
                .body(validRequest)
                .when()
                .post("registration")
                .then().log().all().statusCode(200);

    }

    @Test
    public void errRegisterTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(validRequest)
                .when()
                .post("registration")
                .then().log().all()
                .body("message", equalTo("The email address you have entered is already registered"))
                .extract().response();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        Assert.assertEquals(409, statusCode);
    }

    @Test
    public void contTypeTest() {

        given()
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(validRequest)
                .when()
                .post("registration")
                .then().log().all().statusCode(415);
    }

    @Test
    public void notNameTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(notName)
                .when()
                .post("registration")
                .then().log().all().statusCode(400);

    }

    @Test
    public void notEmailTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(notEmail)
                .when()
                .post("registration")
                .then().log().all().statusCode(400);

    }

    @Test
    public void notPasswordTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(notPassword)
                .when()
                .post("registration")
                .then().log().all().statusCode(400);

    }

    @Test
    public void shortPasswordTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(shortPassword)
                .when()
                .post("registration")
                .then().log().all().statusCode(400);

    }

    @Test
    public void checkMailTest() {

        given()
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .basePath(basePath)
                .auth()
                .preemptive()
                .basic("required_name", "required_password")
                .body(checkMail)
                .when()
                .post("registration")
                .then().log().all().statusCode(200)
                .body("data.Email", endsWith("@gmail.com"));

    }
}

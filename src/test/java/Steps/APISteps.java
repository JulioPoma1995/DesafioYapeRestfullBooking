package Steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APISteps {

    private static RequestSpecification request;
    private Response response;
    private ValidatableResponse json;

    public static int BookingId;
    private int code;

    public static String Token;


    @Given("^I declare the base URI (.*)$")
    public void GivenBaseUri(String URI){
        request = given()
                .baseUri(URI)
                .contentType(ContentType.JSON);
    }
    @Then("^I send a GET request on the (.*) endpoint to validate a (\\d+) status code$")
    public void expectedStatusCode(String endpoint, int expectedStatusCode){
        response = request
                    .when()
                    .get(endpoint);
            json = response.then().statusCode(expectedStatusCode);
    }

    @And ("^I validate that the API show more than (\\d+) record$")
    public void ValidateRecods(int notexpectedzise){

        List<String> jsonResponse = response.jsonPath().getList("booking");
        int actualsize = jsonResponse.size();
        Assert.assertNotEquals(notexpectedzise,actualsize);
    }

    @Then("^I validate there is a value: (.*) for the response at (.*) endpoint$")
    public void validateValue(String expectedvalue, String endpoint){
        response = request
                   .when()
                   .get(endpoint);

                List<String> jsonResponse = response.jsonPath().getList("firstname");
                String actualValue = jsonResponse.get(0);
                assertEquals(actualValue, expectedvalue);
    }

    @When("^I send a POST on the (.*) endpoint values to register a new user: (.*), (.*), (\\d+), (.*), (.*), (.*) y (.*)$")
    public void SendValues(String endpoint, String firstname, String lastname, int totalprice, String depositpaid, String checkin, String checkout, String additionalneeds){
            String requestBody = "{\n" +
                    "    \"firstname\" : \"" + firstname + "\",\n" +
                    "    \"lastname\" : \"" + lastname + "\",\n" +
                    "    \"totalprice\" : " + totalprice + ",\n" +
                    "    \"depositpaid\" : " + depositpaid + ",\n" +
                    "    \"bookingdates\" : {\n" +
                    "        \"checkin\" : \"" + checkin + "\",\n" +
                    "        \"checkout\" : \"" + checkout + "\"\n" +
                    "    },\n" +
                    "    \"additionalneeds\" : \"" + additionalneeds + "\"\n" +
                    "}";
            response = request.when().header("Content-Type","application/json").header("Accept","application/json")
                    .body(requestBody).post(endpoint).prettyPeek();

            code = response.getStatusCode();

        BookingId = response.jsonPath().getInt("bookingid");

    }

    @Then("^I validate a (\\d+) status code$")
    public void ValidateStatus (int expectedcode){
        json = response.then().statusCode(expectedcode);
    }

     @And("^I validate the values are correct in the response (.*), (.*), (\\d+), (.*), (.*), (.*) y (.*)$")
     public void ValidateResult(String firstname, String lastname, int totalprice, String depositpaid, String checkin, String checkout, String additionalneeds){
         Assert.assertEquals(firstname,response.jsonPath().getString("booking.firstname"));
         Assert.assertEquals(lastname,response.jsonPath().getString("booking.lastname"));
         Assert.assertEquals(totalprice,response.jsonPath().getInt("booking.totalprice"));
         Assert.assertEquals(depositpaid, response.jsonPath().getString("booking.depositpaid"));
         Assert.assertEquals(checkin, response.jsonPath().getString("booking.bookingdates.checkin"));
         Assert.assertEquals(checkout, response.jsonPath().getString("booking.bookingdates.checkout"));
         Assert.assertEquals(additionalneeds, response.jsonPath().getString("booking.additionalneeds"));
     }

    @Then("^I send a GET on the endpoint finding the bookingId created to validate a (\\d+) status code$")
    public void validateSize(int expectedcode){
        response = request
                .when().header("Content-Type","application/json").header("Accept","application/json")
                .get("/booking/" + BookingId);
        json = response.then().statusCode(expectedcode);
    }

    @When("^I send a POST with the correct (.*) and (.*) to the (.*) endpoint$")
    public void CreatedToken(String user, String password, String endpoint){
        String requestbody = "{\n" +
                "    \"username\" : \"" + user + "\",\n" +
                "    \"password\" : \"" + password + "\"\n" +
                "}";
        response = request
                .when().header("Content-Type","application/json")
                .body(requestbody).post(endpoint).prettyPeek();

        Token = response.jsonPath().getString("token");
    }

    @And ("^I validate the error (.*)$")
    public void ValidateBadCredentials(String message){
        Assert.assertEquals(message,response.jsonPath().getString("reason"));
    }

    @When("^I send a PUT to the (.*) endpoint with the values i want update: (.*), (.*), (\\d+), (.*), (.*), (.*) y (.*)$")
    public void ValidateUpdate(String endpoint,String firstname, String lastname, int totalprice, String depositpaid, String checkin, String checkout, String additionalneeds){
        String requestBody = "{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "    \"lastname\" : \"" + lastname + "\",\n" +
                "    \"totalprice\" : " + totalprice + ",\n" +
                "    \"depositpaid\" : " + depositpaid + ",\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"" + checkin + "\",\n" +
                "        \"checkout\" : \"" + checkout + "\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"" + additionalneeds + "\"\n" +
                "}";

        response = request.when().log().all().header("Content-Type","application/json").header("Accept","application/json")
                .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=" )
                .body(requestBody).put(endpoint).prettyPeek();

        json = response.then().statusCode(200);
    }

    @And("^I validate the values in the response (.*), (.*), (\\d+), (.*), (.*), (.*) y (.*)$")
    public void ValidateResponse(String firstname, String lastname, int totalprice, String depositpaid, String checkin, String checkout, String additionalneeds){
        Assert.assertEquals(firstname,response.jsonPath().getString("firstname"));
        Assert.assertEquals(lastname,response.jsonPath().getString("lastname"));
        Assert.assertEquals(totalprice,response.jsonPath().getInt("totalprice"));
        Assert.assertEquals(depositpaid, response.jsonPath().getString("depositpaid"));
        Assert.assertEquals(checkin, response.jsonPath().getString("bookingdates.checkin"));
        Assert.assertEquals(checkout, response.jsonPath().getString("bookingdates.checkout"));
        Assert.assertEquals(additionalneeds, response.jsonPath().getString("additionalneeds"));
    }

    @When("^I send a DELETE to the (.*) endpoint$")
    public void ValidateDelete(String endpoint){
        response = request
                .when().header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .delete(endpoint);
    }

    @When("^I send a GET to the (.*) endpoint$")
    public void ValidatePing(String endpoint){
        response = request
                .when()
                .get(endpoint);
    }

}

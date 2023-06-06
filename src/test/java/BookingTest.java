import booking.details.BookingDates;
import booking.details.BookingDetails;
import booking.details.BookingDetailsBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Disabled;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


import static io.restassured.RestAssured.when;

public class BookingTest {

    private static String BOOKING_ENDPOINT = "/booking";
    private static String VALID_BOOKING_ID = "1";
    private static String NOT_EXISTING_BOOKING_ID = "a";

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @org.junit.Test
    public void successGetBookingsIdAll() {
        ResponseBody responseBody = when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .getBody();

        assertTrue(responseBody.jsonPath().getList("$").size() > 0);
        assertThat(responseBody.jsonPath().getList("$"), hasItem(hasKey("bookingid")));
    }

    @org.junit.Test
    public void successFilterByName() {
        ResponseBody responseBody = given()
                .queryParam("firstName", "sally")
                .queryParam("lastName", "brown")
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response().getBody();

        assertTrue(responseBody.jsonPath().getList("$").size() > 0);
    }

    @org.junit.Test
    public void negativeFilterByNotExistingName() {
        ResponseBody responseBody = given()
                .queryParam("firstname", "sally")
                .queryParam("lastname", "notexisting123")
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response().getBody();

        System.out.println(responseBody.prettyPrint());
        // No resource for given first and last name
        assertTrue(responseBody.jsonPath().getList("$").size() == 0);
    }

    @org.junit.Test
    public void successFilterByCheckinCheckoutDates() {
        ResponseBody responseBody = given()
                .queryParam("checkin", "2014-03-13")
                .queryParam("checkout", "2014-05-21")
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response().getBody();

        // No resource for given first and last name
        assertTrue(responseBody.jsonPath().getList("$").size() == 0);
    }

    @Disabled("There is no validation of date format, so it's a test to show as an example")
    @org.junit.Test
    public void negativeFilterCheckinCheckoutDatesInvalidFormat() {
        given()
                .queryParam("checkin", "12-01-2013")
                .queryParam("checkout", "2014-03-13")
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    // This test is failing as booking details with specific id is variable in time
    // In test environment I expect that this data will be constant or e.g. I'll do a query to DB to get the details
    @org.junit.Test
    public void successGetBookingById() {
        BookingDetails bD = new BookingDetailsBuilder()
                .setFirstname("Susan")
                .setLastname("Wilson")
                .setTotalprice(215)
                .setDepositpaid(true)
                .setBookingdates(new BookingDates("2019-07-19", "2022-03-07"))
                .setAdditionalneeds("Breakfast")
                .build();

        ResponseBody responseBody = when()
                .get(BOOKING_ENDPOINT + "/" + VALID_BOOKING_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().response().getBody();

        assertThat(responseBody.jsonPath().get("firstname"), equalTo(bD.getFirstname()));
        assertThat(responseBody.jsonPath().get("lastname"), equalTo(bD.getLastname()));
        assertThat(responseBody.jsonPath().get("totalprice"), equalTo(bD.getTotalprice()));
        assertThat(responseBody.jsonPath().get("depositpaid"), equalTo(bD.isDepositpaid()));
        assertThat(responseBody.jsonPath().get("bookingdates.checkin"), equalTo(bD.getBookingdates().getCheckin()));
        assertThat(responseBody.jsonPath().get("bookingdates.checkout"), equalTo(bD.getBookingdates().getCheckout()));
    }

    @org.junit.Test
    public void negativeGetBookingByIdNotExistingId() {
        when()
                .get(BOOKING_ENDPOINT + "/" + NOT_EXISTING_BOOKING_ID)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}

Feature: Validación de API Restfull Booking


    Scenario: Happy - Validation of getting bookingId.
        Given I declare the base URI https://restful-booker.herokuapp.com
        Then I send a GET request on the /booking endpoint to validate a 200 status code
        And I validate that the API show more than 1 record

    Scenario Outline: Happy - Validate a creation of new user.
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a POST on the /booking endpoint values to register a new user: <firstname>, <lastname>, <totalprice>, <depositpaid>, <checkin>, <checkout> y <additionalneeds>
        Then I validate a 200 status code
        And I validate the values are correct in the response <firstname>, <lastname>, <totalprice>, <depositpaid>, <checkin>, <checkout> y <additionalneeds>
        Examples:
            | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
            | Julio     | Poma     | 111        | true        | 2023-08-24 | 2024-01-01 | Lunch           |

    Scenario: Happy - Validation of getting bookindId created.
        Given I declare the base URI https://restful-booker.herokuapp.com
        Then I send a GET on the endpoint finding the bookingId created to validate a 200 status code


    Scenario Outline: Happy - Validation of obtein token.
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a POST with the correct <user> and <password> to the /auth endpoint
        Then I validate a 200 status code
        Examples:
        |  user  | password  |
        |admin   |password123|

    Scenario Outline: UnHappy - Validation of bad credential to obtein token.
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a POST with the correct <user> and <password> to the /auth endpoint
        Then I validate a 200 status code
        And I validate the error <message>
        Examples:
            |  user  | password  | message       |
            |admin2  |password178|Bad credentials|


    Scenario Outline: Happy - Validation of user update
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a PUT to the /booking/45 endpoint with the values i want update: <firstname>, <lastname>, <totalprice>, <depositpaid>, <checkin>, <checkout> y <additionalneeds>
        And I validate the values in the response <firstname>, <lastname>, <totalprice>, <depositpaid>, <checkin>, <checkout> y <additionalneeds>
        Examples:
            | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
            | Jisus     | Pere     | 123        | true        | 2023-08-30 | 2024-01-22 | Dinner          |


    Scenario: Happy - Validation of delete user
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a DELETE to the /booking/45 endpoint
        Then I validate a 201 status code
    @API
    Scenario: Happy - Validation of Ping-HealthCheck
        Given I declare the base URI https://restful-booker.herokuapp.com
        When I send a GET to the /ping endpoint
        Then I validate a 201 status code
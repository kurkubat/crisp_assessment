import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class patientInformationAPI {

    static String endpoint = "https://apimgmt-dev-crisp.azure-api.net/patients/query/";

    // Service Health Check
    @BeforeClass
    void healthCheck(){

        given().header("Accept","application/json")
                .when().get(endpoint)
                .then().statusCode(200)
                .log().ifValidationFails();

    }

    // Make a GET request for all Parameters and validate the payload.
    @Test(priority = 1)
    void getRequest(){

        // Validating the status code
        given().when().get(endpoint).then().assertThat().statusCode(HttpStatus.SC_OK);

            // Rest implementations to get Response from Json body as String
            String json = given().header("Content-Type", "application/json")
                    .when().get(endpoint)
                    .asString();

            // Validating the response payload data
        String id = JsonPath.read(json, ".Id").toString();
        id = id.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");
        String [] idValues = id.split(",");

        Assert.assertEquals(idValues[0],"111");
        Assert.assertEquals(idValues[1],"222");
        Assert.assertEquals(idValues[2],"333");
        Assert.assertEquals(idValues[3],"444");

        String name = JsonPath.read(json, ".Name").toString();
        name = name.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");
        String [] nameValues = name.split(",");

        Assert.assertEquals(nameValues[0],"Jenn D.");
        Assert.assertEquals(nameValues[1],"Jack");
        Assert.assertEquals(nameValues[2],"Bernard");
        Assert.assertEquals(nameValues[3],"Ross C.");

        String dateOfBirth = JsonPath.read(json, ".DateOfBirth").toString();
        dateOfBirth = dateOfBirth.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");
        String [] dobValues = dateOfBirth.split(",");

        Assert.assertEquals(dobValues[0],"1934-06-01");
        Assert.assertEquals(dobValues[1],"1956-05-01");
        Assert.assertEquals(dobValues[2],"1966-04-01");
        Assert.assertEquals(dobValues[3],"1976-03-01");

        String address = JsonPath.read(json, ".Address").toString();
        address = address.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");
        String [] addressValues = address.split(",");

        Assert.assertEquals(addressValues[0],"CA");
        Assert.assertEquals(addressValues[1],"MD");
        Assert.assertEquals(addressValues[2],"CA State");
        Assert.assertEquals(addressValues[3],"Valley State");

    }
    // Make a GET request for one query Parameter and validate the payload.
    @Test(priority = 2)
    void getRequestWithQuery(){

        // Validating the status code
        given().when().get(endpoint).then().assertThat().statusCode(HttpStatus.SC_OK);

        // Rest implementations to get Response from Json body as String
        String json = given().queryParam("Id","111").header("Content-Type", "application/json")
                .when().get(endpoint)
                .asString();

        // Validating the response payload data
        String value = JsonPath.read(json, ".Name").toString();
        value = value.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");

        Assert.assertEquals(value,"Jenn D.");

    }
    // Make a GET request with a specific name with XML content and validate the payload.
    @Test(priority = 3)
    void getRequestWithQueryAsXML(){

        // Validating the status code
        given().when().get(endpoint).then().assertThat().statusCode(HttpStatus.SC_OK);

        // Rest implementations to get Response from Json body as String
        String json = given().queryParam("Name","Jack").header("Content-Type", "application/xml")
                .when().get(endpoint)
                .asString();

        // Validating the response payload data
        String value = JsonPath.read(json, ".Id").toString();
        value = value.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");

        Assert.assertEquals(value,"222");

    }
    // Make a GET request with a DateOfBirth that is not listed in the query and validate the payload.
    @Test(priority = 4)
    void getRequestWithData(){

        // Validating the status code
        given().queryParam("DateOfBirth","1920").header("Content-Type", "application/xml")
                .when().get(endpoint).then().assertThat().statusCode(HttpStatus.SC_OK);

        // Rest implementations to get Response from Json body as String
        String json = given().queryParam("DateOfBirth","1920").header("Content-Type", "application/xml")
                .when().get(endpoint)
                .asString();

        // Validating the response payload data
        json = json.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","");

        Assert.assertEquals(json,"");

    }

    // Make a POST request without a body for all Parameters and validate the payload.
    @Test(priority = 5)
    void postRequestWithoutBody(){

        // Validating the status code
        given().when().post(endpoint).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);

        // Rest implementations to get Response from Json body as String
        String json = given().queryParam("DateOfBirth","1920").header("Content-Type", "application/xml")
                .when().post(endpoint)
                .asString();

        // Validating the response payload data
        json = json.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\{", "").replaceAll("\\}","").trim();

        Assert.assertEquals(json,"statusCode: 404, message: Resource not found");

    }

    @Test(priority = 6)
    void putRequestWithBody(){

        // Rest implementations to make a put request with body
        JSONObject requestParams = new JSONObject();
        requestParams.put("Id", "555");
        requestParams.put("Name", "John Doe");
        requestParams.put("DateOfBirth", "1986-02-01");
        requestParams.put("Address", "NY");


        given().body(requestParams.toString())
                .contentType("application/json")
                .when().put(endpoint).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);

    }
}

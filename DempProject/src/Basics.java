import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Validate if Add Place is working as expected
		
		//given-all input details--base URL,QUERY PARAM
		//when-submit api--Resource and HTTP Method
		//then-validate the response
		
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response=given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json").body(new String(Files.readAllBytes(Paths.get("C:\\WORK\\AMPION\\googlepath.json")))).when().post("/maps/api/place/add/json").then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.extract().response().asString();
		System.out.println(response);
		JsonPath js=new JsonPath(response);//for parsing json
		String placeId=js.getString("place_id");
		System.out.println(placeId);
		
		//update place
		String newAddress= "Summer Walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "").when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//get Place
		
		String getPlaceResponse= given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId).when().get("/maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
	 JsonPath js1=ReusableMethods.rawToJason(getPlaceResponse);
		String actualAddress=js1.getString("address");
		System.out.println(actualAddress); 
		Assert.assertEquals(actualAddress,newAddress );

		
	}

}

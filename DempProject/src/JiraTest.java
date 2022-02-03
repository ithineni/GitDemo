import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI="http://localhost:8080";
		//Login scenario
		SessionFilter session= new SessionFilter();
		String response=given().relaxedHTTPSValidation().header("Content-Type","application/json").
		body("{\r\n"
				+ "     \"username\": \"ithineni\", \r\n"
				+ "     \"password\": \"Protected1\" \r\n"
				+ "  }")
		.log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
    String expectedMessage="Hi How are you";
		//Add comment
		String addCommentResponse=given().pathParam("id", "10004").log().all().header("Content-Type","application/json").body("{\r\n"
		+ "    \"body\": \""+expectedMessage+"\",\r\n"
		+ "    \"visibility\": {\r\n"
		+ "        \"type\": \"role\",\r\n"
		+ "        \"value\": \"Administrators\"\r\n"
		+ "    }\r\n"
		+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js=new JsonPath(addCommentResponse);
		String commentId=js.getString("id");
	//Add attachment
		/*given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id","10004").
		header("Content-Type","multipart/form-data").
		multiPart("key",new File("jira.text")).when().post("/rest/api/2/issue/{id}/attachments").then().
		log().all().assertThat().statusCode(200);*/
		
		//GET Issue
		String response1=given().filter(session).pathParam("id", "10004").queryParam("fields", "comment").
				when().
				get("/rest/api/2/issue/{id}").then().log().all().extract().response().asString();
		System.out.println(response1);
		JsonPath js1= new JsonPath(response1);
		int commentsCount=js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) {
			String commentIdIssue=js1.get("fields.comment.comments["+i+"].id");
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String message=js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
			
		}
		
		
	}
	

}

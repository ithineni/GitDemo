package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	
	public void addBook(String isbn,String isle) {
		RestAssured.baseURI="http://216.10.245.166";
		String response=given().header("Content-Type", "application/json").body(payload.Addbook(isbn,isle)).
		when()
		.post("Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js=ReusableMethods.rawToJason(response);
		String id= js.get("ID");
		System.out.println(id);
		
		}
	//deleteBook
			@Test(dataProvider="BooksData")
			public void deleteBook(String isbn,String isle) {
				RestAssured.baseURI="http://216.10.245.166";
				String response=given().body(payload.Deletebook(isbn, isle)).
				when()
				.post("/Library/DeleteBook.php")
				.then().assertThat().statusCode(200)
				.extract().response().asString();
				JsonPath js=ReusableMethods.rawToJason(response);
				String msg=js.get("msg");
				System.out.println(msg);
		
	}
	
@DataProvider(name="BooksData")
public Object[][] getData() {
	//ARRAY--Collection of elements
	//create multi dimensional array--Collection of arrays
	return new Object[][] {{"oitfy","9363"},{"zdyft","6789"},{"tydfn","7896"}};
}
}

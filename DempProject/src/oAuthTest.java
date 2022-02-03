
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;
public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String[] courseTitles= {"Selenium Webdriver Java", "Protractor"};
		System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
		WebDriver driver= new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth/identifier?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&state=verifydfsj&flowName=GeneralOAuthFlow");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("ithineni");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Protected1");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);
		String url=driver.getCurrentUrl();
		//String url="https://rahulshettyacademy.com/getCourse.php?state=verifydfsj&code=4%2F0AX4XfWhJhS2_tApLP8wzr0F7MHpfS2p4jfTLUrrfeg57JgqDHDn7tQt4mjjfm8hkpb1uGg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent#";
		
		//splitting the url to get the code
		String partialCode=url.split("code=")[1];
		String code=partialCode.split("&scope")[0];
		System.out.println(code);
		
		//tagname[attribute='value']-- css path
		String accessTokenResponse= given().urlEncodingEnabled(false)
				.queryParams("code",code)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type", "authorizartion_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken=js.getString("access_token");
	//Actual Request
 GetCourse gc= given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
  .when()
  .get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
 System.out.println(gc.getLinkedIn());
 System.out.println(gc.getInstructor());
 
 System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
 List<Api> apiCourses=gc.getCourses().getApi();
 for(int i=0;i<apiCourses.size();i++)
 {
	 if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI webservices testing"))
	 {
		 System.out.println(apiCourses.get(i).getPrice());
	 }
 }
 //PRINT ALL COURSEs TITLES OF WEB AUTOMATION
 //Get courses of web automation
 ArrayList<String> a= new ArrayList<String>();
 
 List<WebAutomation> webAutomationCourses=gc.getCourses().getWebAutomation();
 for(int j=0;j<webAutomationCourses.size();j++) {
	 a.add(webAutomationCourses.get(j).getCourseTitle());
 }
 //Converting Array into arralyList
 List<String> expectedList=Arrays.asList(courseTitles);
 Assert.assertTrue(a.equals(expectedList));
 //System.out.println(response);
	}

}

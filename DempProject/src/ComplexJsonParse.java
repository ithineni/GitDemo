import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js= new JsonPath(payload.CoursePrice());
		//Print number of courses
       int  count=js.getInt("courses.size()");
       System.out.println(count);
       //Print toal purchase amount
      int totalAmount =js.getInt("dashboard.purchaseAmount");
      System.out.println(totalAmount);
      //Print title of the second couse
      String title=js.get("courses[1].title");
      System.out.println(title);
      //Print all course titles and respective prices
      for(int i=0;i<count;i++) {
       	  System.out.println(js.get("courses["+i+"].title").toString());
    	  System.out.println(js.get("courses["+i+"].price").toString());
      }
      System.out.println("Print number of courses sold by Appium");
      for(int i=0;i<count;i++) {
       	  String courseTitles=js.get("courses["+i+"].title").toString();
       	  if(courseTitles.equalsIgnoreCase("Appium")) {
       		int copies=js.get("courses["+i+"].copies");
       		System.out.println(copies);
       		break;
       	  }
       	  
	}

	}
}

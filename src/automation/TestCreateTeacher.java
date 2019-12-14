package automation;

import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.Constant;
import utils.ExcelUtils;

public class TestCreateTeacher {
	
	public String baseUrl = Constant.URL;
    String driverPath = Constant.Path_Drive_Gecko;
    public ChromeDriver driver ; 
    
    WebElement firstName;
    WebElement lastName;
    WebElement email;
    WebElement phone;
    
    String strFirstName = "Nguyen";
    String strLastName = "Duc Huy";
    String strEmail = "emailgv@gmail.com";
    String strPhone = "123123";
    String strExpectedResult = "Success";
    
    String actualResult = "";
    
    int index = 1;
    
	@BeforeTest
    public void launchBrowser() throws Exception {
		ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData , "Sheet2");
        System.out.println("launching firefox browser");
     
        System.setProperty("webdriver.chrome.driver", Constant.Path_Drive_Chrome);
        
        //Initialize browser
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        
        String expectedTitle = "Attendance Management";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
        
        //Login by Staff: Enter UserName & Password
	    driver.findElement(By.name("username")).sendKeys(Constant.Username);
	    driver.findElement(By.name("password")).sendKeys(Constant.Password);
	    //Wait For Page To Load
	    driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	    //Click on 'Sign In' button
	    driver.findElement(By.className("btn")).click();
	    
	   //Click on "Teachers"
	    driver.findElement(By.linkText("Teachers")).click();
	    
    }
	@Test(priority = 1)
	public void createTeacher() throws Exception {
		  XSSFSheet sheet = ExcelUtils.getSheetName("Sheet2");
		  
		  int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		  
		//Create a loop over all the rows of excel file to read it
		    for (int i = 1; i < rowCount; i++) {
		        Row row = sheet.getRow(i);
		        //Create a loop to print cell values in a row
		        for (int j = 0; j < row.getLastCellNum(); j++) {
		        	
		        	String str = ExcelUtils.getCellData(i,  j);
		        	System.out.println(str);
		        	
		        	switch(j){
	        		case 0:
	        			strFirstName = str;
	        			break;
	        		case 1:
	        			strLastName = str;
	        			break;
	        		case 2:
	        			strEmail = str;
	        			break;
	        		case 3:
	        			strPhone = str;
	        			strPhone = strPhone.substring(1, strPhone.length()-1);
	        			break;
	        		case 4:
	        			strExpectedResult = str;
	        			break;
	        		default: break;
	        			
		        	}
		
		        }
		        boolean isSuccess = runCreateTeacherFlow(strFirstName, strLastName, strEmail, strPhone, strExpectedResult);
		        //Write Actual Result into Excel file
		        ExcelUtils.setCellData(actualResult, i, 5);
		        if(!isSuccess) {
		        	System.out.println("Add a Teacher Success");
		        }
		      
		     }
		        
		} 
		
	private boolean runCreateTeacherFlow(String strFirstName, String strLastName, String strEmail, String strPhone,
				String strExpectedResult) throws InterruptedException {
		Thread.sleep(2000);
		//Find lElements Form Create Teacher
	    firstName = driver.findElement(By.name("first_name"));
	    lastName = driver.findElement(By.name("last_name"));
	    email = driver.findElement(By.name("email"));
	    phone = driver.findElement(By.name("phone")); 
	    
	    //Click btn Add Teacher
	    driver.findElement(By.className("btn-round")).click();
		
		firstName.clear();
		lastName.clear();
		email.clear();
		phone.clear();
		
		//Set Data to inputText
		firstName.sendKeys(strFirstName);
		lastName.sendKeys(strLastName);
		email.sendKeys(strEmail);
		phone.sendKeys(strPhone);
		
		//Click on "Add" <- Done
	    driver.findElement(By.cssSelector("button[class='btn btn-default btn-success']")).click();
	    
	    //Close
	    driver.findElement(By.cssSelector("button[class='close']")).click();
	    
	    //Check TC pass|fail
	    String title = driver.findElement(By.xpath("//div[contains(@class,'ui-pnotify-container')]/h4")).getText();
	    String content = driver.findElement(By.className("ui-pnotify-text")).getText();
	    
	    
	    actualResult = title + "-" + content;
	  
	    if(actualResult.equalsIgnoreCase(strExpectedResult)) {
	    	return true;
	    }
			return false;
}

	@AfterTest
	public void terminateBrowser(){
	   driver.quit();
	}


//		//Find Elements Form Create Teacher
//	    firstName = driver.findElement(By.name("first_name"));
//	    lastName = driver.findElement(By.name("last_name"));
//	    email = driver.findElement(By.name("email"));
//	    phone = driver.findElement(By.name("phone"));  
//		
//		strFirstName = ExcelUtils.getCellData(index, 1);
//		strLastName = ExcelUtils.getCellData(index, 2);
//		strEmail = ExcelUtils.getCellData(index, 3);
//		
//		strPhone = ExcelUtils.getCellData(index, 4);
//		if(!strPhone.isEmpty()) {
//			strPhone = strPhone.substring(1, strPhone.length()-1);
//		}
//		
//		//Click btn Add Teacher
//		driver.findElement(By.className("btn-round")).click();
//		
//		firstName.clear();
//		lastName.clear();
//		email.clear();
//		phone.clear();
//		
//		//Set Data to inputText
//		firstName.sendKeys(strFirstName);
//		lastName.sendKeys(strLastName);
//		email.sendKeys(strEmail);
//		phone.sendKeys(strPhone);
//		
//		//Click on "Add" <- Done
//	    driver.findElement(By.cssSelector("button[class='btn btn-default btn-success']")).click();
//	    
//	    //Close
//	    driver.findElement(By.cssSelector("button[class='close']")).click();
//	    
//	    //Check TC pass|fail
//	    String title = driver.findElement(By.xpath("//div[contains(@class,'ui-pnotify-container')]/h4")).getText();
//	    String content = driver.findElement(By.className("ui-pnotify-text")).getText();
//	    
//	    
//	    String actualResult = title + "-" + content;
//	    //String expectedResult = "success-Teacher Added Successfully";
//	    String expectedResult = ExcelUtils.getCellData(index, 5);
//	    System.out.println(expectedResult);
//	       
//	    Assert.assertEquals(actualResult, expectedResult);
//	    
//	    index = index + 1;
	
	////////////
//	@Test(priority = 2)
//	public void createTeacher02() throws Exception {
//		
//		
//		strFirstName = ExcelUtils.getCellData(2, 1);
//		strLastName = ExcelUtils.getCellData(2, 2);
//		strEmail = ExcelUtils.getCellData(2, 3);
//		strPhone = ExcelUtils.getCellData(2, 4);
//		if(!strPhone.isEmpty()) {
//			strPhone = strPhone.substring(1, strPhone.length()-1);
//		}
//		
//		Thread.sleep(2000);
//		//Click btn Add Teacher
//		driver.findElement(By.className("btn-round")).click();
//		
//		firstName.clear();
//		lastName.clear();
//		email.clear();
//		phone.clear();
//		
//		//Set Data to inputText
//		firstName.sendKeys(strFirstName);
//		lastName.sendKeys(strLastName);
//		email.sendKeys(strEmail);
//		phone.sendKeys(strPhone);
//		
//		//Click on "Add" <- Done
//	    driver.findElement(By.cssSelector("button[class='btn btn-default btn-success']")).click();
//	    
//	    //Close
//	    driver.findElement(By.cssSelector("button[class='close']")).click();
//	    
//	    //Check TC pass|fail
//	    String title = driver.findElement(By.xpath("//div[contains(@class,'ui-pnotify-container')]/h4")).getText();
//	    String content = driver.findElement(By.className("ui-pnotify-text")).getText();
//	    
////	    System.out.println(title);
////	    System.out.println(content);
//	    
//	    String actualResult = title + "-" + content;
//	    
//	    String expectedResult = ExcelUtils.getCellData(2, 5);
//	    System.out.println(expectedResult);
//	       
//	    Assert.assertEquals(actualResult, expectedResult);
//	    
//	    index = index + 1;
//	}
	
}

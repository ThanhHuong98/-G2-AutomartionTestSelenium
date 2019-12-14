package automation;

import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.ExcelUtils;
import utils.Constant;



public class TestCreateClass {
	
	public String baseUrl = Constant.URL;
    String driverPath = Constant.Path_Drive_Gecko;
    public ChromeDriver driver ; 
    
    String className = "21CTT";
    String programName = "Việt Pháp";
    double programIndex = 1;
    String filePath = "";
    
    WebElement newClassName;
    WebElement uploadElement;
    
    String actualResult = "";
    String expectedResult = "Success";
    
    @BeforeTest
    public void launchBrowser() throws Exception {
        System.out.println("launching firefox browser");
        
        
        System.setProperty("webdriver.chrome.driver", Constant.Path_Drive_Chrome);
        
        //Initialize browser
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        driver.get(baseUrl);
        
        String expectedTitle = "Attendance Management";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
//        
//        //Login by Staff: Enter UserName & Password
	    driver.findElement(By.name("username")).sendKeys(Constant.Username);
	    driver.findElement(By.name("password")).sendKeys(Constant.Password);
	    //Wait For Page To Load
	    driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//	    //Click on 'Sign In' button
	    driver.findElement(By.className("btn")).click();
//	   //Click on "Classes"
        driver.findElement(By.linkText("Classes")).click();
        
      
        
    }
    
    @Test(priority = 1)
    public void createClass() throws Exception {
    	ExcelUtils.setExcelFile(Constant.Path_TestData + Constant.File_TestData , "Sheet3");
    	XSSFSheet sheet = ExcelUtils.getSheetName("Sheet3");
		  
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
	    for (int i = 1; i < rowCount; i++) {
	        Row row = sheet.getRow(i);
	        //Create a loop to print cell values in a row
	        for (int j = 0; j < row.getLastCellNum(); j++) {
	        	
	        	String str = ExcelUtils.getCellData(i,  j);
	        	System.out.println(str);
	        	
	        	switch(j){
        		case 0:
        			className = str;
        			break;
        		case 1:
        			programName = str;
        			break;
        		case 2:
        			programIndex = ExcelUtils.getCellDataNumber(i,  j);
        			System.out.println(programIndex);
        			break;
        		case 3:
        			filePath = str;
        			break;
        		case 4:
        			expectedResult= str;
        			break;
        		default: break;
        			
	        	}
	
	        }
	        
	        boolean isSuccess = runCreateClassFlow(className, programName, programIndex, filePath, expectedResult);
	        //Write Actual Result into Excel file
	        ExcelUtils.setCellData(actualResult, i, 5);
	        if(isSuccess) {
	        	System.out.println("Add a Teacher Success");
	        }
	       
	     }
	        
    }
    
    public boolean runCreateClassFlow(String className, String programName, double programIndex, String filePath, String expectedResult) throws InterruptedException {
    	Thread.sleep(2000);
    	
    	//Click btn Add Class
        driver.findElement(By.className("btn-round")).click();
        //Element in Form CreateClass
        newClassName = driver.findElement(By.name("new_class_name"));
        uploadElement = driver.findElement(By.name("students"));
        
        //Enter NewClassName
 	   newClassName.clear();
       newClassName.sendKeys(className);  
        
         //Select Option Dropdown Program
 		WebElement programSelect = driver.findElement(By.name("program"));
         Select programs = new Select(programSelect);
 		programs.selectByVisibleText(programName);
 		programs.selectByIndex((int)programIndex);
        
        //Upload file; /Users/admin/Downloads/student.xlsx"
        uploadElement.sendKeys(filePath);
        
        //Click on "Add" <- Done
        driver.findElement(By.cssSelector("button[class='btn btn-default btn-success']")).click();
        
//        actualResult = driver.findElement(By.xpath("//div[contains(@class,'ui-pnotify-container')]/h4")).getText();
//        System.out.println(actualResult);
        //Check TC pass|fail
	    String title = driver.findElement(By.xpath("//div[contains(@class,'ui-pnotify-container')]/h4")).getText();
	    String content = driver.findElement(By.className("ui-pnotify-text")).getText();
	    System.out.println(actualResult);
	    
	    actualResult = title + "-" + content;
	  
        if(actualResult.equalsIgnoreCase(expectedResult)) {
	    	return true;
	    }
			return false;
    	
    }
    
    
    
    
    @AfterTest
    public void terminateBrowser(){
       driver.quit();
    }
}

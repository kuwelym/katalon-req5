import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory

// Verify the user is able to navigate to My Account page
// Open the website
WebUI.openBrowser('')
WebUI.navigateToUrl('http://127.0.0.1:4200/#')
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver();
// Login first 
WebElement loginNav = driver.findElement(By.xpath("//a[@href='#/auth/login']"));
loginNav.click();

WebUI.waitForElementVisible(findTestObject('Login/Email Field'), 5)
WebElement emailField = driver.findElement(By.xpath("//input[@type='text']"));
emailField.sendKeys("customer@practicesoftwaretesting.com");

WebUI.waitForElementVisible(findTestObject('Login/Password Field'), 5)
WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
passwordField.sendKeys("welcome01");

WebElement submitButton = driver.findElement(By.className("btnSubmit"));
submitButton.click();
	
WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 5)

// Navigate to My Account page
WebElement userMenu = driver.findElement(By.id("user-menu"));
userMenu.click();
WebElement myAccount = driver.findElement(By.xpath("//a[@href='#/account']"));
myAccount.click();

WebElement myAccountTitle = WebUI.findWebElement(findTestObject('My Account Title'))

assert myAccountTitle.isDisplayed() == true

WebUI.closeBrowser()
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
import java.sql.*;
import com.kuwelym.keywords.LoginHelper
import internal.GlobalVariable as GlobalVariable

TestData testData = findTestData('Data Files/Change Password Data')

// Verify the user is able to navigate to My Account page
// Open the website
WebUI.openBrowser('')
WebUI.navigateToUrl('http://127.0.0.1:4200/#')
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver();


// Login first
LoginHelper.forgetPassword(email)
LoginHelper.login(email, "welcome02")

WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 3)

// Navigate to My Profile page
WebElement userMenu = driver.findElement(By.id("user-menu"))
userMenu.click()
WebElement myAccount = driver.findElement(By.xpath("//a[@href='#/account/profile']"))
myAccount.click()

WebElement myAccountTitle = WebUI.findWebElement(findTestObject('My Profile Title'))

assert myAccountTitle.isDisplayed() == true


WebUI.setText(findTestObject('Change Password/Password Field'), current_password)
WebUI.setText(findTestObject('Change Password/New Password Field'), new_password)
WebUI.setText(findTestObject('Change Password/Confirm New Password Field'), confirm_new_password)

WebElement changePasswordButton = WebUI.findWebElement(findTestObject('Change Password/Change Password Button'))
changePasswordButton.click()

WebElement message = driver.findElement(By.xpath("//div[@role='alert']"))

// check if the message contains the expected text

assert message.getText().contains(expected_result) == true

if(expected_result == "successfully") {
	LoginHelper.login(email, new_password)

    boolean isLoggedIn = WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 3, FailureHandling.OPTIONAL)
    
    assert isLoggedIn == true
}
// Close the browser
WebUI.closeBrowser()
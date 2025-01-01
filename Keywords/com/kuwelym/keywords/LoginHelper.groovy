package com.kuwelym.keywords
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import java.sql.*;

class LoginHelper {
	@Keyword
	static void login(String email, String password) {

		// Refresh the failed login attempts
		refreshFailedLoginAttempts(email)

		WebDriver driver = DriverFactory.getWebDriver()

		WebUI.waitForElementVisible(findTestObject('Login/Sign In Link'), 10)
		WebElement loginNav = driver.findElement(By.xpath("//a[@href='#/auth/login']"))
		loginNav.click()
		WebUI.waitForElementVisible(findTestObject('Login/Email Field'), 5)
		WebElement emailField = driver.findElement(By.xpath("//input[@type='text']"))
		emailField.sendKeys(email)

		WebUI.waitForElementVisible(findTestObject('Login/Password Field'), 5)
		WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"))
		passwordField.sendKeys(password)

		WebElement submitButton = driver.findElement(By.className("btnSubmit"))
		submitButton.click()
	}

	@Keyword
	static void refreshFailedLoginAttempts(String email) {
		String url = "jdbc:mariadb://localhost:3306/toolshop";
		String username = "root";
		String dbPassword = "root";
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, username, dbPassword);

		// UPDATE the failed_login_attempts to 0
		def query = "UPDATE users SET failed_login_attempts = 0 WHERE email = '${email}'"
		Statement statement = conn.createStatement();
		statement.executeUpdate(query);

		statement.close();
		conn.close();
	}

	@Keyword
	static void forgetPassword(String email) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement loginNav = driver.findElement(By.xpath("//a[@href='#/auth/login']"))
		loginNav.click()

		WebElement forgetPasswordNav = driver.findElement(By.xpath("//a[@href='#/auth/forgot-password']"))
		forgetPasswordNav.click()

		WebUI.waitForElementVisible(findTestObject('Login/Email Field'), 5)
		WebElement emailField = driver.findElement(By.xpath("//input[@type='text']"))
		emailField.sendKeys(email)

		WebElement submitButton = driver.findElement(By.xpath("//input[@data-test='forgot-password-submit']"))
		submitButton.click()
	}
}
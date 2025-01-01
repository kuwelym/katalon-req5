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

// Verify the user is able to navigate to My Account page
// Open the website
WebUI.openBrowser('')
WebUI.navigateToUrl('http://127.0.0.1:4200/#')
WebUI.maximizeWindow()

WebDriver driver = DriverFactory.getWebDriver();
// Login first
LoginHelper.forgetPassword("customer2@practicesoftwaretesting.com")
LoginHelper.login("customer2@practicesoftwaretesting.com", "welcome02")
	
WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 5)

// Navigate to My Account page
WebElement userMenu = driver.findElement(By.id("user-menu"));
userMenu.click();
WebElement myAccount = driver.findElement(By.xpath("//a[@href='#/account/invoices']"));
myAccount.click();

WebElement myInvoiceTitle = WebUI.findWebElement(findTestObject('My Invoices Title'))

assert myInvoiceTitle.isDisplayed() == true

WebElement invoiceRow = driver.findElement(By.xpath("//tr[td[text()='INV-2022000004']]"))
WebElement detailsLink = invoiceRow.findElement(By.xpath(".//a[contains(@href, '#/account/invoices/10')]"))
detailsLink.click()

String url = "jdbc:mariadb://localhost:3306/toolshop";
String username = "root";
String password = "root";
Class.forName("org.mariadb.jdbc.Driver"); // Or appropriate driver class
Connection conn = DriverManager.getConnection(url, username, password);

// Get INV-2022000004 invoice details
String query = "SELECT invoice_number, invoice_date, total, payment_method, payment_account_name, payment_account_number, billing_address, billing_postcode, billing_city, billing_state, billing_country FROM invoices WHERE invoice_number = 'INV-2022000004'"
Statement statement = conn.createStatement()
def result = statement.executeQuery(query)

if (result.next()) {
    String invoiceNumber = driver.findElement(By.id("invoice_number")).getAttribute("value")
    String invoiceDate = driver.findElement(By.id("invoice_date")).getAttribute("value")
    String total = driver.findElement(By.id("total")).getAttribute("value").replace('$', '').trim()
    String paymentMethod = driver.findElement(By.id("payment_method")).getAttribute("value")
    String accountName = driver.findElement(By.id("account_name")).getAttribute("value")
    String accountNumber = driver.findElement(By.id("account_number")).getAttribute("value")
    String address = driver.findElement(By.id("address")).getAttribute("value")
    String postcode = driver.findElement(By.id("postcode")).getAttribute("value")
    String city = driver.findElement(By.id("city")).getAttribute("value")
    String state = driver.findElement(By.id("state")).getAttribute("value")
    String country = driver.findElement(By.id("country")).getAttribute("value")

    // Verify the invoice details match the database
    String dbInvoiceNumber = result.getString("invoice_number")
    String dbInvoiceDate = result.getString("invoice_date")
    String dbTotal = result.getFloat("total").toString()
    String dbPaymentMethod = result.getString("payment_method")
    String dbAccountName = result.getString("payment_account_name")
    String dbAccountNumber = result.getString("payment_account_number")
    String dbAddress = result.getString("billing_address")
    String dbPostcode = result.getString("billing_postcode")
    String dbCity = result.getString("billing_city")
    String dbState = result.getString("billing_state")
    String dbCountry = result.getString("billing_country")

    assert invoiceNumber == dbInvoiceNumber
    assert invoiceDate == dbInvoiceDate
    assert total == dbTotal
    assert paymentMethod == dbPaymentMethod
    assert accountName == dbAccountName
    assert accountNumber == dbAccountNumber
    assert address == dbAddress
    assert postcode == dbPostcode
    assert city == dbCity
    assert state == dbState
    assert country == dbCountry
}

// Get the products in the invoice in the products table join with invoice_details(product_id)
query = "SELECT quantity, product_name, unit_price, total_price FROM invoice_details JOIN products ON invoice_details.product_id = products.id WHERE invoice_id = (SELECT id FROM invoices WHERE invoice_number = 'INV-2022000004')"
List<WebElement> productRows = driver.findElements(By.xpath("//tbody/tr"))
while (result.next()) {
    for (int i = 0; i < productRows.size(); i++) {
        WebElement row = productRows.get(i)
        String quantity = row.findElement(By.xpath("td[1]")).getText().trim()
        String productName = row.findElement(By.xpath("td[2]")).getText().trim()
        String unitPrice = row.findElement(By.xpath("td[3]")).getText().replace('$', '').trim()
        String totalPrice = row.findElement(By.xpath("td[4]")).getText().replace('$', '').trim()

        String dbQuantity = result.getString("quantity")
        String dbProductName = result.getString("product_name")
        String dbUnitPrice = result.getFloat("unit_price").toString()
        String dbTotalPrice = result.getFloat("total_price").toString()

        assert quantity == dbQuantity
        assert productName == dbProductName
        assert unitPrice == dbUnitPrice
        assert totalPrice == dbTotalPrice

        result.next()
    }
}

result.close();
statement.close();
conn.close();

WebUI.closeBrowser()
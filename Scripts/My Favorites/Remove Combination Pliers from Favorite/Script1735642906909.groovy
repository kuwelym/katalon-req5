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
import org.openqa.selenium.Keys
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import java.sql.*;

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
// Retry mechanism for emailField
int retryCount = 0;
int maxRetries = 3;
WebElement emailField = null;

while (retryCount < maxRetries) {
    try {
        emailField = driver.findElement(By.xpath("//input[@type='text']"));
        emailField.sendKeys("klyminh1@gmail.com");
        break; // Exit loop if successful
    } catch (StaleElementReferenceException e) {
        retryCount++;
        Thread.sleep(1000); // Wait for a second before retrying
    }
}

if (emailField == null) {
    throw new RuntimeException("Failed to locate email field after " + maxRetries + " retries");
}

WebUI.waitForElementVisible(findTestObject('Login/Password Field'), 5)

// Retry mechanism for passwordField
retryCount = 0;
WebElement passwordField = null;

while (retryCount < maxRetries) {
    try {
        passwordField = driver.findElement(By.xpath("//input[@type='password']"));
        passwordField.sendKeys("welcome02");
        break; // Exit loop if successful
    } catch (StaleElementReferenceException e) {
        retryCount++;
        Thread.sleep(1000); // Wait for a second before retrying
    }
}

if (passwordField == null) {
    throw new RuntimeException("Failed to locate password field after " + maxRetries + " retries");
}
WebElement submitButton = driver.findElement(By.className("btnSubmit"));
submitButton.click();
	
WebUI.waitForElementVisible(findTestObject('Page_Home MyAccount'), 5)

String url = "jdbc:mariadb://localhost:3306/toolshop";
String username = "root";
String password = "root";
Class.forName("org.mariadb.jdbc.Driver"); // Or appropriate driver class
Connection conn = DriverManager.getConnection(url, username, password);

// Get the favorite products count for the user using the user id getting from the user email
def query = "SELECT COUNT(*) as favorite_count FROM favorites WHERE user_id = (SELECT id FROM users WHERE email = 'klyminh1@gmail.com')"
Statement statement = conn.createStatement()
def result = statement.executeQuery(query)

// Verify if the user has favorite products
result.next()

def favoriteCountFromDb = result.getInt("favorite_count")

result.close();
statement.close();
conn.close();


// Navigate to My Account page
WebElement userMenu = driver.findElement(By.id("user-menu"));
userMenu.click();
WebElement myFavorites = driver.findElement(By.xpath("//a[@href='#/account/favorites']"));
myFavorites.click();

List<WebElement> favoriteProducts = driver.findElements(By.cssSelector("[data-test^='favorite-']"));

WebElement combinationPliersCard = favoriteProducts.find { product ->
    WebElement productName = product.findElement(By.cssSelector("[data-test='product-name']"))
    return productName.getText() == "Combination Pliers"
}

if (combinationPliersCard != null) {
    // Find the delete button within the specific card and click it
    WebElement deleteButton = combinationPliersCard.findElement(By.cssSelector("button[data-test='delete']"))
    deleteButton.click()

} else {
    throw new RuntimeException("Combination Pliers card not found in favorites")
}

favoriteProducts = driver.findElements(By.cssSelector("[data-test^='favorite-']"));

// Verify the favorite count is decreased by 1
assert favoriteProducts.size() == favoriteCountFromDb - 1

// Verify the Combination Pliers card is not present
assert favoriteProducts.find { product ->
    WebElement productName = product.findElement(By.cssSelector("[data-test='product-name']"))
    return productName.getText() == "Combination Pliers"
} == null

// Close the browser
WebUI.closeBrowser()

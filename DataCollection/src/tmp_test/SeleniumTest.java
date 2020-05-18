package tmp_test;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {
	public static void main(String[] args) {
		// comment the above 2 lines and uncomment below 2 lines to use Chrome
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\LeeKyuSung\\Desktop\\chromedriver_win32 (1)\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// 1. 로그인
		String baseUrl = "https://www.facebook.com";
		driver.get(baseUrl);
		System.out.println(driver.getTitle());
		WebElement id = driver.findElement(By.id("email"));
		WebElement pwd = driver.findElement(By.id("pass"));
		id.sendKeys("ks5050577@nate.com");
		pwd.sendKeys("CTP471ctp471!");
		pwd.sendKeys(Keys.RETURN);

		System.out.println(driver.getTitle());

		// 2. 친구리스트 조회
		baseUrl = "https://www.facebook.com/ks5050577/friends_all";
		driver.get(baseUrl);
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			
		}
		
		// TODO alert
		/*Alert alert = driver.switchTo().alert();
		System.out.println("message : " + alert.getText());
		alert.accept();
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			
		}*/

		// 3. scroll down
		while (true) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		}

	}
}

package tmp_test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

// test class for userinformation

public class SeleniumTest3 {
	public static void main(String[] args) throws Exception {
		// 1. chrome (at window local test)
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\LeeKyuSung\\Desktop\\chromedriver_win32 (1)\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// 2. firefox (at linux test server)
		// System.setProperty("webdriver.gecko.driver", "/home/ctp471/prj/geckodriver");
		// FirefoxDriver driver = new FirefoxDriver();

		// 1. 로그인
		String baseUrl = "https://www.facebook.com";
		driver.get(baseUrl);
		WebElement id = driver.findElement(By.id("email"));
		WebElement pwd = driver.findElement(By.id("pass"));
		id.sendKeys("ks5050577@nate.com");
		pwd.sendKeys("CTP471ctp471!");
		pwd.sendKeys(Keys.RETURN);

		Thread.sleep(5000);

		baseUrl = "https://www.facebook.com/ks5050577/about";
		driver.get(baseUrl);
		Thread.sleep(2000);
		
		List<WebElement> informations = driver.findElements(By.xpath("//*[@class='_c24 _50f4']"));
		int found = informations.size();
		System.out.println(found);
		for (int i=0; i<found; i++) {
			System.out.println(informations.get(i).getText());
		}

	}
}

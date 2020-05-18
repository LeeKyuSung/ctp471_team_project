package crawling;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import conf.Config;

public class Crawling {
	private static Crawling instance = new Crawling();

	private final String baseUrl = Config.CRAWLING_TARGET;
	private WebDriver driver; // 1. chrome (at window local test)
	// private FirefoxDriver driver; // 2. firefox (at linux test server)

	private Crawling() {
		// 1. chrome (at window local test)
		System.setProperty("webdriver.chrome.driver", Config.CRAWLING_CHROME);
		driver = new ChromeDriver();

		// 2. firefox (at linux test server)
		// System.setProperty("webdriver.gecko.driver", Config.CRAWLING_FIREFOX);
		// driver = new FirefoxDriver();

		// login to facebook
		driver.get(baseUrl);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebElement id = driver.findElement(By.id("email"));
		WebElement pwd = driver.findElement(By.id("pass"));
		id.sendKeys(Config.CRAWLING_FACEBOOK_ID);
		pwd.sendKeys(Config.CRAWLING_FACEBOOK_PWD);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		pwd.sendKeys(Keys.RETURN);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Crawling getInstance() {
		return instance;
	}

	public String[] getFriendsList(String userID) {
		// TODO
		return null;
	}

	public String[] getUserInfo(String userID) {
		String targetUrl = baseUrl + userID + "/about";
		driver.get(targetUrl);

		List<WebElement> informations = driver.findElements(By.xpath("//*[@class='_c24 _50f4']"));

		int found = informations.size();
		String[] ret = new String[found];
		for (int i = 0; i < found; i++) {
			ret[i] = informations.get(i).getText();
		}

		return ret;
	}

	public void close() {
		driver.close();
	}
}

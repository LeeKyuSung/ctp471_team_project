package tmp_test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

public class SeleniumTest2 {
	public static void main(String[] args) throws Exception {
		// comment the above 2 lines and uncomment below 2 lines to use Chrome
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\LeeKyuSung\\Desktop\\chromedriver_win32 (1)\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// 1. 로그인
		String baseUrl = "https://www.facebook.com";
		driver.get(baseUrl);
		WebElement id = driver.findElement(By.id("email"));
		WebElement pwd = driver.findElement(By.id("pass"));
		id.sendKeys("ks5050577@nate.com");
		pwd.sendKeys("CTP471ctp471!");
		pwd.sendKeys(Keys.RETURN);
		
		Thread.sleep(5000);

		// 2. 친구리스트 조회
		baseUrl = "https://www.facebook.com/ks5050577/friends_all";
		driver.get(baseUrl);
		Thread.sleep(2000);
		
		// find your frineds count
		String frinedsCount = driver.findElement(By.xpath("//*[@data-tab-key='friends']")).getText().substring(2).replace(",", "");
		int count = Integer.parseInt(frinedsCount);
		
		System.out.println("Count : [" + count + "]");
		
		count -= 100;

		// find your couurent loaded frineds count and get it in a list
		List<WebElement> frineds = driver.findElements(By.xpath("//*[@class='fsl fwb fcb']"));
		int found = frineds.size();

		while (found <= count) {

			// scroll to the last friend found from the current loaded friend list
			Coordinates coordinate = ((Locatable) frineds.get(found - 1)).getCoordinates();
			coordinate.onPage();
			coordinate.inViewPort();
			frineds = driver.findElements(By.xpath("//*[@class='fsl fwb fcb']"));
			found = frineds.size();
			System.out.println("found : " + found);

			// break and print frined list if the condition found frineds = count of frined
			// list
			if (found >= count) {
				System.out.println(found);
				System.out.println("---Printing FriendList---");
				for (int i = 0; i < found; i++) {
					System.out.println(frineds.get(i).getText());
					System.out.println(frineds.get(i).getAttribute("innerHTML"));
					System.out.println(frineds.get(i).getAttribute("outerHTML"));
				}
				break;
			}

		}

	}
}

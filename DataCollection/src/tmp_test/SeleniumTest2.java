package tmp_test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

// finding frineds task (1 time in 10 minute?)
// 1. login
// 2. select user to find from db (maybe 3~5, which is KAIST, which is not found )
// 3. find friends of the user
// 4. save friendsList & friends users at database

// updating userinfo task (1 time in 10 minutes)
// 1. login
// 2. select user to update from db (maybe 100?, which don't know KAIST, low searchNt, UserInfoUpdated is N)
// for each user
// 3. check KAIST. 
// 4. save friendsNumber.

// updating KAISTFriendsList (once or twice a day)
// 1. select user to update (which is KAIST, didn't updated)
// 2. get friendsList from db and check all of the friends which is KAIST
// 3. save to KAISTFriendsList and KAISTFriendsNum

public class SeleniumTest2 {
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

		// 2. 친구리스트 조회
		baseUrl = "https://www.facebook.com/ks5050577/friends_all";
		driver.get(baseUrl);
		Thread.sleep(2000);

		// find your frineds count
		String frinedsCount = driver.findElement(By.xpath("//*[@data-tab-key='friends']")).getText().substring(2)
				.replace(",", "");
		int count = Integer.parseInt(frinedsCount);

		System.out.println("Count : [" + count + "]");

		count = 100;

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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.yield;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FrontendTests {
    private Process process;
    private WebDriver driver;

    @BeforeAll
    public void setup() throws IOException {
        process = new ProcessBuilder("bash", "../../scripts/start.sh").start();

        // Extract SpringBoot and React port
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int sbPort = -1;
        int reactPort = -1;
        Pattern sbPortPattern = Pattern.compile("Tomcat started on port\\(s\\): (\\d+) \\(http\\) with context path ''");
        Pattern reactPortPattern = Pattern.compile("Local:\\s+http://localhost:(\\d+)");
        while (sbPort == -1 || reactPort == -1) {
            String line = reader.readLine();
            if (line == null) {
                if (sbPort == -1 || reactPort == -1) {
                    throw new IOException("Could not find SpringBoot and React ports");
                }
                break;
            }
            // Remove terminal formatting from line
            line = line.replaceAll("\u001B\\[[;\\d]*m", "");
            System.out.println(line);
            Matcher sbMatcher = sbPortPattern.matcher(line);
            Matcher reactMatcher = reactPortPattern.matcher(line);
            if (sbMatcher.find()) {
                sbPort = Integer.parseInt(sbMatcher.group(1));
            }
            if (reactMatcher.find()) {
                reactPort = Integer.parseInt(reactMatcher.group(1));
            }
        }
        System.out.println("SpringBoot port: " + sbPort);
        System.out.println("React port: " + reactPort);

        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(String.format("http://localhost:%d/search", reactPort));
    }

    @Test
    public void testSearch() {
        WebElement searchTextField = driver.findElement(By.id("searchTextField"));
        searchTextField.clear();
        searchTextField.sendKeys("TestTopic");
        WebElement newTopicTextField = driver.findElement(By.id("newTopicTextField"));
        newTopicTextField.clear();
        newTopicTextField.sendKeys("TestTopic");
        WebElement newTopicButton = driver.findElement(By.id("newTopicButton"));
        newTopicButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("p")).getText().contains("Topic: TestTopic");
            }
        });

        assertEquals("Topic: TestTopic", driver.findElement(By.tagName("p")).getText());
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        if (process != null) {
            // TODO: Kill subprocesses
            process.destroy();
        }

    }
}
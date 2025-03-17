package com.example.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SeleniumTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\karol\\Downloads\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.manage().window().maximize();

            driver.get("https://demoqa.com");

            try {
                WebElement cookieCloseBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("close-fixedban")));
                cookieCloseBtn.click();
                System.out.println("Closed cookie popup.");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("No cookie consent popup found.");
            }
            
            WebElement widgetsTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h5[text()='Widgets']")));
            widgetsTab.click();
            System.out.println("Navigated to Widgets.");
            
            WebElement progressBarMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Progress Bar']")));
            progressBarMenu.click();
            System.out.println("Opened Progress Bar section.");

            WebElement startButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("startStopButton")));
            startButton.click();
            System.out.println("Started the progress bar.");

            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.className("progress-bar"), "100%"));
            System.out.println("Progress bar reached 100%.");

            WebElement resetButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("resetButton")));
            resetButton.click();
            System.out.println("Clicked Reset button.");

            WebElement progressBar = driver.findElement(By.className("progress-bar"));
            
            String progressValue = progressBar.getAttribute("aria-valuenow");
            if ("0".equals(progressValue)) {
                System.out.println("Test Passed: Progress bar reset successfully.");
            } else {
                System.out.println("Test Failed: Progress bar did not reset properly. Current value: " + progressValue);
            }

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String progressBarJS = js.executeScript("return document.querySelector('.progress-bar').getAttribute('aria-valuenow');").toString();
            System.out.println("JavaScript fetched progress value: " + progressBarJS);

            if ("0".equals(progressBarJS)) {
                System.out.println("Test Passed: JavaScript confirmed progress bar reset.");
            } else {
                System.out.println("Test Failed: JavaScript shows incorrect progress bar value.");
            }

            Thread.sleep(500);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}

package com.example.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Task4 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\karol\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get("https://demowebshop.tricentis.com/");

            driver.findElement(By.className("ico-register")).click();

            driver.findElement(By.id("gender-male")).click();
            driver.findElement(By.id("FirstName")).sendKeys("TestUser");
            driver.findElement(By.id("LastName")).sendKeys("Demo");

            Random random = new Random();
            String email = "testuser" + random.nextInt(10000) + "@mail.com";
            driver.findElement(By.id("Email")).sendKeys(email);

            driver.findElement(By.id("Password")).sendKeys("Test@1234");
            driver.findElement(By.id("ConfirmPassword")).sendKeys("Test@1234");
            driver.findElement(By.id("register-button")).click();
            System.out.println("Account created");

            WebElement continueButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='button-1 register-continue-button']"))
            );
            continueButton.click();
            System.out.println("Home page");

            List<WebElement> pollOptions = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("pollanswers-1"))
            );

            if (!pollOptions.isEmpty()) {
                pollOptions.get(0).click();
                driver.findElement(By.id("vote-poll-1")).click();
            }

            WebElement updatedVoteCountElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className("poll-total-votes"))
            );

            String updatedVoteCount = updatedVoteCountElement.getText();
            System.out.println(updatedVoteCount + " in total");

        } finally {
            driver.quit();
        }
    }

//    private static int extractVoteNumber(String text) {
//        return Integer.parseInt(text.replaceAll("\\D+", ""));
//    }
}

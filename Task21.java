package com.example.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class Task21 {
    public static void main(String[] args) {
    	
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\karol\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-features=Ads");
        options.addArguments("--force-device-scale-factor=0.8");
        options.addArguments("--high-dpi-support=0.8");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            driver.get("https://demoqa.com/");
            driver.manage().window().maximize();

            try {
                WebElement closeCookies = driver.findElement(By.id("close-fixedban"));
                closeCookies.click();
            } catch (Exception e) {
                System.out.println("No cookie consent found.");
            }

            WebElement elementsTab = driver.findElement(By.xpath("//h5[text()='Elements']"));
            elementsTab.click();
            System.out.println("Elements tab");

            driver.findElement(By.xpath("//span[text()='Web Tables']")).click();
            System.out.println("Web tables");
            Thread.sleep(1500);
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,350)", "");

            for (int i = 0; i < 8; i++) {
                driver.findElement(By.id("addNewRecordButton")).click();
                driver.findElement(By.id("firstName")).sendKeys("User" + i);
                driver.findElement(By.id("lastName")).sendKeys("Test" + i);
                driver.findElement(By.id("userEmail")).sendKeys("user" + i + "@example.com");
                driver.findElement(By.id("age")).sendKeys("30");
                driver.findElement(By.id("salary")).sendKeys("5000");
                driver.findElement(By.id("department")).sendKeys("IT");
                driver.findElement(By.id("submit")).click();
            }
            Thread.sleep(1500);

            WebElement nextButton = driver.findElement(By.xpath("//button[text()='Next']"));
            nextButton.click();
            System.out.println("Pressed next");
            Thread.sleep(1500);

            WebElement deleteButton = driver.findElement(By.xpath("//span[contains(@id, 'delete-record')]"));
            deleteButton.click();
            System.out.println("Deleted an entry");
            Thread.sleep(1500);

            WebElement pageInfo = driver.findElement(By.xpath("//input[@aria-label='jump to page']"));
            String currentPage = pageInfo.getAttribute("value");
            Thread.sleep(1500);
            
            if (currentPage != null && currentPage.equals("1")) {
                System.out.println("Pagination returned to first page.");
            } else {
                System.out.println("Pagination did not reset properly. Current page: " + currentPage);
            }

            WebElement pageCount = driver.findElement(By.className("-totalPages"));
            String totalPages = pageCount.getText();
            if (totalPages.equals("1")) {
                System.out.println("Total page count is reduced to one.");
            } else {
                System.out.println("Total page count is still more than one. Total pages: " + totalPages);
            }
            Thread.sleep(2500);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

package com.example.test;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;


import java.io.*;
import java.time.Duration;
import java.util.*;

public class Task3 {
    private WebDriver driver;
    private static String email;
    private static final String PASSWORD = "Test@12345";

    @BeforeClass
    public static void registerUser() throws IOException {
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
    	WebDriver regDriver = new ChromeDriver(options);
        regDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        regDriver.manage().window().maximize();
        regDriver.get("https://demowebshop.tricentis.com/");
        regDriver.findElement(By.linkText("Log in")).click();
        regDriver.findElement(By.xpath("//div[@class='new-wrapper register-block']//input[@value='Register']")).click();
        regDriver.findElement(By.id("gender-male")).click();
        regDriver.findElement(By.id("FirstName")).sendKeys("Test");
        regDriver.findElement(By.id("LastName")).sendKeys("User");

        email = "testuser" + UUID.randomUUID() + "@mail.com";
        regDriver.findElement(By.id("Email")).sendKeys(email);
        regDriver.findElement(By.id("Password")).sendKeys(PASSWORD);
        regDriver.findElement(By.id("ConfirmPassword")).sendKeys(PASSWORD);
        regDriver.findElement(By.id("register-button")).click();
        
        regDriver.findElement(By.xpath("//input[@class='button-1 register-continue-button']")).click();

        try (FileWriter writer = new FileWriter("user_credentials.txt")) {
            writer.write(email + "\n" + PASSWORD);
        }

        regDriver.quit();
    }

    @Before
    public void setup() throws IOException {
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
    	driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        login();
    }

    private void login() throws IOException {
        driver.get("https://demowebshop.tricentis.com/");
        driver.findElement(By.linkText("Log in")).click();

        BufferedReader reader = new BufferedReader(new FileReader("user_credentials.txt"));
        email = reader.readLine();
        reader.close();

        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//input[@value='Log in']")).click();
    }

    @Test
    public void placeOrderWithData1() throws IOException {
        placeOrder("data1.txt");
    }

    @Test
    public void placeOrderWithData2() throws IOException {
        placeOrder("data2.txt");
    }

    private void placeOrder(String dataFile) throws IOException {
        driver.findElement(By.linkText("Digital downloads")).click();
        List<String> products = readLines(dataFile);
        System.out.println("Adding products: " + products);

        Map<String, Integer> productCounts = new HashMap<>();
        for (String product : products) {
            productCounts.put(product, productCounts.getOrDefault(product, 0) + 1);
        }

        List<WebElement> productList = driver.findElements(By.className("product-item"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            for (WebElement item : productList) {
                if (item.getText().contains(entry.getKey())) {
                    WebElement addToCartButton = item.findElement(By.cssSelector("input[value='Add to cart']"));
                    for (int i = 0; i < entry.getValue(); i++) {
                        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ajax-loading-block-window")));
                    }
                    break;
                }
            }
        }

        driver.findElement(By.linkText("Shopping cart")).click();
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();
        
        System.out.println("Checking if an existing billing address is available...");

        try {
            WebElement addressDropdown = driver.findElement(By.id("billing-address-select"));
            Select addressSelect = new Select(addressDropdown);
            
            if (addressDropdown.isDisplayed() && addressSelect.getOptions().size() > 1) {
                System.out.println("Using existing billing address.");
                driver.findElement(By.xpath("//input[@value='Continue']")).click();
                addressSelect.selectByIndex(1);
            } else {
                throw new NoSuchElementException("No existing address found.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("No existing billing address found. Filling in new details...");
            
            Select countryDropdown = new Select(driver.findElement(By.id("BillingNewAddress_CountryId")));
            countryDropdown.selectByVisibleText("Lithuania");
            driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Vilnius");
            driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Ozo g 69");
            driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("10101");
            driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("065069421");
        }

        driver.findElement(By.xpath("//input[@value='Continue']")).click();

        
        WebElement paymentContinueButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("payment-method-next-step-button")));
        paymentContinueButton.click();
        System.out.println("Clicked payment method continue.");
        
        wait.until(ExpectedConditions.elementToBeClickable(By.className("payment-info-next-step-button"))).click();
        System.out.println("Clicked payment info continue.");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Confirm']"))).click();
        System.out.println("Clicked confirm order.");
     // Wait for the success message to appear before asserting
        WebDriverWait waitSuccess = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement successMessage = waitSuccess.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Your order has been successfully processed!')]"))
        );
        Assert.assertTrue("Success message not found!", successMessage.isDisplayed());
        System.out.println("Test passed");
    }

    private List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}

package com.example.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class task1 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\karol\\Downloads\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.manage().window().maximize();
            driver.get("https://demowebshop.tricentis.com/");
            System.out.println("Opened Demo Web Shop.");

            WebElement giftCardsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Gift Cards')]")));
            giftCardsMenu.click();
            System.out.println("Navigated to Gift Cards section.");

            List<WebElement> giftCards = driver.findElements(By.cssSelector(".product-item"));
            WebElement selectedGiftCard = null;
            
            //galima tiesiog per xpath referencint
            
            for (WebElement card : giftCards) {
                WebElement priceElement = card.findElement(By.cssSelector(".price"));
                double price = Double.parseDouble(priceElement.getText().replace("$", ""));
                if (price > 99) {
                    selectedGiftCard = card;
                    System.out.println("Found card!");
                    break;
                }
            }

            if (selectedGiftCard == null) {
                System.out.println("No gift card found above $99.");
                return;
            }

            selectedGiftCard.findElement(By.cssSelector("h2 a")).click();
            System.out.println("Selected a gift card priced above $99.");
            
            driver.findElement(By.id("giftcard_4_RecipientName")).sendKeys("John Doe");
            driver.findElement(By.id("giftcard_4_SenderName")).sendKeys("Jane Doe");
            
            //optional zinutes
            driver.findElement(By.id("giftcard_4_Message")).sendKeys("we take care of fam <3");
            System.out.println("Entered a message for the gift card.");

            WebElement quantityField = driver.findElement(By.id("addtocart_4_EnteredQuantity"));
            quantityField.clear();
            quantityField.sendKeys("5000");

            driver.findElement(By.id("add-to-cart-button-4")).click();
            System.out.println("Added gift card to cart.");
            Thread.sleep(800);

            driver.findElement(By.cssSelector(".add-to-wishlist-button")).click();
            System.out.println("Added gift card to wish list.");
            Thread.sleep(800);

            WebElement jewelryMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Jewelry')]")));
            jewelryMenu.click();
            System.out.println("Navigated to Jewelry section.");

            WebElement customJewelryLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create Your Own Jewelry")));
            customJewelryLink.click();
            System.out.println("Opened Create Your Own Jewelry.");

            WebElement materialDropdownElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product_attribute_71_9_15")));
            Select materialDropdown = new Select(materialDropdownElement);

            materialDropdown.selectByValue("47");
            System.out.println("Selected material: Silver 1mm");

            driver.findElement(By.id("product_attribute_71_10_16")).sendKeys("80");
            System.out.println("Length 80 picked");
            Thread.sleep(1500);

            WebElement starPendantLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='product_attribute_71_11_17_50']")));
            starPendantLabel.click();

            System.out.println("Selected Pendant: Star");

            
            WebElement jewelryQty = driver.findElement(By.id("addtocart_71_EnteredQuantity"));
            jewelryQty.clear();
            jewelryQty.sendKeys("26");

            driver.findElement(By.id("add-to-cart-button-71")).click();
            System.out.println("Added custom jewelry to cart.");
            Thread.sleep(1500);

            driver.findElement(By.cssSelector(".add-to-wishlist-button")).click();
            System.out.println("Added custom jewelry to wish list.");
            Thread.sleep(1500);

            WebElement wishlistLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Wishlist')]")));
            wishlistLink.click();
            System.out.println("Opened Wishlist.");
            Thread.sleep(1500);

            List<WebElement> wishlistCheckboxes = driver.findElements(By.name("addtocart"));
            for (WebElement checkbox : wishlistCheckboxes) {
                if (!checkbox.isSelected()) {
                    checkbox.click();
                }
            }
            System.out.println("Selected items in Wishlist to add to cart.");
            Thread.sleep(1500);

            driver.findElement(By.name("addtocartbutton")).click();
            System.out.println("Moved items from Wishlist to Cart.");
            Thread.sleep(1500);

            WebElement cartSubTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-total-right")));
            String subTotalText = cartSubTotal.getText().replace("$", "").trim();

            if ("1002600.00".equals(subTotalText)) {
                System.out.println("Test Passed: Sub-Total is correct: " + subTotalText);
            } else {
                System.out.println("Test Failed: Sub-Total is incorrect. Found: " + subTotalText);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}

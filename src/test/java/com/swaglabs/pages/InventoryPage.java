package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle         = By.cssSelector(".title");
    private By inventoryItems    = By.cssSelector(".inventory_item");
    private By addToCartButtons  = By.cssSelector("[data-test^='add-to-cart']");
    private By cartIcon          = By.cssSelector(".shopping_cart_link");
    private By cartBadge         = By.cssSelector(".shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnInventoryPage() {
        wait.until(ExpectedConditions.urlContains("inventory"));
        return driver.getCurrentUrl().contains("inventory");
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public void addFirstItemToCart() {
        List<WebElement> buttons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));
        jsClick(buttons.get(0));
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(addToCartButtons, buttons.size()));
    }

    public void addItemToCartByIndex(int index) {
        List<WebElement> buttons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));
        jsClick(buttons.get(index));
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(addToCartButtons, buttons.size()));
    }

    public String getCartCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? "0" : badges.get(0).getText();
    }

    public void goToCart() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(cartIcon)));
        wait.until(ExpectedConditions.urlContains("cart"));
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public int getInventoryItemCount() {
        return driver.findElements(inventoryItems).size();
    }
}

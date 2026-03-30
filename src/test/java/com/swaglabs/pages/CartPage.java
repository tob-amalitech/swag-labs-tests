package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle        = By.cssSelector(".title");
    private By cartItems        = By.cssSelector(".cart_item");
    private By cartItemNames    = By.cssSelector(".inventory_item_name");
    private By removeButtons    = By.cssSelector("[data-test^='remove']");
    private By checkoutButton   = By.id("checkout");
    private By continueShoppingBtn = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnCartPage() {
        wait.until(ExpectedConditions.urlContains("cart"));
        return driver.getCurrentUrl().contains("cart");
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<WebElement> getCartItems() {
        return driver.findElements(cartItems);
    }

    public void removeFirstItem() {
        List<WebElement> buttons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(removeButtons));
        buttons.get(0).click();
    }

    public void proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    public void continueShopping() {
        driver.findElement(continueShoppingBtn).click();
    }

    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }
}

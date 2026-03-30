package com.swaglabs.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Step One locators
    private By firstNameField   = By.id("first-name");
    private By lastNameField    = By.id("last-name");
    private By postalCodeField  = By.id("postal-code");
    private By continueButton   = By.id("continue");
    private By errorMessage     = By.cssSelector("[data-test='error']");

    // Step Two locators
    private By finishButton     = By.id("finish");
    private By summaryTotal     = By.cssSelector(".summary_total_label");

    // Confirmation locators
    private By confirmationHeader   = By.cssSelector(".complete-header");
    private By confirmationText     = By.cssSelector(".complete-text");
    private By backHomeButton       = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOnCheckoutStepOne() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
        return driver.getCurrentUrl().contains("checkout-step-one");
    }

    public boolean isOnCheckoutStepTwo() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        return driver.getCurrentUrl().contains("checkout-step-two");
    }

    public boolean isOnConfirmationPage() {
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        return driver.getCurrentUrl().contains("checkout-complete");
    }

    public void enterShippingInfo(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void clickContinue() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(continueButton)));
    }

    public String getOrderTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summaryTotal)).getText();
    }

    public void clickFinish() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(finishButton)));
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
    }

    public String getConfirmationHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationHeader)).getText();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public void clickBackHome() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(backHomeButton)));
        wait.until(ExpectedConditions.urlContains("inventory"));
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}

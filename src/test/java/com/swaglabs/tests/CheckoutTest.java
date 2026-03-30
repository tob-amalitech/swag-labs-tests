package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.CheckoutPage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setupCheckout() {
        LoginPage loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        // Login and add item to cart before each test
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();
        cartPage.proceedToCheckout();
    }

    @Test(description = "Verify checkout step one page loads correctly")
    public void testCheckoutStepOnePageLoads() {
        Assert.assertTrue(checkoutPage.isOnCheckoutStepOne(),
                "Should be on checkout step one page");
    }

    @Test(description = "Verify checkout fails when all fields are empty")
    public void testCheckoutWithEmptyFields() {
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
                "Should show error for missing first name");
    }

    @Test(description = "Verify checkout fails when first name is missing")
    public void testCheckoutMissingFirstName() {
        checkoutPage.enterShippingInfo("", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
                "Should show error when first name is empty");
    }

    @Test(description = "Verify checkout step two (overview) is reached with valid info")
    public void testCheckoutStepTwo() {
        checkoutPage.enterShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isOnCheckoutStepTwo(),
                "Should proceed to order summary page");
    }

    @Test(description = "Verify order total is displayed on step two")
    public void testOrderTotalDisplayed() {
        checkoutPage.enterShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        String total = checkoutPage.getOrderTotal();
        Assert.assertNotNull(total, "Order total should be displayed");
        Assert.assertTrue(total.contains("Total:"), "Total label should be present");
    }

    @Test(description = "Verify successful order completion")
    public void testSuccessfulOrderCompletion() {
        checkoutPage.enterShippingInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOnConfirmationPage(),
                "Should land on confirmation page after completing order");
        Assert.assertEquals(checkoutPage.getConfirmationHeader(), "Thank you for your order!",
                "Confirmation header should match expected text");
    }

    @Test(description = "Verify back to home button on confirmation page works")
    public void testBackToHomeAfterOrder() {
        checkoutPage.enterShippingInfo("Jane", "Smith", "54321");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        checkoutPage.clickBackHome();

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Should return to inventory/products page after order");
    }
}

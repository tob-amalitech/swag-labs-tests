package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "User should be redirected to inventory page after login");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products",
                "Page title should be 'Products'");
    }

    @Test(description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("invalid_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message text should mention wrong credentials");
    }

    @Test(description = "Verify login fails with empty username")
    public void testEmptyUsernameLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed when username is empty");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error should say username is required");
    }

    @Test(description = "Verify login fails with empty password")
    public void testEmptyPasswordLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed when password is empty");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Error should say password is required");
    }

    @Test(description = "Verify locked out user cannot login")
    public void testLockedOutUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error should mention account is locked");
    }
}

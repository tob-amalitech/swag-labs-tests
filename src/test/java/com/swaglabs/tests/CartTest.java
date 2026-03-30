package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void loginBeforeTest() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        loginPage.login("standard_user", "secret_sauce");
    }

    @Test(description = "Verify item can be added to cart from inventory page")
    public void testAddItemToCart() {
        inventoryPage.addFirstItemToCart();

        Assert.assertEquals(inventoryPage.getCartCount(), "1",
                "Cart badge should show 1 item after adding to cart");
    }

    @Test(description = "Verify multiple items can be added to cart")
    public void testAddMultipleItemsToCart() {
        inventoryPage.addItemToCartByIndex(0);
        inventoryPage.addItemToCartByIndex(1);

        Assert.assertEquals(inventoryPage.getCartCount(), "2",
                "Cart badge should show 2 items after adding two products");
    }

    @Test(description = "Verify cart shows correct items after adding")
    public void testCartContainsAddedItems() {
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();

        Assert.assertTrue(cartPage.isOnCartPage(),
                "Should navigate to cart page");
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should contain 1 item");
    }

    @Test(description = "Verify item can be removed from cart")
    public void testRemoveItemFromCart() {
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should have 1 item before removal");

        cartPage.removeFirstItem();

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing the only item");
    }

    @Test(description = "Verify cart page title is correct")
    public void testCartPageTitle() {
        inventoryPage.goToCart();

        Assert.assertEquals(cartPage.getPageTitle(), "Your Cart",
                "Cart page title should be 'Your Cart'");
    }

    @Test(description = "Verify continue shopping returns to inventory")
    public void testContinueShopping() {
        inventoryPage.goToCart();
        cartPage.continueShopping();

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Should return to inventory page after clicking Continue Shopping");
    }
}

package com.anilnag.selenium.ui;

import com.anilnag.selenium.base.BaseTest;
import com.anilnag.selenium.pages.CartPage;
import com.anilnag.selenium.pages.InventoryPage;
import com.anilnag.selenium.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private InventoryPage loginAsStandardUser() {
        return new LoginPage(driver).open().loginAs("standard_user", "secret_sauce");
    }

    @Test(description = "Adding a product updates the cart badge count")
    public void addingItemUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(inventory.getCartCount(), 1);
    }

    @Test(description = "Cart page reflects items added from the inventory page")
    public void cartShowsAddedItem() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCartByName("Sauce Labs Backpack");
        CartPage cart = inventory.openCart();
        Assert.assertEquals(cart.getItemCount(), 1);
        Assert.assertTrue(cart.getItemNames().contains("Sauce Labs Backpack"));
    }

    @Test(description = "Full checkout flow completes end to end")
    public void checkoutFlowCompletesSuccessfully() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCartByName("Sauce Labs Backpack");
        String confirmation = inventory.openCart()
                .goToCheckout()
                .fillInfoAndContinue("Anil", "Nag", "89109")
                .finishOrder();
        Assert.assertEquals(confirmation, "Thank you for your order!");
    }
}

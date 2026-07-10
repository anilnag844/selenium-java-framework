package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return textOf(pageTitle);
    }

    public InventoryPage addToCartByName(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        click(By.id("add-to-cart-" + slug));
        return this;
    }

    public int getCartCount() {
        return isVisible(cartBadge) ? Integer.parseInt(textOf(cartBadge)) : 0;
    }

    public CartPage openCart() {
        click(cartLink);
        waitForUrlContains("cart.html");
        return new CartPage(driver);
    }
}

package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By removeButton = By.cssSelector("button[id^='remove']");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(org.openqa.selenium.WebElement::getText)
                .collect(Collectors.toList());
    }

    public CartPage removeFirstItem() {
        click(removeButton);
        return this;
    }

    public CheckoutInfoPage goToCheckout() {
        click(checkoutButton);
        waitForUrlContains("checkout-step-one");
        return new CheckoutInfoPage(driver);
    }
}

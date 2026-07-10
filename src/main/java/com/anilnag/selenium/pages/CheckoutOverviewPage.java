package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOverviewPage extends BasePage {

    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String finishOrder() {
        click(finishButton);
        return textOf(completeHeader);
    }
}

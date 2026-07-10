package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waitVisible(locator).getText();
    }

    protected boolean isVisible(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    /**
     * saucedemo.com's nav links are client-routed anchors with no href, driven purely by a bound
     * click handler — occasionally a click lands without the route change registering. Short
     * retries recover from this far more reliably than one long wait.
     */
    protected void clickAndWaitForUrl(By locator, String urlFragment) {
        final int maxAttempts = 4;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            click(locator);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains(urlFragment));
                return;
            } catch (org.openqa.selenium.TimeoutException timeout) {
                if (attempt == maxAttempts) {
                    throw timeout;
                }
            }
        }
    }
}

package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    /**
     * React-controlled inputs track their value through the DOM's *native* property setter; a
     * plain sendKeys()/clear() pair can land without the framework's own state ever seeing it, so
     * the value gets wiped back to empty on the next render. Calling the native setter directly
     * (bypassing any instance-level override) and dispatching a real "input" event is what
     * actually reaches React's onChange, and is far more reliable here than sendKeys().
     */
    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        String script =
                "var el = arguments[0], value = arguments[1];"
                        + "var proto = el.tagName === 'TEXTAREA' ? window.HTMLTextAreaElement.prototype : window.HTMLInputElement.prototype;"
                        + "Object.getOwnPropertyDescriptor(proto, 'value').set.call(el, value);"
                        + "el.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "el.dispatchEvent(new Event('change', { bubbles: true }));";
        ((JavascriptExecutor) driver).executeScript(script, element, text);
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
     * saucedemo.com's nav links are client-routed anchors with no href, and their visible icon is
     * a CSS pseudo-element — the anchor's real hit box doesn't line up with where a native click
     * lands, so WebDriver's coordinate-based click silently misses. Invoking element.click() via
     * JS bypasses hit-testing entirely and lands on the node itself.
     */
    protected void clickAndWaitForUrl(By locator, String urlFragment) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        waitForUrlContains(urlFragment);
    }
}

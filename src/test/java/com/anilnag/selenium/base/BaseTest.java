package com.anilnag.selenium.base;

import com.anilnag.selenium.driver.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            System.out.println("DEBUG [" + result.getName() + "] url=" + driver.getCurrentUrl());
            System.out.println("DEBUG [" + result.getName() + "] title=" + driver.getTitle());
            System.out.println("DEBUG [" + result.getName() + "] cartLinkCount="
                    + js.executeScript("return document.querySelectorAll('.shopping_cart_link').length;"));
            System.out.println("DEBUG [" + result.getName() + "] cartLinkHtml="
                    + js.executeScript("var e=document.querySelector('.shopping_cart_link'); return e ? e.outerHTML : 'NOT FOUND';"));
            System.out.println("DEBUG [" + result.getName() + "] badgeCount="
                    + js.executeScript("var e=document.querySelector('.shopping_cart_badge'); return e ? e.textContent : 'NO BADGE';"));
        }
        DriverFactory.quitDriver();
    }
}

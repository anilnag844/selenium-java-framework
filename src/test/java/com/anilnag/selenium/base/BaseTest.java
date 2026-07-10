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
            Object footerHtml = ((JavascriptExecutor) driver).executeScript(
                    "var f = document.querySelector('.cart_footer'); return f ? f.outerHTML : 'NO .cart_footer FOUND. Body: ' + document.body.innerHTML.substring(0, 2000);");
            System.out.println("DEBUG [" + result.getName() + "] url=" + driver.getCurrentUrl());
            System.out.println("DEBUG [" + result.getName() + "] cart_footer=" + footerHtml);
        }
        DriverFactory.quitDriver();
    }
}

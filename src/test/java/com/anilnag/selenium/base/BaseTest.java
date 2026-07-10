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
            System.out.println("DEBUG [" + result.getName() + "] error="
                    + js.executeScript("var e=document.querySelector('[data-test=\"error\"]'); return e ? e.textContent : 'NO ERROR ELEMENT';"));
            System.out.println("DEBUG [" + result.getName() + "] firstNameVal="
                    + js.executeScript("var e=document.getElementById('first-name'); return e ? e.value : 'NO FIELD';"));
            System.out.println("DEBUG [" + result.getName() + "] lastNameVal="
                    + js.executeScript("var e=document.getElementById('last-name'); return e ? e.value : 'NO FIELD';"));
            System.out.println("DEBUG [" + result.getName() + "] postalVal="
                    + js.executeScript("var e=document.getElementById('postal-code'); return e ? e.value : 'NO FIELD';"));
        }
        DriverFactory.quitDriver();
    }
}

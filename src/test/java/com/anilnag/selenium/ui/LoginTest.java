package com.anilnag.selenium.ui;

import com.anilnag.selenium.base.BaseTest;
import com.anilnag.selenium.pages.InventoryPage;
import com.anilnag.selenium.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Standard user logs in and lands on the Products page")
    public void standardUserCanLogIn() {
        InventoryPage inventory = new LoginPage(driver).open().loginAs("standard_user", "secret_sauce");
        Assert.assertEquals(inventory.getTitle(), "Products");
    }

    @Test(description = "Locked out user is blocked with an explicit error")
    public void lockedOutUserIsBlocked() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"));
    }

    @Test(description = "Wrong password is rejected with an error, not a silent failure")
    public void invalidPasswordIsRejected() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.loginAs("standard_user", "wrong_password");
        Assert.assertTrue(loginPage.isErrorDisplayed());
    }
}

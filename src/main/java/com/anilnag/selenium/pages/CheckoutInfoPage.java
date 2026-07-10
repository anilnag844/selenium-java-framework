package com.anilnag.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInfoPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    /**
     * This page can still be mid-hydration when it's first reached — React re-renders these
     * inputs against their (still-empty) initial state a moment later and silently wipes out
     * whatever was just typed. Filling all three, waiting past that window, then re-verifying
     * (and retyping if anything got clobbered) survives it; a single sendKeys() pass does not.
     */
    public CheckoutOverviewPage fillInfoAndContinue(String firstName, String lastName, String postalCode) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            type(firstNameInput, firstName);
            type(lastNameInput, lastName);
            type(postalCodeInput, postalCode);
            sleepMillis(500);
            if (fieldsStillHold(firstName, lastName, postalCode)) {
                break;
            }
        }
        clickAndWaitForUrl(continueButton, "checkout-step-two");
        return new CheckoutOverviewPage(driver);
    }

    private boolean fieldsStillHold(String firstName, String lastName, String postalCode) {
        return firstName.equals(waitVisible(firstNameInput).getDomProperty("value"))
                && lastName.equals(waitVisible(lastNameInput).getDomProperty("value"))
                && postalCode.equals(waitVisible(postalCodeInput).getDomProperty("value"));
    }

    private void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

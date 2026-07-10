# selenium-java-framework

[![CI](https://github.com/anilnag844/selenium-java-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/anilnag844/selenium-java-framework/actions/workflows/ci.yml)

A public, sanitized demo of the **Selenium + Java + TestNG framework architecture** I build at enterprise scale — Page Object Model, a thread-safe driver factory, a REST Assured API layer, and CI on GitHub Actions.

> This mirrors the layering patterns used in production frameworks I've architected for enterprise QE teams, run here against public demo targets with no client code. More context: [anilnag844.github.io](https://anilnag844.github.io) · [LinkedIn](https://www.linkedin.com/in/anilkumarnag/)

## Architecture

```
src/main/java/com/anilnag/selenium/
├── driver/         DriverFactory — thread-safe, headless-by-default Chrome via WebDriverManager
└── pages/          Page Object Model — LoginPage, InventoryPage, CartPage, CheckoutInfoPage, CheckoutOverviewPage
src/test/java/com/anilnag/selenium/
├── base/           BaseTest — driver lifecycle (setUp/tearDown) shared by every UI test
├── ui/             LoginTest, CartTest — TestNG specs against saucedemo.com
└── api/            UsersApiTest — REST Assured specs against jsonplaceholder.typicode.com
testng.xml          Suite definition (UI + API test groups)
```

Locators live once per page object; tests never see a `By` selector directly, so a UI change touches one file, not every spec that exercises it. Page objects return the next page object from each action (`loginAs()` returns `InventoryPage`, `goToCheckout()` returns `CheckoutInfoPage`), so tests read as a chained user journey instead of a flat script.

Two saucedemo.com quirks this framework works around deliberately, not accidentally:

- **Nav clicks**: its links are client-routed anchors with no `href`, and the visible icon is a CSS pseudo-element — the anchor's real hit box doesn't line up with where a coordinate-based native click lands. `BasePage.clickAndWaitForUrl()` invokes `element.click()` via JavaScript instead, bypassing hit-testing.
- **Form input**: a freshly-routed page can still be mid-hydration when a field is typed into — React re-renders the input against its (still-empty) initial state a moment *later*, silently wiping out what was already typed. An immediate post-`sendKeys()` check misses this since the clobber hasn't happened yet. `CheckoutInfoPage.fillInfoAndContinue()` fills all three fields, waits past the hydration window, then re-verifies every field and retypes anything that got wiped before submitting.

## Run it

```bash
mvn test                    # everything (UI headless + API)
mvn test -DHEADLESS=false   # watch the browser locally
```

## What's covered

| Suite | Target | What it checks |
|---|---|---|
| `ui.LoginTest` | saucedemo.com | Valid login, locked-out user, invalid credentials |
| `ui.CartTest` | saucedemo.com | Add-to-cart badge state, cart contents, full checkout flow |
| `api.UsersApiTest` | jsonplaceholder.typicode.com | GET list, GET single resource, POST create |

## CI

Every push and PR runs the full suite headless on GitHub Actions (Chrome is preinstalled on `ubuntu-latest`; WebDriverManager resolves the matching driver automatically). Surefire reports are uploaded as a build artifact.

## License

MIT

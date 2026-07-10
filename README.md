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

- **Clicks**: its nav elements are client-routed with no `href`, and a visible icon is often a CSS pseudo-element — the real hit box doesn't reliably line up with where a coordinate-based native click lands. `BasePage.click()` invokes `element.click()` via JavaScript instead, bypassing hit-testing.
- **Form input**: its inputs are React-controlled — the app's state only updates through React's tracked native setter, so a plain `sendKeys()`/`clear()` pair can land in the DOM without React's own state ever seeing it, and the field reverts to empty on the next render. `BasePage.type()` sets the value through the real native property setter and dispatches a genuine `input` event, which is what React's `onChange` actually listens for.

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

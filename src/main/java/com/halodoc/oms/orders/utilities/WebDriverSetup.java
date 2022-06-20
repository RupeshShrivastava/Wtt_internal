package com.halodoc.oms.orders.utilities;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverSetup {

    public static RemoteWebDriver getDriver() {
        RemoteWebDriver driver = setUpChromeDriver();
        driver.manage().window().maximize();
        return driver;

    }

    public static ChromeDriver setUpChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> images = new HashMap<>();
        images.put("images", 2);
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
        WebDriverManager.chromedriver().setup();
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        //options.addArguments("--window-size=1200*600");
        options.addArguments("--disable-dev-shm-usage");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");

        return new ChromeDriver(options);

    }
}

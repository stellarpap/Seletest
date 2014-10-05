/*
This file is part of the Seletest by Papadakis Giannis <gpapadakis84@gmail.com>.

Copyright (c) 2014, Papadakis Giannis <gpapadakis84@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.automation.seletest.core.selenium.webAPI.remoteWebDriver;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.seletest.core.selenium.threads.SessionContext;
import com.automation.seletest.core.selenium.webAPI.DriverBaseController;
import com.automation.seletest.core.selenium.webAPI.elements.ByJQuerySelector;
import com.automation.seletest.core.selenium.webAPI.elements.Locators;
import com.automation.seletest.core.selenium.webAPI.interfaces.MainController;
import com.automation.seletest.core.services.FilesUtils;
import com.automation.seletest.core.services.annotations.Monitor;
import com.automation.seletest.core.services.annotations.RetryFailure;
import com.automation.seletest.core.services.annotations.WaitCondition;
import com.automation.seletest.core.services.annotations.WaitCondition.waitFor;

/**
 * This class contains the implementation of WebDriver 2 API
 * for interaction with UI
 * @author Giannis Papadakis(mailTo:gpapadakis84@gmail.com)
 * @param <T>
 *
 */
@Component("webDriverControl")
public class WebDriverController<T extends RemoteWebDriver> extends DriverBaseController<T> implements MainController{

    /**FileUtils*/
    @Autowired
    FilesUtils fileService;

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void goToTargetHost(String url) {
        webDriver().get(url);
    }

    /*************************************************************
     ************************ACTIONS SECTION*********************
     *************************************************************
     */

    @Override
    @Monitor
    @WaitCondition(waitFor.CLICKABLE)
    @RetryFailure(retryCount=1)
    public void click(Object locator) {
        SessionContext.getSession().getWebElement().click();
    }

    @Override
    @Monitor
    @WaitCondition(waitFor.VISIBILITY)
    @RetryFailure(retryCount=1)
    public void type(Object locator, String text) {
        SessionContext.getSession().getWebElement().sendKeys(text);
    }

    @Override
    @WaitCondition(waitFor.VISIBILITY)
    public void changeStyle(Object locator, String attribute, String attributevalue) {
        executeJS("arguments[0].style."+attribute+"=arguments[1]",SessionContext.getSession().getWebElement(),attributevalue);
    }

    /*************************************************************
     ************************SCREENSHOTS SECTION******************
     *************************************************************
     */
    @Override
    @Monitor
    public void takeScreenShot() throws IOException{
        File scrFile = ((TakesScreenshot) webDriver()).getScreenshotAs(OutputType.FILE);
        File file = fileService.createScreenshotFile();
        FileUtils.copyFile(scrFile, file);
        fileService.reportScreenshot(file);
    }

    @Override
    @Monitor
    @WaitCondition(waitFor.VISIBILITY)
    public void takeScreenShotOfElement(Object locator) throws IOException {
        File screenshot = ((TakesScreenshot)webDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage  fullImg = ImageIO.read(screenshot);
        WebElement element=SessionContext.getSession().getWebElement();
        Point point = element.getLocation();
        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();
        Rectangle elementScreen=new Rectangle(eleWidth, eleHeight);
        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), elementScreen.width, elementScreen.height);
        ImageIO.write(eleScreenshot, "png", screenshot);
        File file = fileService.createScreenshotFile();
        FileUtils.copyFile(screenshot, file);
        fileService.reportScreenshot(file);
    }

    @Override
    public WebElement findElement(Object locator) {
        return waitController().waitForElementVisibility(locator);
    }


    /**************************************
     **Returning type methods**************
     **************************************/

    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public String getText(Object locator) {
        return SessionContext.getSession().getWebElement().getText();
    }

    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public String getTagName(Object locator) {
        return SessionContext.getSession().getWebElement().getTagName();
    }

    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public Point getLocation(Object locator) {
        return SessionContext.getSession().getWebElement().getLocation();
    }

    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public Dimension getElementDimensions(Object locator) {
        return SessionContext.getSession().getWebElement().getSize();
    }

    @Override
    @RetryFailure(retryCount=1)
    public String getPageSource() {
        return webDriver().getPageSource();
    }

    /**************************************
     *Verification type methods************
     **************************************/

    @Override
    public boolean isWebElementPresent(String locator) {
        waitController().waitForElementPresence(locator);
        return true;
    }

    @Override
    public boolean isTextPresent(String text) {
        if(getPageSource().contains(text)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isWebElementVisible(Object locator) {
        waitController().waitForElementVisibility(locator);
        return true;
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.ElementController#uploadFile(java.lang.String, org.openqa.selenium.WebElement)
     */
    @Override
    @Monitor
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public void uploadFile(Object locator, String path) {
        LocalFileDetector detector = new LocalFileDetector();
        File localFile = detector.getLocalFile(path);
        ((RemoteWebElement)SessionContext.getSession().getWebElement()).setFileDetector(detector);
        SessionContext.getSession().getWebElement().sendKeys(localFile.getAbsolutePath());
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.ElementController#executeJS(java.lang.String, java.lang.Object[])
     */
    @Override
    public Object executeJS(String script, Object... args) {
        JavascriptExecutor jsExec=webDriver();
        return jsExec.executeScript(script,args);
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.ElementController#selectByValue(java.lang.String)
     */
    @Override
    @Monitor
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public void selectByValue(String locator, String value) {
        new Select(SessionContext.getSession().getWebElement()).selectByValue(value);
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.ElementController#selectByLabel(java.lang.String, java.lang.String)
     */
    @Override
    @Monitor
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public void selectByVisibleText(String locator, String text) {
        new Select(SessionContext.getSession().getWebElement()).selectByVisibleText(text);

    }

    /*************************************************************
     ************************COOKIES SECTION*********************
     *************************************************************
     */
    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void deleteCookieByName(String name) {
        webDriver().manage().deleteCookieNamed(name);
    }

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void deleteAllCookies() {
        webDriver().manage().deleteAllCookies();
    }

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void addCookie(Cookie cookie) {
        webDriver().manage().addCookie(cookie);
    }

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public Set<Cookie> getCookies() {
        Set<Cookie> cookies=webDriver().manage().getCookies();
        return cookies;
    }

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void deleteCookie(Cookie cookie) {
        webDriver().manage().deleteCookie(cookie);
    }

    /*************************************************************
     ************************TIMEOUTS SECTION*********************
     *************************************************************
     */
    @Override
    @RetryFailure(retryCount=1)
    public void implicitlyWait(long timeout,TimeUnit timeunit) {
        webDriver().manage().timeouts().implicitlyWait(timeout, timeunit);
    }

    @Override
    @RetryFailure(retryCount=1)
    public void pageLoadTimeout(long timeout, TimeUnit timeunit) {
        webDriver().manage().timeouts().pageLoadTimeout(timeout, timeunit);
    }

    @Override
    @RetryFailure(retryCount=1)
    public void scriptLoadTimeout(long timeout, TimeUnit timeunit) {
        webDriver().manage().timeouts().setScriptTimeout(timeout, timeunit);
    }

    /*************************************************************
     ************************WINDOWS SECTION*********************
     *************************************************************
     */
    @Override
    @RetryFailure(retryCount=1)
    public void setWindowPosition(Point point) {
        webDriver().manage().window().setPosition(point);
    }

    @Override
    @RetryFailure(retryCount=1)
    public void setWindowDimension(Dimension dimension) {
        webDriver().manage().window().setSize(dimension);
    }

    @Override
    public Point getWindowPosition() {
        return webDriver().manage().window().getPosition();
    }

    @Override
    @RetryFailure(retryCount=1)
    public Dimension getWindowDimension() {
        return webDriver().manage().window().getSize();
    }

    @Override
    @RetryFailure(retryCount=1)
    public void maximizeWindow() {
        webDriver().manage().window().maximize();
    }

    /*************************************************************
     ************************LOGS SECTION*********************
     *************************************************************
     */
    @Override
    @RetryFailure(retryCount=1)
    public LogEntries logs(String logtype) {
        return webDriver().manage().logs().get(logtype);
    }

    @Override
    @RetryFailure(retryCount=1)
    public void switchToLatestWindow() {
        Iterator<String> iterator = webDriver().getWindowHandles().iterator();
        String lastWindow = null;
        while (iterator.hasNext()) {
            lastWindow = iterator.next();
        }
        webDriver().switchTo().window(lastWindow);
    }

    @Override
    @RetryFailure(retryCount=1)
    public void acceptAlert() {
        waitController().waitForAlert().accept();
    }


    @Override
    @RetryFailure(retryCount=1)
    public void dismissAlert() {
        waitController().waitForAlert().dismiss();
    }

    @Override
    @RetryFailure(retryCount=1)
    public int getNumberOfOpenedWindows() {
        return webDriver().getWindowHandles().size();
    }

    @Override
    @Monitor
    @RetryFailure(retryCount=1)
    public void quit(CloseSession type) {
        switch (type) {
        case QUIT:
            webDriver().quit();
            break;
        case CLOSE:
            webDriver().close();
            break;
        default:
            webDriver().quit();
            break;
        }
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.WindowsController#switchToFrame(java.lang.String)
     */
    @Override
    @RetryFailure(retryCount=1)
    public void switchToFrame(String frameId) {
        webDriver().switchTo().frame(frameId);
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.WindowsController#goBack()
     */
    @Override
    @RetryFailure(retryCount=1)
    public void goBack() {
        webDriver().navigate().back();
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.WindowsController#goForward()
     */
    @Override
    @RetryFailure(retryCount=1)
    public void goForward() {
        webDriver().navigate().forward();
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.MainController#findChildElements(java.lang.Object, java.lang.Object)
     */
    @Override
    @WaitCondition(waitFor.PRESENCE)
    public List<WebElement> findChildElements(Object parent, String child) {
        List<WebElement> children=SessionContext.getSession().getWebElement().findElements(Locators.findByLocator(child).setLocator(child));
        return children;
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.MainController#rowsTable(java.lang.Object)
     */
    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public int getrowsTable(Object tableLocator) {
        return SessionContext.getSession().getWebElement().findElements(By.cssSelector("tbody tr")).size();
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.MainController#columnsTable(java.lang.Object)
     */
    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public int getcolumnsTable(Object tableLocator) {
        return SessionContext.getSession().getWebElement().findElements(ByJQuerySelector.ByJQuery("tbody tr:nth-child(1) td")).size();
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.MainController#getFirstSelectedOption(java.lang.Object)
     */
    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public String getFirstSelectedOptionText(Object locator) {
        String selectedText=new Select(SessionContext.getSession().getWebElement()).getFirstSelectedOption().getText();
        return selectedText;
    }

    /* (non-Javadoc)
     * @see com.automation.seletest.core.selenium.webAPI.interfaces.MainController#getAllOptionsText(java.lang.Object)
     */
    @Override
    @WaitCondition(waitFor.PRESENCE)
    @RetryFailure(retryCount=1)
    public List<String> getAllOptionsText(Object locator) {
        List<String> optionValues = new ArrayList<String>();
        List<WebElement> list=new Select(SessionContext.getSession().getWebElement()).getAllSelectedOptions();
        for (WebElement element:list) {
            optionValues.add(element.getText());
        }
        return optionValues;
    }

}

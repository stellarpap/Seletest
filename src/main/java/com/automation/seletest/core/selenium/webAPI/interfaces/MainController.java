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
package com.automation.seletest.core.selenium.webAPI.interfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;

/**
 * This interface is for all methods used by webdriver to interact with app UI
 * @author Giannis Papadakis(mailTo:gpapadakis84@gmail.com)
 * @param <T>
 */
public interface MainController<T extends MainController<T>>{

    /**
     * Finds a web element
     * @param locator
     * @return WebElement the element found
     */
    WebElement findElement(Object locator);

    /**
     * Click function
     * @param locator
     * @param timeout
     * @return MainController
     */
    T click(Object locator);

    /**
     * Enter function
     * @param locator
     * @param text
     * @param timeout
     * @return MainController
     */
    T type(Object locator, String text);


    /**
     * Quit browser type
     * @author Giannis Papadakis(mailTo:gpapadakis84@gmail.com)
     *
     */
    public enum CloseSession{QUIT,CLOSE};


    /**
     * Gets URL
     * @param url
     */
    T goToTargetHost(String url);

    /**
     * Change style of specific element
     * @param attribute
     * @param locator
     * @param attributevalue
     */
    T changeStyle(Object locator, String attribute, String attributevalue);

    /**
     * Takes screenshot
     * @throws IOException
     */
    T takeScreenShot() throws IOException;

    /**
     * Take screenshot of an element
     * @param locator
     * @throws IOException
     */
    T takeScreenShotOfElement(Object locator) throws IOException;


    /**
     * Returns text of element
     * @param locator
     * @return String text of element
     */
    String getText(Object locator);

    /**
     * Gets the tagName of the element
     * @param locator
     * @return tagName
     */
    String getTagName(Object locator);

    /**
     * Gets the element Location
     * @param locator
     * @return Point Location of element
     */
    Point getLocation(Object locator);

    /**
     * Gets the element Dimensions
     * @param locator
     * @return Dimension for Element Dimensions
     */
    Dimension getElementDimensions(Object locator);

    /**
     * Returns the source of the current page
     * @return html source
     */
    String getPageSource();

    /**
     * Defines if a web element is present
     * @param locator
     * @return true if a web element is present
     */
    boolean isWebElementPresent(String locator);

    /**
     * Defines if text is present in source
     * @param text
     * @return false if not text present in page source
     */
    boolean isTextPresent(String text);

    /**
     * Defines if a web element is visible
     * @param locator
     * @return true if a web element is visible
     */
    boolean isWebElementVisible(Object locator);

    /**
     * Upload a file in dialog box
     * @param path
     * @param element
     */
    T uploadFile(Object locator, String path);

    /**
     * Executes JavaScript on browser
     * @param script
     * @param args
     * @return Object result of JS
     */
    Object executeJS(String script, Object... args);

    /**
     * Select option from drop down by value
     * @param value
     */
    T selectByValue(String locator, String value);

    /**
     * Select option from drop down by visible text
     * @param locator
     * @param text
     */
    T selectByVisibleText(String locator, String text);

    /**
     * Deletes a cookie by name
     * @param name
     * @param value
     * @return MainController
     */
    T deleteCookieByName(String name);

    /**
     * Gets a cookie by name
     * @param name
     * @return cookie
     */
    Cookie getCookieNamed(String name);

    /**
     * Deletes a cookie
     * @param cookie
     * @return MainController
     */
    T deleteCookie(Cookie cookie);

    /**
     * Delete all cookies
     * @return MainController
     */
    T deleteAllCookies();

    /**
     * Create a cookie
     * @param name
     * @param value
     * @return MainController
     */
    T addCookie(Cookie cookie);

    /**
     * Gets all cookies
     * @return Set<Cookie>
     */
    Set<Cookie> getCookies();


    /**
     * Implicitly wait
     * @param timeout
     * @param timeunit
     */
    T implicitlyWait(long timeout,TimeUnit timeunit);

    /**
     * Timeout waiting for page to load
     * @param timeout
     * @param timeunit
     */
    T pageLoadTimeout(long timeout,TimeUnit timeunit);

    /**
     * Timeout waiting for script to finish
     * @param timeout
     * @param timeunit
     */
    T scriptLoadTimeout(long timeout,TimeUnit timeunit);

    /**
     * Set window position
     * @param point
     */
    T setWindowPosition(Point point);

    /**
     * Set window size
     * @param dimension
     */
    T setWindowDimension(Dimension dimension);

    /**
     * Gets window position
     * @return Point for window position
     */
    Point getWindowPosition();

    /**
     * Gets window dimension
     * @return
     */
    Dimension getWindowDimension();

    /**
     * Maximize window
     */
    T maximizeWindow();

    /**
     * Get logs (client,server,performance....etc.)
     * @param logtype
     * @return LogEntries the log entries
     */
    LogEntries logs(String logtype);

    /**
     * Switch to latest window
     */
    T switchToLatestWindow();

    /**
     * Gets the number of opened windows
     * @return Integer the number of opened windows
     */
    int getNumberOfOpenedWindows();

    /**
     * Accept alert
     */
    T acceptAlert();

    /**
     * Dismiss Alert
     */
    T dismissAlert();

    /**
     * Quit session or close latest window
     * @param type
     */
    T quit(CloseSession type);

    /**
     * Switch control to frame located by name or id
     * @param frameId
     */
    T switchToFrame(String frameId);

    /**
     *  Go backward
     * @return MainController
     */
    T goBack();

    /**
     * Go forward
     * @return MainController
     */

    T goForward();

    /**
     * Find all child elements of parent
     * @param parent
     * @param child
     * @return List<WebElement> with all child elements
     */
    List<WebElement> findChildElements(Object parent, String child);

    /**
     *
     * @param tableLocator
     * @return int number of Rows of a table
     */
    int getRowsTable(Object tableLocator);

    /**
     * Get the total columns of a table
     * @param tableLocator
     * @return int number of Columns of a table
     */
    int getColumnsTable(Object tableLocator);


    /**
     * Gets the selected option's text from drop down menu
     * @param locator
     * @return String the selected option's text
     */
    String getFirstSelectedOptionText(Object locator);

    /**
     * Gets the text of all options of drop down menu
     * @param locator
     * @return List<String> with all the options's text
     */
    List<String> getAllOptionsText(Object locator);

    /**
     * Deselect option  by text
     * @param locator
     * @param text
     * @return MainController
     */
    T clearSelectedOptionByText(Object locator, String text);


    /**
     * Deselect Option by Value
     * @param locator
     * @param value
     * @return MainController
     */
    T clearSelectedOption(Object locator, String value);

    /**
     * If field editable
     * @param locator
     * @return true if a field is editable
     */
    boolean isFieldEditable(Object locator);

    /**
     * If element is clickable
     * @param locator
     * @return true if element is clickable
     */
    boolean isElementClickable(Object locator);

    /**
     * If field is not editable
     * @param locator
     * @return true if field is not editable
     */
    boolean isFieldNotEditable(Object locator);

    /**
     * If element is not clickable
     * @param locator
     * @return true if element is not clickable
     */
    boolean isElementNotClickable(Object locator);

    /**
     * Download a file using URLConnection
     * @param url
     * @param filenamePrefix
     * @param fileExtension
     * @return The absolute path of the downloaded file
     * @throws IOException
     * @throws MalformedURLException
     * @throws InterruptedException
     */
    String downloadFile(String url, String filenamePrefix, String fileExtension) throws MalformedURLException, IOException, InterruptedException;

}
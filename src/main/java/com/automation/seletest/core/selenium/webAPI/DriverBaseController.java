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
package com.automation.seletest.core.selenium.webAPI;

import org.springframework.beans.factory.annotation.Autowired;

import com.automation.seletest.core.selenium.threads.SessionContext;
import com.automation.seletest.core.services.actions.WaitFor;
import com.automation.seletest.core.services.factories.StrategyFactory;

/**
 * @author Giannis Papadakis (mailTo:gpapadakis84@gmail.com)
 * @param <T>
 *
 */
@SuppressWarnings("unchecked")
public abstract class DriverBaseController<T> {

    @Autowired
    StrategyFactory<?> factoryStrategy;

    /**
     * Gets the WebDriver instance
     * @return WebDriver instance
     */
    public T webDriver(){
        return (T) SessionContext.getSession().getWebDriver();
    }

    /**
     * Gets the selenium instance
     * @return
     */
    public T selenium(){
        return (T) SessionContext.getSession().getSelenium();
    }

    /**
     * Gets the strategy for Wait<WebDriver>
     * @return Alias for Wait Strategy
     */
    public String getWait(){
        return SessionContext.getSession().getWaitStrategy();
    }

    /**
     * WaitFor Controller
     * @return WaitFor
     */
    public WaitFor waitController() {
        return factoryStrategy.getWaitStrategy(getWait());
    }

    /**
     * UiScrollable locator for android
     * @param uiSelector
     * @return UIScrollable locator
     */
    public String uiScrollable(String uiSelector) {
        return "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(" + uiSelector + ".instance(0));";
    }

}
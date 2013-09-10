package org.ourgrid.portal.test.client;

import com.thoughtworks.selenium.DefaultSelenium;

public class ConnectSelenium{
	
    private final String 	SERVER_HOST = "0.0.0.0";
    private final Integer 	SERVER_PORT = 8080;
    //private final String 	BROWSER 	= "*iexplore";
    private final String 	BROWSER 	= "*firefox";
	
    public DefaultSelenium createSeleniumClient(String url) throws Exception{
        return new DefaultSelenium(SERVER_HOST, SERVER_PORT, BROWSER, url);
    }
}

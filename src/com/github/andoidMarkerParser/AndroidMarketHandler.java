/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.andoidMarkerParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Handles action with Android Market
 *
 * @author blackbass
 */
public class AndroidMarketHandler {

    public static final String SEARCH_TYPE_PACKAGE_NAME = "pname:";
    public static final String SEARCH_TYPE_PUBLISHER_NAME = "pub:";
    public static final String MARKET_URL_SEARCH_PAGE = "https://play.google.com/store/search";
    public static final String MARKET_URL_DETAILS_PAGE = "https://market.android.com/details?id=";
    private static final String MARKET_EN_LOCALE = "en";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:10.0.2) Gecko/20100101 Firefox/10.0.2";
    private static final String SEARCH_RESULT_SECTION = "div.results-section-set";
    private static final String SEARCH_RESULT_LIST = "ul.search-results-list";
    private static final String SEARCH_ITEMS_SELECTOR = "li";

    /**
     * public static methods for private makeRequest
     *
     * @param request search request
     * @param searchType search type
     * [SEARCH_TYPE_PACKAGE_NAME,SEARCH_TYPE_PUBLISHER_NAME]
     * @return
     */
    public static List<AndroidApplication> marketSearch(String request, String searchType) {
        request = searchType + request;
        Elements applications = makeRequest(request);
        if (applications.size() > 0) {
            return getApplications(applications);
        } else {
            return null;
        }
    }

    /**
     * public static methods for private makeRequest
     *
     * @param request
     * @return
     */
    public static List<AndroidApplication> marketSearch(String request) {
        Elements applications = makeRequest(request);
        if (applications == null) {
            return null;
        }
        if (applications.size() > 0) {
            return getApplications(applications);
        } else {
            return null;
        }
    }

    /**
     * Method returns HTML after market.android/search request
     *
     * @param String request thing to search in Android market
     * @return
     */
    private static Elements makeRequest(String request) {
        try {
            request = request.replace(" ", "%20");
            request = MARKET_URL_SEARCH_PAGE + "?q=" + request + "&c=apps"
                    + getLocaleUrl(MARKET_EN_LOCALE);
            Document doc;
            // try get
            doc = Jsoup.connect(request).timeout(10000).ignoreHttpErrors(true).followRedirects(true).userAgent(USER_AGENT).get();
            Elements items = doc.select(SEARCH_RESULT_SECTION)
                    .select(SEARCH_RESULT_LIST)
                    .select(SEARCH_ITEMS_SELECTOR);
            return items;
        } catch (IOException ex) {
            System.out.println("makeRequest exception; " + ex.toString());
            return null;
        }

    }

    /**
     * add iterations application List
     *
     * @param list
     */
    private static List getApplications(Elements items) {
        List<AndroidApplication> applications = new ArrayList<AndroidApplication>();
        for (Element item : items) {
            applications.add(parseApplication(item));
        }
        return applications;
    }

    /**
     * Parse application for image caption, title
     *
     * @param item
     */
    private static AndroidApplication parseApplication(Element item) {
        Element imgElement = item.select("img").first();
        Element nameElement = item.select("a.title").first();
        Element packageNameElement = item;
        Element descriptionElement = item.select(".description").first();
        Element categoryElement = item.select("span.category a").first();
        Element priceElement = item.select("div.buy-border a").first();

        String name = nameElement.attr("title");
        String image = imgElement.attr("src");
        String packageName = packageNameElement.attr("data-docid");
        String description = "";
        if (descriptionElement != null && descriptionElement.hasText()) {
            description = descriptionElement.html();
        }
        String category = "";
        if (categoryElement != null && categoryElement.hasText()) {
            category = categoryElement.html();
        }
        String currency = priceElement.attr("data-docCurrencyCode");

        Double price = (priceElement.attr("data-isFree").equals("true")) ? 0.0 : new Double(priceElement.attr("data-docPriceMicros")) / 1000000;


        //TODO: name into base64
        return new AndroidApplication(name, image, packageName, description, category, currency, price);
    }

    /**
     * return locale url param for market request
     *
     * @param locale
     * @return
     */
    private static String getLocaleUrl(String locale) {
        return "&hl=" + locale;
    }
}

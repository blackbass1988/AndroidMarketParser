/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.andoidMarkerParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles action with Android Market
 *
 * @author blackbass
 */
public class AndroidMarketHandler {

    public static final String SEARCH_TYPE_PACKAGE_NAME = "pname:";
    public static final String SEARCH_TYPE_PUBLISHER_NAME = "pub:";
    public static final String MARKET_URL_SEARCH_PAGE = "https://play.google.com/store/search";
    public static final String MARKET_URL_DETAILS_PAGE = "https://play.google.com/store/apps/details?id=";
    private static final String MARKET_EN_LOCALE = "en";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:10.0.2) Gecko/20100101 Firefox/10.0.2";
    private static final String SEARCH_RESULT_SECTION = "div.results-section-set";
    private static final String SEARCH_RESULT_LIST = "ul.search-results-list";
    private static final String SEARCH_ITEMS_SELECTOR = "li";
    private static final int TIMEOUT = 10000;
    private static final boolean IGNORE_HTTP_ERRORS = true;
    private static final boolean FOLLOW_REDIRECTS = true;

    private static final String DETAILS_IMAGE_ELEMENT = "doc-banner-icon img";
    private static final String DETAILS_PAGE_CONTENT = "div.page-content";


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
        Elements applications;
        if (request.contains("pname:")){
            applications = makeDetailsRequest(request);
        }else{
            applications = makeRequest(request);
        }
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
     * @param request String thing to search in Android market
     * @return
     */
    private static Elements makeRequest(String request) {
        try {
            request = request.replace(" ", "%20");
            request = MARKET_URL_SEARCH_PAGE + "?q=" + request + "&c=apps"
                    + getLocaleUrl(MARKET_EN_LOCALE);
            Document doc;
            // try get
            doc = Jsoup
                    .connect(request)
                    .timeout(TIMEOUT)
                    .ignoreHttpErrors(IGNORE_HTTP_ERRORS)
                    .followRedirects(FOLLOW_REDIRECTS)
                    .userAgent(USER_AGENT).get();
            Elements items = doc.select(SEARCH_RESULT_SECTION)
                    .select(SEARCH_RESULT_LIST)
                    .select(SEARCH_ITEMS_SELECTOR);
            return items;
        } catch (IOException e) {
            System.out.println("makeRequest exception; " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static Elements makeDetailsRequest(String request) {
        try{
            request = request.replace(" ", "%20");
            request = request.replace("pname:", "");
            request = MARKET_URL_DETAILS_PAGE + request + getLocaleUrl(MARKET_EN_LOCALE);
            Document doc = Jsoup
                    .connect(request)
                    .timeout(TIMEOUT)
                    .ignoreHttpErrors(IGNORE_HTTP_ERRORS)
                    .followRedirects(FOLLOW_REDIRECTS)
                    .userAgent(USER_AGENT).get();
            Elements item = doc.select(DETAILS_PAGE_CONTENT);
            return item;
        } catch (IOException e){
            System.out.println("makeDetailsRequest exception; " + e.toString());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * add iterations application List
     *
     * @param items list
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

        if (item.attr("class").equalsIgnoreCase("page-content")){
            //is details

            Element imgElement = item.select("div.doc-banner-icon img").first();
            Element nameElement = item.select(".doc-banner-title").first();
            Element descriptionElement = item.select("div#doc-original-text").first();
            Element categoryElement = item.select("dd a").first();
            Element priceElement = item.select("div.buy-border a span.buy-offer").first();
            Element packageNameElement = priceElement;

            String name = nameElement.html();
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

            return new AndroidApplication(name, image, packageName, description, category, currency, price);

        }else{
            Element imgElement = item.select("img").first();
            Element nameElement = item.select("a.title").first();
            Element packageNameElement = item;
            Element descriptionElement = item.select(".description").first();
            Element categoryElement = item.select("span.category a").first();
            Element priceElement = item.select("div.buy-border a span.buy-offer").first();

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

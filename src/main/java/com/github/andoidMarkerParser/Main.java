package com.github.andoidMarkerParser;

import java.util.List;

/**
 *
 * @author blackbass
 */
public class Main {

    /**
     * Simple runner returner of AndroidMarketParser
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length<1) {
            System.out.println("java -jar AndoidMarketParser.jar <searchQuery>");
            return;
        }
        String request = args[0];
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);
        if (returnData == null) {
            System.out.println("request return 0 applications");
            return;
        }
        for (AndroidApplication app : returnData) {
            System.out.println("Name: " + app.getName());
            System.out.println("Image: " + app.getImage());
            System.out.println("packageName: " + app.getPackageName());
            System.out.println("description: " + app.getDescription());
            System.out.println("category: " + app.getCategory());
            System.out.println("detailSrc: " + app.getDetailsUrl());
            System.out.println("currency: " + app.getCurrency());
            System.out.println("price: " + app.getPrice());
            System.out.println("fileSize: " + app.getFileBytes());
        }
        System.out.println("Total finded: " + returnData.size());
    }
}

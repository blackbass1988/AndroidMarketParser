package com.github.andoidMarkerParser;

/**
 * Model of Android Application which is filled by AndroidMarketHandler
 *
 * @author blackbass
 */
public class AndroidApplication {

    private String name, image, packageName, description, detailsUrl, category, currency;
    private Double price;
    private Long fileBytes;

    /**
     *
     */
    public AndroidApplication() {
    }

    /**
     *
     * @param name
     * @param image
     */
    public AndroidApplication(String name, String image, String packageName, 
            String description, String category, String currency, Double price, Long fileBytes) {
        this.image = image;
        this.name = name;
        this.packageName = packageName;
        this.description = description;
        this.detailsUrl = AndroidMarketHandler.MARKET_URL_DETAILS_PAGE + packageName;
        this.category = category;
        this.currency = currency;
        this.price = price;
        this.fileBytes = fileBytes;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @param packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     * @return
     */
    public String getImage() {
        return this.image;
    }

    /**
     *
     * @return
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return this.category;
    }

    /**
     *
     * @return
     */
    public String getDetailsUrl() {
        return this.detailsUrl;
    }

    /**
     *
     * @return
     */
    public Double getPrice() {
        return this.price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Long getFileBytes() {
        return fileBytes;
    }

}

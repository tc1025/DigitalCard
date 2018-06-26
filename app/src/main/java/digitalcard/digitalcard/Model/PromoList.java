package digitalcard.digitalcard.Model;

public class PromoList {
    private String merchant, title, banner, originalPrice, discountedPrice, description, link;

    public PromoList(String merchant, String title, String link, String description) {
        this.merchant = merchant;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public PromoList(String merchant, String title, String banner, String originalPrice, String discountedPrice, String description, String link) {
        this.merchant = merchant;
        this.title = title;
        this.banner = banner;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.description = description;
        this.link = link;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}

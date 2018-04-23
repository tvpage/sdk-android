package com.tvpage.lib.model;

import java.io.Serializable;

/**
 * Created by MTPC-110 on 4/11/2017.
 */

public class ProductModel implements Serializable {
    String img_product;
    String title_product;
    String price_product;
    String product_info_url;
    String product_id;
    String product_channel_id;
    String product_video_id;
    /*"id":"83102610",                      = Product ID
                               "entityIdParent":"83094490", = Channel Id
                               "entityIdChild":"83102610",   = Video Id*/

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_channel_id() {
        return product_channel_id;
    }

    public void setProduct_channel_id(String product_channel_id) {
        this.product_channel_id = product_channel_id;
    }

    public String getProduct_video_id() {
        return product_video_id;
    }

    public void setProduct_video_id(String product_video_id) {
        this.product_video_id = product_video_id;
    }

    public String getProduct_info_url() {
        return product_info_url;
    }

    public void setProduct_info_url(String product_info_url) {
        this.product_info_url = product_info_url;
    }

    public String getImg_product() {
        return img_product;
    }

    public void setImg_product(String img_product) {
        this.img_product = img_product;
    }

    public String getTitle_product() {
        return title_product;
    }

    public void setTitle_product(String title_product) {
        this.title_product = title_product;
    }

    public String getPrice_product() {
        return price_product;
    }

    public void setPrice_product(String price_product) {
        this.price_product = price_product;
    }
}

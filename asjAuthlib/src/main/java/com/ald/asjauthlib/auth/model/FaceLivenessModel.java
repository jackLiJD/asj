package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by sean yu on 2017/7/24.
 */

public class FaceLivenessModel extends BaseModel {
    private String imageBestUrl;
    private Double confidence;
    private String thresholds;

    public String getImageBestUrl() {
        return imageBestUrl;
    }

    public void setImageBestUrl(String imageBestUrl) {
        this.imageBestUrl = imageBestUrl;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
    }
}

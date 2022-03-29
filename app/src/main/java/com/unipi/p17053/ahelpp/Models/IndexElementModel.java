package com.unipi.p17053.ahelpp.Models;

public class IndexElementModel {

    String elementId;
    String recommendationDiscl;
    String redirectTo;

    public IndexElementModel( String elementId,String recommendationDiscl, String redirectTo) {
        this.elementId = elementId;
        this.recommendationDiscl = recommendationDiscl;
        this.redirectTo = redirectTo;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getRecommendationDiscl() {
        return recommendationDiscl;
    }

    public void setRecommendationDiscl(String recommendationDiscl) {
        this.recommendationDiscl = recommendationDiscl;
    }


    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}

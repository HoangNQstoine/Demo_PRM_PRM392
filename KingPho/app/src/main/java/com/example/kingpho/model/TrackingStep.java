package com.example.kingpho.model;

public class TrackingStep {
    private String title;
    private String subtitle;
    private int iconResource;
    private String status;

    public TrackingStep(String title, String subtitle, int iconResource, String status) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResource = iconResource;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getStatus() {
        return status;
    }
}

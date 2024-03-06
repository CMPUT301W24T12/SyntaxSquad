package com.example.eventease2;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.qrcode.encoder.QRCode;

public class Event {
    private ImageView image;
    private String eventName;
    private String description;
    private String location;
    private boolean isAbleLocationTracking;
    private String duration;

    public Bitmap qrCode;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAbleLocationTracking() {
        return isAbleLocationTracking;
    }

    public void setAbleLocationTracking(boolean ableLocationTracking) {
        isAbleLocationTracking = ableLocationTracking;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public Event(ImageView image, String eventName, String description, String location, boolean isAbleLocationTracking, String duration, Bitmap qrCode) {
        this.image = image;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.isAbleLocationTracking = isAbleLocationTracking;
        this.duration = duration;
        this.qrCode = qrCode;
    }
}

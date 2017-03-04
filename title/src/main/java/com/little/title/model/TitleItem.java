package com.little.title.model;

/**
 * Created by mmh on 2017/3/1.
 */
public class TitleItem {
    public String name;
    public int textStyle;
    public int backgroundResourceId;
    public int height;//高度，单位：dp
    public int width;//宽度，单位：dp

    public TitleItem() {
    }

    public TitleItem(String name) {
        this.name = name;
    }

    public TitleItem(String name, int textStyle) {
        this.name = name;
        this.textStyle = textStyle;
    }

    public TitleItem(String name, int textStyle, int backgroundResourceId) {
        this.name = name;
        this.textStyle = textStyle;
        this.backgroundResourceId = backgroundResourceId;
    }

    public TitleItem(String name, int textStyle, int backgroundResourceId, int height, int width) {
        this.name = name;
        this.textStyle = textStyle;
        this.backgroundResourceId = backgroundResourceId;
        this.height = height;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getBackgroundResourceId() {
        return backgroundResourceId;
    }

    public void setBackgroundResourceId(int backgroundResourceId) {
        this.backgroundResourceId = backgroundResourceId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

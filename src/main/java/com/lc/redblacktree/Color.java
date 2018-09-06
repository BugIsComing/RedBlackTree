package com.lc.redblacktree;

/**
 * 枚举各种颜色
 * @author lc
 */
public enum Color {
    RED("red"),BLACK("black");
    private String color;
    Color(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}

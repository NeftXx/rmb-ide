package com.neftxx.type;

public class ComponentType extends RmbType {
    public static final ComponentType RMB_BUTTON = new ComponentType("RBton");
    public static final ComponentType RMB_LABEL = new ComponentType("RLabel");
    public static final ComponentType RMB_NUMERIC_FIELD = new ComponentType("RtxtN");
    public static final ComponentType RMB_PASSWORD_FIELD = new ComponentType("RtxtP");
    public static final ComponentType RMB_TEXT_AREA = new ComponentType("RtxtA");
    public static final ComponentType RMB_TEXT_FIELD = new ComponentType("Rtxt");

    protected ComponentType(String name) {
        super(name, 1);
    }

    @Override
    public boolean isSame(RmbType other) {
        return this == other;
    }

    @Override
    public boolean isAssignable(RmbType other) {
        if (TypeTool.isNull(other)) return true;
        return this == other;
    }

    @Override
    public String toString() {
        return name;
    }
}

package com.neftxx.type;

public class FusionType extends RmbType {
    public FusionType(String name) {
        super(name, 1);
    }

    @Override
    public boolean isSame(RmbType other) {
        if (!(other instanceof FusionType))
            return false;
        FusionType fusionType = (FusionType) other;
        return name.equals(fusionType.name);
    }

    @Override
    public boolean isAssignable(RmbType other) {
        if (TypeTool.isReserva(other)) return true;
        if (TypeTool.isNull(other)) return true;
        return isSame(other);
    }

    @Override
    public String toString() {
        return name;
    }
}

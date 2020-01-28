package com.neftxx.ast.util;

import com.neftxx.type.PrimitiveType;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;

public class Convert {
    public static int toInt(RmbType type, Object value) throws RmbException {
        if (TypeTool.isEnt(type))
            return (Integer) value;

        if (TypeTool.isChr(type))
            return (Character) value;

        if (TypeTool.isDec(type))
            return ((Double) value).intValue();

        throw new RmbException("Los tipos son incompatibles: " + type + " no se puede convertir en " + PrimitiveType.ENT);
    }

    public static double toDouble(RmbType type, Object value) throws RmbException {
        if (TypeTool.isChr(type))
            return (Character) value;

        if (TypeTool.isEnt(type))
            return (Integer) value;

        if (TypeTool.isDec(type))
            return ((Double) value);

        throw new RmbException("Los tipos son incompatibles: " + type + " no se puede convertir en " + PrimitiveType.DEC);
    }

    public static boolean toBoolean(RmbType type, Object value) throws RmbException {
        if (TypeTool.isBul(type)) {
            return (boolean) value;
        }

        if (TypeTool.isChr(type)) {
            int res = (Character) value;
            return res > 0;
        }

        if (TypeTool.isEnt(type)) {
            int res = (int) value;
            return res > 0;
        }

        throw new RmbException("Los tipos son incompatibles: " + type + " no se puede convertir en " + PrimitiveType.BUL);
    }

    public static char toChar(RmbType type, Object value) throws RmbException {
        if (TypeTool.isChr(type))
            return (Character) value;

        if (TypeTool.isEnt(type)) {
            int res = (int) value;
            return (char) res;
        }

        throw new RmbException("Los tipos son incompatibles: " + type + " no se puede convertir en " + PrimitiveType.CHR);
    }
}

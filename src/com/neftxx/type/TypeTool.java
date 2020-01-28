package com.neftxx.type;

public class TypeTool {
    public static boolean isPrimitiveType(RmbType type) {
        return type instanceof PrimitiveType;
    }

    public static boolean isEnt(RmbType type) {
        return type == PrimitiveType.ENT;
    }

    public static boolean isDec(RmbType type) {
        return type == PrimitiveType.DEC;
    }

    public static boolean isChr(RmbType type) {
        return type == PrimitiveType.CHR;
    }

    public static boolean isBul(RmbType type) {
        return type == PrimitiveType.BUL;
    }

    public static boolean isArray(RmbType type) {
        return type instanceof ArrayType;
    }

    public static boolean isUndefined(RmbType type) {
        return type == UndefinedType.UNDEFINED;
    }

    public static boolean isNull(RmbType type) {
        return type == NullType.NLO;
    }

    public static boolean isVoid(RmbType type) { return type == VoidType.ZRO; }

    public static boolean isNumeric(RmbType type) {
        return isChr(type) || isEnt(type) || isDec(type);
    }

    public static boolean isFusion(RmbType type) {
        return type instanceof FusionType;
    }

    public static boolean isReserva(RmbType type) {
        return type == NullType.RESERVA;
    }

    public static boolean isReference(RmbType type) {
        return type == NullType.REFERENCE;
    }

    public static boolean isMethod(RmbType type) {
        return type instanceof MethodType;
    }

    public static boolean isComponentType(RmbType type) {
        return type instanceof ComponentType;
    }

    public static boolean isButton(RmbType type) {
        return type == ComponentType.RMB_BUTTON;
    }

    public static boolean isLabel(RmbType type) {
        return type == ComponentType.RMB_LABEL;
    }

    public static boolean isNumericField(RmbType type) {
        return type == ComponentType.RMB_NUMERIC_FIELD;
    }

    public static boolean isPasswordField(RmbType type) {
        return type == ComponentType.RMB_PASSWORD_FIELD;
    }

    public static boolean isTextArea(RmbType type) {
        return type == ComponentType.RMB_TEXT_AREA;
    }

    public static boolean isTextField(RmbType type) {
        return type == ComponentType.RMB_TEXT_FIELD;
    }

    public static boolean isRString(RmbType type) { return type == StringType.R_STRING; }

    public static RmbType max(RmbType t1, RmbType t2) {
        if (t1 == null || t2 == null)
            return  UndefinedType.UNDEFINED;

        if (isUndefined(t1) || isUndefined(t2) || isNull(t1) || isNull(t2))
            return UndefinedType.UNDEFINED;

        if (isBul(t1) && isDec(t2) || isDec(t1) && isBul(t2))
            return UndefinedType.UNDEFINED;

        if (isBul(t1) || isBul(t2))
            return PrimitiveType.BUL;

        if (isDec(t1) || isDec(t2))
            return PrimitiveType.DEC;

        return PrimitiveType.ENT;
    }
}

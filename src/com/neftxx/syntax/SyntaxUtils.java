package com.neftxx.syntax;

import java.util.regex.Pattern;

public class SyntaxUtils {
    public static final String[] KEYWORDS = new String[]{
            "zro", "ent", "chr", "dec", "bul", "if", "else", "while", "for", "repeat", "switch", "case",
            "default", "romper", "siga", "definir", "fusion", "importar", "regresar", "when", "nlo", "Rlbl",
            "Rtxt", "RtxtA", "RtxtP", "RtxtN", "Rbton", "Rmensaje", "Rstring", "rompe"
    };

    private static final String[] PRIMITIVE_METHODS = new String[] {
            "_imp", "_pesode", "_reservar", "_atxt", "_conc", "_aent", "_adec", "_eqls", "_write", "_apend", "_wf",
            "_close", "_read", "_Nuevo_GUI", "_abrir_ventana", "_atxt"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PRIMITIVE_METHODS_PATTERN = "\\b(" + String.join("|", PRIMITIVE_METHODS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String CHAR_PATTERN = "'([^\"\\\\]|\\\\.)*'";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String OPERATION_PATTERN = "==|<>|>=|<=|->|=|>|<|%|\\+|-|\\^|\\|::|\\*";
    private static final String NUMBERS_PATTERN = "[0-9]+(\".\"[0-9]+)?";
    private static final String METHOD_PATTERN = "\\.[a-zA-Z_][a-zA-Z0-9_]+";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PRIMITIVE>" + PRIMITIVE_METHODS_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<CHAR>" + CHAR_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<OPERATION>" + OPERATION_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBERS_PATTERN + ")"
                    + "|(?<METHOD>" + METHOD_PATTERN + ")"
    );
}

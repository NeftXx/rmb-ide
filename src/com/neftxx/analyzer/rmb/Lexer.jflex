/*****************************************************************
 * Lexer.java
 *
 * Copyright ©2019 Ronald Berdúo. All Rights Reserved.
 * This software is the proprietary information of Ronald Berdúo.
 *
 *****************************************************************/
package com.neftxx.evaluator;

import com.neftxx.error.ErrorHandler;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Ronald Berdúo
 */

%%

%class Lexer
%cupsym Sym
%public
%unicode
%line
%column
%cup
%char

%{
    /**
     * Guarda el texto de las cadenas
     */
    public StringBuilder string;

    /**
     * Creador de simbolos complejos
     */
    public ComplexSymbolFactory symbolFactory;

    /*
     * Nombre del archivo que se esta analizando
     */
    public String filename;

    /**
     * Constructor del analizador lexico
     *
     * @param in Entrada que se va analizar
     * @param sf creador de simbolos complejos
     */
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf, String filename) {
	    this(in);
        string = new StringBuilder();
	    symbolFactory = sf;
        this.filename = filename;
    }

    /**
     * Metodo que devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @param value valor que recibira el simbolo
     * @param buflength tam del valor
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym, Object value, int buflength) {
        Location left = new Location(yyline + 1, yycolumn + yylength() - buflength, yychar + yylength() - buflength);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right, value);
    }

    /**
     * Metodo que devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym) {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right);
    }

    /**
     * Devuelve un nuevo java_cup.runtime.Symbol
     *
     * @param name nombre que recibira el simbolo
     * @param sym numero de token
     * @param val valor que recibira el simbolo
     * @return java_cup.runtime.Symbol
     */
    private Symbol symbol(String name, int sym, Object val) {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right= new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name, sym, left, right, val);
    }

    /**
     * Guarda los errores en el manejador
     *
     * @param message mensaje del error
     */
    private void error(String message) {
        ErrorHandler.addLexicalError(message, yyline + 1, yycolumn + 1, FilenameUtils.getName(filename));
    }
%}

%eofval{
    return symbolFactory.newSymbol(
        "EOF", Sym.EOF,
        new Location(yyline + 1, yycolumn + 1, yychar),
        new Location(yyline + 1, yycolumn + 1, yychar + 1)
    );
%eofval}

/* Definición léxica */

/**
 *  Finalización de línea:
 *
 *  Carácter salto de línea (LF ASCII)
 *  Carácter retorno de carro (CR ASCII)
 *  Carácter retorno de carro (CR ASCII) seguido de carácter salto de línea (LF ASCII)
 */
Fin_linea           = \r|\n|\r\n

/**
 *  Espacios en blanco:
 *
 *  Espacio (SP ASCII)
 *  Tabulación horizontal (HT ASCII)
 *  Caracteres de finalización de línea
 */
Espacios            = {Fin_linea} | [ \t\f]

/* Identificadores */
Identificador       = [_]*[a-zA-Z][a-zA-Z0-9_]*

/* literales */
Entero              = 0 | [1-9][0-9]*
Decimal             = [0-9]+("."[0-9]+)?
Booleano            = true|false
Nulo                = nlo

/* Estados */

/**
 *  Comentarios:
 *
 *  Los comentarios de una línea que serán delimitados al inicio con los símbolos //
 *  y al final con un carácter de finalización de línea.
 */
%state COMENTARIO_DE_FIN_DE_LINEA

/* Comentarios múltiples */
%state COMENTARIO_TRADICIONAL

/**
 * Caracter: "'" <Carácter ASCII> "'"
 * string: """ <Caracteres ASCII> """
 */
%state CARACTER
%state CADENA

%%

<YYINITIAL> {
    /* palabras claves */
    "zro"            { return symbol("zro", Sym.ZRO);                       }
    "ent"            { return symbol("zro", Sym.ENT);                       }
    "chr"            { return symbol("chr", Sym.CHR);                       }
    "dec"            { return symbol("dec", Sym.DEC);                       }
    "bul"            { return symbol("bul", Sym.BUL);                       }
    "if"             { return symbol("if",  Sym.IF);                        }
    "else"           { return symbol("else",  Sym.ELSE);                    }
    "while"          { return symbol("while", Sym.WHILE);                   }
    "for"            { return symbol("for", Sym.FOR);                       }
    "repeat"         { return symbol("repeat", Sym.REPEAT);                 }
    "switch"         { return symbol("switch", Sym.SWITCH);                 }
    "case"           { return symbol("case", Sym.CASE);                     }
    "default"        { return symbol("default", Sym.DEFAULT);               }
    "romper"|"rompe" { return symbol("romper", Sym.ROMPER);                 }
    "siga"           { return symbol("siga", Sym.SIGA);                     }
    "#definir"       { return symbol("#definir", Sym.DEFINIR);              }
    "fusion"         { return symbol("fusion", Sym.FUSION);                 }
    "#importar"      { return symbol("#importar", Sym.IMPORTAR);            }
    "regresar"       { return symbol("regresar", Sym.REGRESAR);             }
    "when"           { return symbol("when", Sym.WHEN);                     }
    "_imp"           { return symbol("_imp", Sym.IMP);                      }
    "_pesode"        { return symbol("_pesode", Sym.PESO_DE);               }
    "_reservar"      { return symbol("_reservar", Sym.RESERVAR);            }
    "_Nuevo_GUI"     { return symbol("_Nuevo_GUI", Sym.NUEVO_GUI);          }
    "_abrir_ventana" { return symbol("_abrir_ventana", Sym.ABRIR_VENTANA);  }
    "_alto_y_ancho"  { return symbol("_alto_y_ancho", Sym.ALTO_ANCHO);      }
    "_atxt"          { return symbol("_atxt", Sym.ATXT);                    }
    "_conc"          { return symbol("_conc", Sym.CONC);                    }
    "_aent"          { return symbol("_aent", Sym.AENT);                    }
    "_adec"          { return symbol("_adec", Sym.ADEC);                    }
    "_eqls"          { return symbol("_eqls", Sym.EQLS);                    }
    "_write"         { return symbol("_write", Sym.WRITE);                  }
    "_wf"            { return symbol("_wf",    Sym.WF);                     }
    "_close"         { return symbol("_close", Sym.CLOSE);                  }
    "_apend"         { return symbol("_apend", Sym.APEND);                  }
    "_read"          { return symbol("_read",  Sym.READ);                    }
    "Rmensaje"       { return symbol("Rmensaje", Sym.RMENSAJE);             }


    /* literales */
    {Entero}            {
                            int valor = 0;
                            try {
                                valor = Integer.parseInt(yytext());
                            } catch (NumberFormatException ex) {
                                error("El número " + yytext () + " esta fuera del rango de un ent.");
                            }
                            return symbol("ent(" + valor + ")", Sym.LIT_ENTERO, valor);
                        }

    {Decimal}           {
                            double valor = 0;
                            try {
                                valor = Double.parseDouble(yytext());
                            } catch (NumberFormatException ex) {
                                error("El número " + yytext () + " esta fuera del rango de un dec.");
                            }
                            return symbol("dec(" + valor + ")", Sym.LIT_DECIMAL, valor);
                        }

    {Booleano}          { return symbol(yytext(), Sym.LIT_BOOLEANO, Boolean.parseBoolean(yytext())); }

    {Nulo}              { return symbol("null", Sym.NULL); }

    \'                  { string.setLength(0); yybegin(CARACTER); }

    \"                  { string.setLength(0); yybegin(CADENA); }

    /* nombres */
    {Identificador}     { return symbol("id (" + yytext() + ")", Sym.ID, yytext()); }

    /* separadores */
    "=="                { return symbol("=", Sym.IGUAL_IGUAL); }
    "+"                 { return symbol("+", Sym.MAS); }
    "-"                 { return symbol("-", Sym.MENOS); }
    "*"                 { return symbol("*", Sym.MULT); }
    "/"                 { return symbol("/", Sym.DIV); }
    "%"                 { return symbol("%", Sym.MODULO); }
    "^"                 { return symbol("^", Sym.POTENCIA); }
    "="                 { return symbol("=", Sym.IGUAL); }
    "<>"                { return symbol("!=", Sym.DIFERENTE_QUE); }
    ">="                { return symbol(">=", Sym.MAYOR_IGUAL_QUE); }
    ">"                 { return symbol(">", Sym.MAYOR_QUE); }
    "<="                { return symbol("<=", Sym.MENOR_IGUAL_QUE); }
    "<"                 { return symbol("<", Sym.MENOR_QUE); }
    "&&"                { return symbol("&&", Sym.AND); }
    "||"                { return symbol("||", Sym.OR); }
    "!"                 { return symbol("!", Sym.NOT); }
    "("                 { return symbol("(", Sym.PAR_IZQ); }
    ")"                 { return symbol(")", Sym.PAR_DER); }
    "["                 { return symbol("[", Sym.COR_IZQ); }
    "]"                 { return symbol("]", Sym.COR_DER); }
    ";"                 { return symbol(";", Sym.PUNTO_COMA); }
    ":"                 { return symbol(":", Sym.DOS_PUNTOS); }
    ","                 { return symbol(",", Sym.COMA); }
    "."                 { return symbol(".", Sym.PUNTO); }
    "{"                 { return symbol("{", Sym.LLAVE_IZQ); }
    "}"                 { return symbol("}", Sym.LLAVE_DER); }
    "++"                { return symbol("==", Sym.INCREMENTO); }
    "--"                { return symbol("==", Sym.DECREMENTO); }

    /* espacios en blanco */
    {Espacios}          { /* IGNORAR ESPACIOS */ }

    /* comentarios */
    "/*"                { yybegin(COMENTARIO_TRADICIONAL); }
    "//"                { yybegin(COMENTARIO_DE_FIN_DE_LINEA); }
}

<COMENTARIO_TRADICIONAL> {

    "*/"                { yybegin(YYINITIAL); }

    <<EOF>>             { yybegin(YYINITIAL); }

    [^]                 { /* IGNORAR CUALQUIER COSA */ }
}

<COMENTARIO_DE_FIN_DE_LINEA> {

    {Fin_linea}             { yybegin(YYINITIAL); }
    .                       { /* IGNORAR */  }
}

<CARACTER> {
    /* fin del caracter */
    \'                  {
                            yybegin(YYINITIAL);
                            if (string.length() == 1) {
                                char x = string.charAt(0);
                                return symbol("chr(" + x + ")", Sym.LIT_CHAR, x, 1);
                            } else {
                                error("Error de salida de chr '"+ yytext () +"'.");
                                return symbol("chr(\0)", Sym.LIT_CHAR, '\0', string.length());
                            }
                        }

    /* Secuencias de escape */
    "\\'"               { string.append('\''); }
    "\\\""              { string.append('\"'); }
    "\\?"               { string.append('?'); }
    "\\\\"              { string.append('\\'); }
    "\\n"               { string.append('\n'); }
    "\\%"               { string.append('%'); }
    "\\t"               { string.append('\t'); }

    /* errores */
    \\.                 { error("Error de salida de chr '"+ yytext () +"'."); }

    {Fin_linea}         {
                            yybegin(YYINITIAL);
                            error("La expresión de caracteres no estaba cerrada.");
                        }

    /* Cualquier otra cosa esta bien */
    [^\r\n\'\\]+        { string.append(yytext()); }
}

<CADENA> {
    /* Fin de cadena */
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol("RString", Sym.LIT_STRING, string.toString(), string.length());
                        }

    /* Secuencias de escape */
    "\\'"               { string.append('\''); }
    "\\\""              { string.append('\"'); }
    "\\?"               { string.append('?'); }
    "\\\\"              { string.append('\\'); }
    "\\n"               { string.append('\n'); }
    "\\%"               { string.append("\\%"); }
    "\\t"               { string.append('\t'); }

    \\.                 { string.append(yytext()); }

    {Fin_linea}         {
                            yybegin(YYINITIAL);
                            error("La cadena no esta cerrada.");
                        }

    [^\r\n\"\\]+        { string.append(yytext()); }
}

/* Cualquier cosa que no coincida con lo de arriba es un error. */
[^] {
	error("El caracter no es valido en el lenguaje '"+ yytext () +"'.");
}
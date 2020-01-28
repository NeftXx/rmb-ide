/*****************************************************************
 * Lexer.java
 *
 * Copyright ©2019 Ronald Berdúo. All Rights Reserved.
 * This software is the proprietary information of Ronald Berdúo.
 *
 *****************************************************************/
package com.neftxx.olc;

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
%ignorecase

%{
    /**
     * Guarda el texto de las cadenas
     */
    StringBuilder string;

    /**
     * Creador de simbolos complejos
     */
    ComplexSymbolFactory symbolFactory;

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
     * @param name que recibira el simbolo
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
%state CADENA

%%

<YYINITIAL> {
    /* Palabras claves */
    "proyecto"          { return symbol("proyecto", Sym.PROYECTO);           }
    "ruta"              { return symbol("ruta", Sym.RUTA);                   }
    "nombre"            { return symbol("nombre", Sym.NOMBRE);               }
    "correr"            { return symbol("correr", Sym.CORRER);               }
    "configuracion"     { return symbol("configuracion", Sym.CONFIGURACION); }
    "archivo"           { return symbol("archivo", Sym.ARCHIVO);             }
    "fecha_mod"         { return symbol("fecha_mod", Sym.FECHA_MOD);         }
    "carpeta"           { return symbol("CARPETA", Sym.CARPETA);             }

    /* literales */
    \"                  { string.setLength(0); yybegin(CADENA); }

    /* separadores */
    "{"                 { return symbol("{", Sym.LLAVE_IZQ);  }
    "}"                 { return symbol("}", Sym.LLAVE_DER);  }
    ":"                 { return symbol(":", Sym.DOS_PUNTOS); }
    ","                 { return symbol(",", Sym.COMA);       }

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

<CADENA> {
    /* Fin de cadena */
    \"                  {
                            yybegin(YYINITIAL);
                            return symbol("String literal", Sym.STRING, string.toString(), string.length());
                        }

    \\.                 { error("Cadena no valida \""+ yytext () +"\"."); }

    {Fin_linea}         {
                            yybegin(YYINITIAL);
                            error("La cadena no esta cerrada.");
                        }

    [^\r\n\"]+        { string.append(yytext()); }
}

/* Cualquier cosa que no coincida con lo de arriba es un error. */
[^] {
	error("El caracter no es valido en el lenguaje '"+ yytext () +"'.");
}
package com.neftxx.ast.util;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.expression.array.ArrayNode;
import com.neftxx.ast.expression.array.RmbArray;
import com.neftxx.scope.Scope;
import com.neftxx.type.ArrayType;
import com.neftxx.type.PrimitiveType;
import com.neftxx.type.TypeTool;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class StringAnalyzer {
    public enum TokenType {
        WORD,
        PERCENTAGE,
        DOUBLE,
        INT,
        BOOLEAN,
        CHAR,
        STRING
    }

    public enum State {
        INITIAL,
        IS_PERCENTAGE,
        IS_WORD,
        IS_FORMAT
    }

    public static class Token {
        public TokenType tokenType;
        public String value;

        public Token(TokenType tokenType, String value) {
            this.tokenType = tokenType;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token { " +
                    "tokenType=" + tokenType +
                    ", value='" + value + '\'' +
                    " }";
        }
    }

    private static StringBuilder auxLex = new StringBuilder();;
    private static ArrayList<Token> tokens = new ArrayList<>();;
    private static State state = State.INITIAL;
    private static DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

    public static String obtenerCadena(ArrayNode arrayNode) {
        StringBuilder s = new StringBuilder();
        for (var child: arrayNode.childrenNodes) {
            s.append(child.toString());
        }
        return s.toString();
    }

    public static String getCad(AstNode astNode, ArrayList<Expression> expressions, Scope scope) {
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.######", decimalFormatSymbols);
        int size = expressions.size();
        ArrayType tempArray = new ArrayType(1, PrimitiveType.CHR);
        StringBuilder val = new StringBuilder();
        Expression expression = expressions.get(0);
        var cad = expression.interpret(scope);
        if (TypeTool.isArray(expression.type)) {
            ArrayType arrayType = (ArrayType) expression.type;
            if (arrayType.isSame(tempArray)) {
                if (cad instanceof ArrayNode) {
                    val.append(obtenerCadena((ArrayNode) cad));
                } else if (cad instanceof RmbArray) {
                    val.append(obtenerCadena(((RmbArray) cad).root));
                } else {
                    astNode.reportError("Error al intentar imprimir la expresion de tipo " + expression.type + " ya que no es de tipo " + tempArray);
                    return null;
                }
            } else {
                astNode.reportError("Error al intentar imprimir la expresion de tipo " + expression.type + " ya que no es de tipo " + tempArray);
                return null;
            }

            if (size > 1) {
                val.append("$");
                char[] characters = val.toString().toCharArray();
                var tokens = analyze(characters);
                int i = 1;
                val.setLength(0);
                for (var token : tokens) {
                    switch (token.tokenType) {
                        case WORD:
                            val.append(token.value);
                            break;
                        case PERCENTAGE:
                            val.append('%');
                            break;
                        case INT:
                            if (i < expressions.size()) {
                                var exp = expressions.get(i);
                                var expValue = exp.interpret(scope);
                                if (PrimitiveType.ENT.isSame(exp.type)) {
                                    val.append(expValue);
                                } else {
                                    astNode.reportError("Advertencia: el tipo \"" + exp.type + "\" no es de tipo \"ent\"");
                                }
                            } else {
                                astNode.reportError("Advertencia: el formato \"%e\" espera un argumento \"ent\" coincidente.");
                            }
                            i++;
                            break;
                        case CHAR:
                            if (i < expressions.size()) {
                                var exp = expressions.get(i);
                                var expValue = exp.interpret(scope);
                                if (PrimitiveType.CHR.isSame(exp.type)) {
                                    val.append(expValue);
                                } else {
                                    astNode.reportError("Advertencia: el tipo \"" + exp.type + "\" no es de tipo \"chr\"");
                                }
                            } else {
                                astNode.reportError("Advertencia: el formato \"%c\" espera un argumento \"chr\" coincidente.");
                            }
                            i++;
                            break;
                        case DOUBLE:
                            if (i < expressions.size()) {
                                var exp = expressions.get(i);
                                var expValue = exp.interpret(scope);
                                if (PrimitiveType.DEC.isSame(exp.type)) {
                                    double valTemp = 0;
                                    try {
                                        if (expValue instanceof Integer) {
                                            Integer a = (Integer) expValue;
                                            valTemp = a.doubleValue();
                                        } else {
                                            valTemp = (double) expValue;
                                        }
                                    } catch (Exception e) {
                                        astNode.reportError(e.getMessage());
                                    }
                                    val.append(format.format(valTemp));
                                } else {
                                    astNode.reportError("Advertencia: el tipo \"" + exp.type + "\" no es de tipo \"dec\"");
                                }
                            } else {
                                astNode.reportError("Advertencia: el formato \"%d\" espera un argumento \"dec\" coincidente.");
                            }
                            i++;
                            break;
                        case BOOLEAN:
                            if (i < expressions.size()) {
                                var exp = expressions.get(i);
                                var expValue = exp.interpret(scope);
                                if (PrimitiveType.BUL.isSame(exp.type)) {
                                    val.append(expValue);
                                } else {
                                    astNode.reportError("Advertencia: el tipo \"" + exp.type + "\" no es de tipo \"bul\"");
                                }
                            } else {
                                astNode.reportError("Advertencia: el formato \"%b\" espera un argumento \"bul\" coincidente.");
                            }
                            i++;
                            break;
                        case STRING:
                            if (i < expressions.size()) {
                                var exp = expressions.get(i);
                                var expValue = exp.interpret(scope);
                                if (tempArray.isSame(exp.type)) {
                                    if (expValue instanceof ArrayNode) {
                                        val.append(obtenerCadena((ArrayNode) expValue));
                                    } else if (expValue instanceof RmbArray) {
                                        val.append(obtenerCadena(((RmbArray) expValue).root));
                                    } else {
                                        astNode.reportError("Advertencia: no se pudo obtener la cadena.");
                                    }
                                } else {
                                    astNode.reportError("Advertencia: el tipo \"" + exp.type + "\" no es de tipo \"chr[]\"");
                                }
                            } else {
                                astNode.reportError("Advertencia: el formato \"%s\" espera un argumento \"chr[]\" coincidente.");
                            }
                            i++;
                            break;
                    }
                }
            }
        } else {
            astNode.reportError("Error al intentar imprimir la expresion de tipo " + expression.type + " ya que no es de tipo " + tempArray);
            return null;
        }
        return val.toString();
    }

    public static ArrayList<Token> analyze(char[] characters) {
        int i = 0;
        char currentCharacter;
        state = State.INITIAL;
        auxLex.setLength(0);
        tokens.clear();
        for (; i < characters.length; i++) {
            currentCharacter = characters[i];
            switch (state) {
                case INITIAL:
                    if (currentCharacter == '%') {
                        auxLex.append(currentCharacter);
                        state = State.IS_FORMAT;
                    } else if (currentCharacter == '\\') {
                        auxLex.append(currentCharacter);
                        state = State.IS_PERCENTAGE;
                    } else if (currentCharacter == '$' && (i == characters.length - 1)) {
                        addToken(TokenType.WORD);
                        return tokens;
                    } else {
                        auxLex.append(currentCharacter);
                        state = State.IS_WORD;
                    }
                    break;
                case IS_PERCENTAGE:
                    auxLex.append(currentCharacter);
                    if (currentCharacter == '%') {
                        addToken(TokenType.PERCENTAGE);
                    } else {
                        state = State.IS_WORD;
                    }
                    break;
                case IS_WORD:
                    if (currentCharacter == '%') {
                        addToken(TokenType.WORD);
                        auxLex.append(currentCharacter);
                        state = State.IS_FORMAT;
                    } else if (currentCharacter == '\\') {
                        addToken(TokenType.WORD);
                        auxLex.append(currentCharacter);
                        state = State.IS_PERCENTAGE;
                    } else if (currentCharacter == '$' && (i == characters.length - 1)) {
                        addToken(TokenType.WORD);
                        return tokens;
                    }  else {
                        auxLex.append(currentCharacter);
                    }
                    break;
                case IS_FORMAT:
                    if (currentCharacter == 'd' || currentCharacter == 'D') {
                        auxLex.append(currentCharacter);
                        addToken(TokenType.DOUBLE);
                    } else if (currentCharacter == 'e' || currentCharacter == 'E') {
                        auxLex.append(currentCharacter);
                        addToken(TokenType.INT);
                    } else if (currentCharacter == 'b' || currentCharacter == 'B') {
                        auxLex.append(currentCharacter);
                        addToken(TokenType.BOOLEAN);
                    } else if (currentCharacter == 'c' || currentCharacter == 'C') {
                        auxLex.append(currentCharacter);
                        addToken(TokenType.CHAR);
                    } else if (currentCharacter == 's' || currentCharacter =='S') {
                        auxLex.append(currentCharacter);
                        addToken(TokenType.STRING);
                    } else {
                        auxLex.append(currentCharacter);
                        state = State.IS_WORD;
                    }
            }
        }
        return tokens;
    }

    private static void addToken(TokenType tokenType) {
        tokens.add(new Token(tokenType, auxLex.toString()));
        auxLex.setLength(0);
        state = State.INITIAL;
    }
}

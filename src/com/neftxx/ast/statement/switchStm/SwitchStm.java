package com.neftxx.ast.statement.switchStm;

import com.neftxx.ast.AstNode;
import com.neftxx.ast.expression.Expression;
import com.neftxx.ast.statement.BreakStm;
import com.neftxx.ast.statement.ContinueStm;
import com.neftxx.ast.statement.ReturnStm;
import com.neftxx.ast.util.Convert;
import com.neftxx.ast.util.RmbException;
import com.neftxx.scope.Scope;
import com.neftxx.type.RmbType;
import com.neftxx.type.TypeTool;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class SwitchStm extends AstNode {
    public Expression expression;
    public ArrayList<CaseStm> caseStms;

    public SwitchStm(NodeInfo info, Expression expression, ArrayList<CaseStm> caseStms) {
        super(info);
        this.expression = expression;
        this.caseStms = caseStms;
    }

    @Override
    public Object interpret(Scope scope) {
        Object valSwitch = expression.interpret(scope);
        if (TypeTool.isPrimitiveType(expression.type)) {
            var typeSwitch = expression.type;
            if (caseStms != null) {
                int i = 0;
                i = caseStms.stream().filter((caseStm -> caseStm.expression == null)).map(_item -> 1).reduce(i, Integer::sum);
                if (i > 1) {
                    reportError("El switch solamente puede contener un default. "
                            + "Este switch contiene " + i + " casos default.");
                    return null;
                }

                Scope scopeLocal = new Scope(scope);
                scopeLocal.setGlobal(scope.getGlobal());

                boolean foundCase = false;
                Object currentValueCase;
                RmbType expType;
                CaseStm caseStmDefault = null;
                try {
                    for (var caseStm: caseStms) {
                        if (caseStm.expression != null && !foundCase) {
                            currentValueCase = caseStm.expression.interpret(scopeLocal);
                            expType = caseStm.expression.type;
                            if (typeSwitch.isAssignable(expType)) {
                                if (TypeTool.isEnt(typeSwitch)) {
                                    currentValueCase = Convert.toInt(expType, currentValueCase);
                                } else if (TypeTool.isChr(typeSwitch)) {
                                    currentValueCase = Convert.toChar(expType, currentValueCase);
                                } else if (TypeTool.isBul(typeSwitch)) {
                                    currentValueCase = Convert.toBoolean(expType, currentValueCase);
                                } else if (TypeTool.isDec(typeSwitch)) {
                                    currentValueCase = Convert.toDouble(expType, currentValueCase);
                                }

                                if (valSwitch.equals(currentValueCase)) {
                                    foundCase = true;
                                }
                            }
                        }

                        if (caseStm.expression == null) {
                            caseStmDefault = caseStm;
                        }

                        if (foundCase) {
                            currentValueCase = caseStm.interpret(scopeLocal);
                            if (currentValueCase instanceof BreakStm) {
                                return null;
                            }

                            if (currentValueCase instanceof ReturnStm || currentValueCase instanceof ContinueStm) {
                                return currentValueCase;
                            }
                        }
                    }

                    if (!foundCase && caseStmDefault != null) {
                        currentValueCase = caseStmDefault.interpret(scopeLocal);
                        if (currentValueCase instanceof BreakStm) {
                            return null;
                        }

                        if (currentValueCase instanceof ReturnStm || currentValueCase instanceof ContinueStm) {
                            return currentValueCase;
                        }
                    }
                } catch (RmbException e) {
                    reportError(e.getMessage());
                    return null;
                }
            }
            return null;
        }
        reportError("La expresion dada en el switch no es de tipo primitivo.");
        return null;
    }
}

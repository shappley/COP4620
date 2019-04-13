package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class AdditiveExpression extends Node {
    private Term term;
    private AdditiveExpressionPrime additiveExpressionPrime;

    public AdditiveExpression(Term term, AdditiveExpressionPrime additiveExpressionPrime) {
        this.term = term;
        this.additiveExpressionPrime = additiveExpressionPrime;
    }

    public String getTermLiteral() {
        return term.getFactorLiteral();
    }

    @Override
    public boolean isValid(Scope scope) {
        Symbol.Type type = evaluateType(scope);
        //if the left-side of the function is not null, then it needs to be INT or FLOAT
        //you can't add VOIDs
        return (additiveExpressionPrime == null || (type == Symbol.Type.INT || type == Symbol.Type.FLOAT))
                && term.isValid(scope)
                && (additiveExpressionPrime == null || additiveExpressionPrime.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope, term, additiveExpressionPrime);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        String termLiteral = getTermLiteral();
        Quadruple instruction = null;
        list.addAll(term.getInstructions(gen));
        if (termLiteral != null) {
            instruction = new Quadruple(-1, null, termLiteral, null, null);
        }
        if (additiveExpressionPrime != null) {
            list.addAll(additiveExpressionPrime.getInstructions(gen, instruction));
        }
        return list;
    }
}

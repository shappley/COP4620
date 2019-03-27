package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public class AdditiveExpression extends Node {
    private Term term;
    private AdditiveExpressionPrime additiveExpressionPrime;

    public AdditiveExpression(Term term, AdditiveExpressionPrime additiveExpressionPrime) {
        this.term = term;
        this.additiveExpressionPrime = additiveExpressionPrime;
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
}

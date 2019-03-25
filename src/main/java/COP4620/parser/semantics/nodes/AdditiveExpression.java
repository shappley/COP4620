package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class AdditiveExpression extends Node {
    private Term term;
    private AdditiveExpressionPrime additiveExpressionPrime;

    public AdditiveExpression(Term term, AdditiveExpressionPrime additiveExpressionPrime) {
        this.term = term;
        this.additiveExpressionPrime = additiveExpressionPrime;
    }

    @Override
    public boolean isValid(Scope scope) {
        return term.isValid(scope) && (additiveExpressionPrime == null || additiveExpressionPrime.isValid(scope));
    }
}

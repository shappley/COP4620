package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.SymbolTable;

public class AdditiveExpressionPrime extends Node {
    private Addop addop;
    private Term term;
    private AdditiveExpressionPrime additiveExpressionPrime;

    public AdditiveExpressionPrime(Addop addop, Term term, AdditiveExpressionPrime additiveExpressionPrime) {
        this.addop = addop;
        this.term = term;
        this.additiveExpressionPrime = additiveExpressionPrime;
    }

    @Override
    public boolean isValid(Scope scope) {
        return addop.isValid(scope) && term.isValid(scope)
                && (additiveExpressionPrime == null || additiveExpressionPrime.isValid(scope));
    }
}

package COP4620.tree.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public class Term extends Node {
    private Factor factor;
    private TermPrime termPrime;

    public Term(Factor factor, TermPrime termPrime) {
        this.factor = factor;
        this.termPrime = termPrime;
    }

    @Override
    public boolean isValid(Scope scope) {
        return factor.isValid(scope) && (termPrime == null || termPrime.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope,factor,termPrime);
    }
}

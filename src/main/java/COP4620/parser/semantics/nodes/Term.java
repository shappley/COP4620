package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

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
}

package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class TermPrime extends Node {
    private Mulop mulop;
    private Factor factor;
    private TermPrime termPrime;

    public TermPrime(Mulop mulop, Factor factor, TermPrime termPrime) {
        this.mulop = mulop;
        this.factor = factor;
        this.termPrime = termPrime;
    }

    @Override
    public boolean isValid(Scope scope) {
        return factor.isValid(scope) && (termPrime == null || termPrime.isValid(scope));
    }
}

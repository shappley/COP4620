package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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
        return evaluateType(scope) != null && factor.isValid(scope)
                && (termPrime == null || termPrime.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope, factor, termPrime);
    }
}

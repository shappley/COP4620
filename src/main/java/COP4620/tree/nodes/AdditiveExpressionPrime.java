package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

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
        return evaluateType(scope) != null && addop.isValid(scope) && term.isValid(scope)
                && (additiveExpressionPrime == null || additiveExpressionPrime.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope, term, additiveExpressionPrime);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen, Quadruple instruction) {
        List<Quadruple> list = new ArrayList<>();
        return list;
    }
}

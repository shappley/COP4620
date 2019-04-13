package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Term extends Node {
    private Factor factor;
    private TermPrime termPrime;

    public Term(Factor factor, TermPrime termPrime) {
        this.factor = factor;
        this.termPrime = termPrime;
    }

    public String getFactorLiteral() {
        return factor.getTokenValue();
    }

    @Override
    public boolean isValid(Scope scope) {
        return factor.isValid(scope) && (termPrime == null || termPrime.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope, factor, termPrime);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        String factorLiteral = getFactorLiteral();
        Quadruple instruction = null;
        list.addAll(factor.getInstructions(gen));
        if (factorLiteral != null) {
            instruction = new Quadruple(-1, null, factorLiteral, null, null);
        }
        if (termPrime != null) {
            list.addAll(termPrime.getInstructions(gen, instruction));
        }
        return list;
    }
}
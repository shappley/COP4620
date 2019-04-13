package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class TermPrime extends Node {
    private Mulop mulop;
    private Factor factor;
    private TermPrime termPrime;

    public TermPrime(Mulop mulop, Factor factor, TermPrime termPrime) {
        this.mulop = mulop;
        this.factor = factor;
        this.termPrime = termPrime;
    }

    public String getFactorLiteral() {
        return factor.getTokenValue();
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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen, Quadruple instruction) {
        List<Quadruple> list = new ArrayList<>();
        String factorLiteral = getFactorLiteral();
        list.addAll(factor.getInstructions(gen));
        if (factorLiteral != null && instruction != null) {
            instruction.setLine(gen.nextLine());
            instruction.setOperation(Operation.getAddOp(mulop.getValue()));
            instruction.setRightValue(factorLiteral);
            instruction.setDestination(gen.getNextTempVariable());
            list.add(instruction);
        }
        if (termPrime != null) {
            list.addAll(termPrime.getInstructions(gen, new Quadruple(-1, null, gen.getLastTempVariable(), null, null)));
        }
        return list;
    }
}

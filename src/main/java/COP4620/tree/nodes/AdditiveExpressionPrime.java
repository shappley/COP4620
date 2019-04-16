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
    private String expressionValue;

    public AdditiveExpressionPrime(Addop addop, Term term, AdditiveExpressionPrime additiveExpressionPrime) {
        this.addop = addop;
        this.term = term;
        this.additiveExpressionPrime = additiveExpressionPrime;
    }

    public String getExpressionValue(CodeGenerator gen) {
        if (expressionValue == null) {
            expressionValue = gen.getNextTempVariable();
        }
        return expressionValue;
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
        Quadruple termInstruction = new Quadruple(-1, null, null, null, null);
        List<Quadruple> list = new ArrayList<>(term.getInstructions(gen, termInstruction));
        instruction.setLine(gen.nextLine());
        instruction.setOperation(Operation.getAddOp(addop.getValue()));

        if (termInstruction.getDestination() != null) {
            instruction.setRightValue(termInstruction.getDestination());
        } else {
            instruction.setRightValue(term.getExpressionValue(gen));
        }

        instruction.setDestination(getExpressionValue(gen));
        list.add(instruction);
        if (additiveExpressionPrime != null) {
            list.addAll(additiveExpressionPrime.getInstructions(gen, new Quadruple(-1, null, gen.getLastTempVariable(), null, null)));
        }
        return list;
    }
}

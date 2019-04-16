package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Call extends Node {
    private String id;
    private Args args;

    public Call(String id, Args args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public boolean isValid(Scope scope) {
        FunctionSymbol function = scope.getFunction(id);
        if (args != null) {
            args.forFunction(function);
        } else if (function.parameters() != 0) {
            return false;
        }
        return function != null && (args == null || args.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return scope.getTypeOf(id);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        String argsCount = "0";
        if (args != null) {
            argsCount = String.valueOf(args.getArgsCount());
            list.addAll(args.getInstructions(gen));
        }
        list.add(new Quadruple(gen.nextLine(), Operation.CALL, id, argsCount, gen.getNextTempVariable()));
        return list;
    }
}

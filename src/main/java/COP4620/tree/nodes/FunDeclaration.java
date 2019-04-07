package COP4620.tree.nodes;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class FunDeclaration extends Node {
    private TypeSpecifier type;
    private String id;
    private Params params;
    private CompoundStmt body;

    public FunDeclaration(TypeSpecifier type, String id, Params params, CompoundStmt body) {
        this.type = type;
        this.id = id;
        this.params = params;
        this.body = body;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (scope.hasFunction("main") || scope.hasFunction(id)) {
            //main must be last declaration, and this function can't have been declared before
            return false;
        } else if (id.equals("main") && getType() != Symbol.Type.VOID) {
            return false;
        }
        scope.addScope();
        FunctionSymbol function = scope.addFunctionDeclaration(id, getType(), new Symbol[0]);
        params.forFunction(function);
        body.forFunction(function);
        return params.isValid(scope) && body.isValid(scope, false)
                && (getType() == Symbol.Type.VOID || function.hasReturn());
    }

    private Symbol.Type getType() {
        return Symbol.Type.valueOf(type.getValue().toUpperCase());
    }

    @Override
    public List<Quadruple> getInstructions() {
        List<Quadruple> instructions = new ArrayList<>();
        instructions.add(new Quadruple(-1, Operation.FUNC, id, getType().toString(), String.valueOf(params.getParameterCount())));
        instructions.addAll(body.getInstructions());
        instructions.add(new Quadruple(-1, Operation.END, Operation.FUNC.name(), id, ""));
        return instructions;
    }
}

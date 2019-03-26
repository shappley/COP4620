package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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
        }
        scope.addScope();
        FunctionSymbol function = scope.addFunctionDeclaration(id, getType(), new Symbol[0]);
        params.forFunction(function);
        body.forFunction(function);
        if (params.isValid(scope) && body.isValid(scope)) {
            scope.removeScope();
            return getType() == Symbol.Type.VOID || function.hasReturn();
        }
        return false;
    }

    private Symbol.Type getType() {
        return Symbol.Type.valueOf(type.getValue().toUpperCase());
    }
}

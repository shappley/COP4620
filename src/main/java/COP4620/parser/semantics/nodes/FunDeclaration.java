package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

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
        return params.isValid(scope)
                && body.isValid(scope);
    }
}

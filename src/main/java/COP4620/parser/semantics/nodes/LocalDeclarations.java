package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class LocalDeclarations extends Node {
    private VarDeclaration varDeclaration;
    private LocalDeclarations localDeclarations;

    public LocalDeclarations(VarDeclaration varDeclaration, LocalDeclarations localDeclarations) {
        this.varDeclaration = varDeclaration;
        this.localDeclarations = localDeclarations;
    }

    @Override
    public boolean isValid(Scope scope) {
        return varDeclaration.isValid(scope)
                && (localDeclarations == null || localDeclarations.isValid(scope));
    }
}

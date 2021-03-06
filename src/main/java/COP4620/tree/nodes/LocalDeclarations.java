package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.List;

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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = varDeclaration.getInstructions(gen);
        if (localDeclarations != null) {
            list.addAll(localDeclarations.getInstructions(gen));
        }
        return list;
    }
}

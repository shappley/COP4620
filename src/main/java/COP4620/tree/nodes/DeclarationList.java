package COP4620.tree.nodes;

import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.List;

public class DeclarationList extends Node {
    private Declaration declaration;
    private DeclarationList declarationList;

    public DeclarationList(Declaration declaration, DeclarationList declarationList) {
        this.declaration = declaration;
        this.declarationList = declarationList;
    }

    @Override
    public boolean isValid(Scope scope) {
        return declaration.isValid(scope)
                && (declarationList == null || declarationList.isValid(scope))
                && (declarationList != null || scope.hasFunction("main"));
    }

    @Override
    public List<Quadruple> getInstructions() {
        List<Quadruple> instructions = declaration.getInstructions();
        if (declarationList != null) {
            instructions.addAll(declarationList.getInstructions());
        }
        return instructions;
    }
}

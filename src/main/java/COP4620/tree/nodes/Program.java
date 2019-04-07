package COP4620.tree.nodes;

import COP4620.parser.Scope;
import COP4620.parser.SymbolTable;

public class Program extends Node {
    private DeclarationList declarationList;

    public Program(DeclarationList declarationList) {
        this.declarationList = declarationList;
    }

    public DeclarationList getDeclarationList() {
        return declarationList;
    }

    @Override
    public boolean isValid(Scope scope) {
        return declarationList != null && declarationList.isValid(scope);
    }
}

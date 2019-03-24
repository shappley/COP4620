package COP4620.parser.semantics.nodes;

public class Program extends Node {
    private DeclarationList declarationList;

    public Program(DeclarationList declarationList) {
        this.declarationList = declarationList;
    }

    public DeclarationList getDeclarationList() {
        return declarationList;
    }

    @Override
    public boolean isValid() {
        return declarationList != null && declarationList.isValid();
    }
}

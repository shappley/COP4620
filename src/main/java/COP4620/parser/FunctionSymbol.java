package COP4620.parser;

public class FunctionSymbol extends Symbol {
    private SymbolTable parameters;

    public FunctionSymbol(String id, Type type, SymbolTable parameters) {
        super(id, type);
        this.parameters = parameters;
    }

    public SymbolTable getParameters() {
        return parameters;
    }
}

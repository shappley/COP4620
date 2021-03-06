package COP4620.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionSymbol extends Symbol {
    private List<Symbol> parameters;
    private int paramIndex = 0;
    private boolean hasReturn;

    public FunctionSymbol(String id, Type type, List<Symbol> parameters) {
        super(id, type);
        this.parameters = parameters;
    }

    public FunctionSymbol(String id, Type type, Symbol[] parameters) {
        this(id, type, new ArrayList<>(Arrays.asList(parameters)));
    }

    public boolean addParameter(Symbol symbol) {
        for (Symbol s : parameters) {
            if (s.getId().equals(symbol.getId())) {
                return false;
            }
        }
        parameters.add(symbol);
        return true;
    }

    public boolean matchParameter(Type type) {
        return paramIndex < parameters.size()
                && parameters.get(paramIndex++).getType() == type;
    }

    public boolean allParametersMatched() {
        return paramIndex == parameters.size();
    }

    public int parameters() {
        return parameters.size();
    }

    public void hasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public boolean hasReturn() {
        return hasReturn;
    }

    public FunctionSymbol copy() {
        return new FunctionSymbol(getId(), getType(), parameters);
    }
}

package COP4620.parser;

import java.util.LinkedList;

public class Scope {
    private LinkedList<SymbolTable> scope;

    public Scope() {
        this.scope = new LinkedList<>();
        addScope();
    }

    public void addScope() {
        this.scope.addFirst(new SymbolTable());
    }

    public void removeScope() {
        this.scope.poll();
    }

    public boolean addDeclaration(String id, Symbol.Type type) {
        return this.scope.peekFirst().add(new Symbol(id, type));
    }

    public boolean isInLocalScope(String id) {
        return this.scope.peekFirst().contains(id);
    }

    public boolean isInScope(String id) {
        for (SymbolTable table : this.scope) {
            if (table.contains(id)) {
                return true;
            }
        }
        return false;
    }

    public Symbol.Type getTypeOf(String id) {
        for (SymbolTable table : this.scope) {
            Symbol s = table.getSymbol(id);
            if (s != null) {
                return s.getType();
            }
        }
        return null;
    }

    public FunctionSymbol getFunction(String id) {
        Symbol s = this.scope.peekLast().getSymbol(id);
        if (s != null && s instanceof FunctionSymbol) {
            return ((FunctionSymbol) s).copy();
        }
        return null;
    }

    public boolean hasFunction(String id) {
        return getFunction(id) != null;
    }

    public FunctionSymbol addFunctionDeclaration(String id, Symbol.Type type) {
        return addFunctionDeclaration(id, type, new Symbol[0]);
    }

    public FunctionSymbol addFunctionDeclaration(String id, Symbol.Type type, Symbol[] parameters) {
        FunctionSymbol function = new FunctionSymbol(id, type, parameters);
        this.scope.peekLast().add(function);
        return function;
    }

    public int depth() {
        return this.scope.size();
    }
}

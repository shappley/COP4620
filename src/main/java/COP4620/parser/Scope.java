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

    public boolean hasFunction(String id) {
        Symbol s = this.scope.peekLast().getSymbol(id);
        return s != null && s instanceof FunctionSymbol;
    }

    public boolean addFunctionDeclaration(String id, Symbol.Type type, Symbol[] parameters) {
        SymbolTable newTable = new SymbolTable();
        for (Symbol s : parameters) {
            newTable.add(s);
        }
        return this.scope.peekLast().add(new FunctionSymbol(id, type, newTable));
    }

    public int depth(){
        return this.scope.size();
    }
}

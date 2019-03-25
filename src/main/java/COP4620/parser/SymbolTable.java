package COP4620.parser;

public class SymbolTable {
    private Symbol[] table;

    public SymbolTable(int size) {
        this.table = new Symbol[size];
    }

    public SymbolTable() {
        this(16);
    }

    public boolean add(Symbol symbol) {
        final int index = indexOf(symbol.getId());
        if (index < 0) {
            table[-(index + 1)] = symbol;
            return true;
        }
        return false;
    }

    public boolean contains(String id) {
        return indexOf(id) >= 0;
    }

    public Symbol getSymbol(String id) {
        final int index = indexOf(id);
        if (index >= 0) {
            return this.table[index];
        }
        return null;
    }

    private int indexOf(String id) {
        int index = (id.hashCode() & 0x7FFFFFFF) % table.length;
        while (table[index] != null) {
            if (table[index].getId().equals(id)) {
                return index;
            }
            index = (index + 1) % table.length;
        }
        return -(index + 1);
    }
}

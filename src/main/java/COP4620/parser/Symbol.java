package COP4620.parser;

public class Symbol {
    private String id;
    private Type type;
    private String value;

    public Symbol(String id, Type type) {
        this(id, type, null);
    }

    public Symbol(String id, Type type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        INT, FLOAT, VOID, INT_ARRAY, FLOAT_ARRAY
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            Symbol other = (Symbol) obj;
            return this == other || this.getId().equals(other.getId());
        }
        return false;
    }
}

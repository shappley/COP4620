package COP4620.codegen;

public enum Operation {
    ALLOC,
    ASGN,
    FUNC,
    CALL,
    RETURN,
    PARAM,
    END,
    BLT,
    BLTE,
    BGT,
    BGTE,
    BE,
    BDE,
    COMPR,
    ADD,
    SUB,
    MUL,
    DIV;

    public static Operation getRelop(String relop) {
        switch (relop) {
            case ">":
                return BGT;
            case ">=":
                return BGTE;
            case "<":
                return BLT;
            case "<=":
                return BLTE;
            case "==":
                return BE;
            case "!=":
                return BDE;
        }
        return null;
    }

    public static Operation getNegatedRelop(String relop) {
        switch (relop) {
            case ">":
                return BLTE;
            case ">=":
                return BLT;
            case "<":
                return BGTE;
            case "<=":
                return BGT;
            case "==":
                return BDE;
            case "!=":
                return BE;
        }
        return null;
    }

    public static Operation getAddOp(String addop) {
        switch (addop) {
            case "+":
                return ADD;
            case "-":
                return SUB;
            case "*":
                return MUL;
            case "/":
                return DIV;
        }
        return null;
    }
}

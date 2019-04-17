package COP4620.codegen;

public enum Operation {
    ADD,
    ALLOC,
    ARG,
    ASGN,
    BR,
    BREQ,
    BRGE,
    BRGT,
    BRLT,
    BRLE,
    BRNEQ,
    CALL,
    COMPR,
    DISP,
    DIV,
    END,
    FUNC,
    MUL,
    PARAM,
    RETURN,
    SUB;

    public static Operation getRelop(String relop) {
        switch (relop) {
            case ">":
                return BRGT;
            case ">=":
                return BRGE;
            case "<":
                return BRLT;
            case "<=":
                return BRLE;
            case "==":
                return BREQ;
            case "!=":
                return BRNEQ;
        }
        return null;
    }

    public static Operation getNegatedRelop(String relop) {
        switch (relop) {
            case ">":
                return BRLE;
            case ">=":
                return BRLT;
            case "<":
                return BRGE;
            case "<=":
                return BRGT;
            case "==":
                return BRNEQ;
            case "!=":
                return BREQ;
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

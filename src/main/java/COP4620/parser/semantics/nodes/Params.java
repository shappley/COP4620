package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class Params extends Node {
    private ParamList paramList;

    public Params(ParamList paramList) {
        this.paramList = paramList;
    }

    public Params() {
        this(null);
    }
}

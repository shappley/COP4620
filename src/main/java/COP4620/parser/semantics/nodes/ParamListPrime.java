package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class ParamListPrime extends Node {
    private Param param;
    private ParamListPrime paramListPrime;

    public ParamListPrime(Param param, ParamListPrime paramListPrime) {
        this.param = param;
        this.paramListPrime = paramListPrime;
    }
}
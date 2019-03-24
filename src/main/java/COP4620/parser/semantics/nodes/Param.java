package COP4620.parser.semantics.nodes;

public class Param extends Node {
    public Param(TypeSpecifier typeSpec, String id, Type type){

    }

    public enum  Type {
        SINGLE, ARRAY
    }
}

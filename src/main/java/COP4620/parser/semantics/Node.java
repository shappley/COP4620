package COP4620.parser.semantics;

import COP4620.lexer.Token;

public class Node {
    private Token[] value;
    private Node[] children;

    public Node() {
        this(new Token[0]);
    }

    public Node(Token... value) {
        this(value, new Node[0]);
    }

    public Node(Node... children) {
        this(new Token[0], children);
    }

    public Node(Token[] value, Node[] children) {
        this.value = value;
        this.children = children;
    }

    public Token[] getValue() {
        return value.clone();
    }

    public Node[] getChildren() {
        return children.clone();
    }

    public boolean isValid() {
        for (Node n : getChildren()) {
            if (!n.isValid()) {
                return false;
            }
        }
        return true;
    }
}

package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;
import COP4620.parser.semantics.Node;
import COP4620.parser.semantics.nodes.ArrayDeclaration;
import COP4620.parser.semantics.nodes.VariableDeclaration;

public class Parser {
    private Token[] tokens;
    private int cursor = 0;

    public Parser(Token[] tokens) {
        this.tokens = tokens;
    }

    public boolean isValid() {
        return program() != null && isDone();
    }

    public boolean isDone() {
        return cursor == tokens.length;
    }

    //Rule #1
    public Node program() {
        Node declarationList = declarationList();
        if (declarationList != null) {
            return new Node(declarationList);
        }
        return null;
    }

    //Rule #2
    //declaration-list -> declaration declaration-list-prime
    //declaration-list-prime -> declaration declaration-list-prime | empty
    public Node declarationList() {
        Node dec, decListPrime;
        if ((dec = declaration()) != null && (decListPrime = declarationListPrime()) != null) {
            return new Node(dec, decListPrime);
        }
        return null;
    }

    private Node declarationListPrime() {
        int save = cursor;
        Node dec, decListPrime;
        if ((dec = declaration()) != null && (decListPrime = declarationListPrime()) != null) {
            return new Node(dec, decListPrime);
        }
        backtrack(save);
        return empty();
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public Node declaration() {
        int save = cursor;
        Node varDec, funDec;
        if ((varDec = varDeclaration()) != null) {
            return new Node(varDec);
        } else if (backtrack(save) && (funDec = funDeclaration()) != null) {
            return new Node(funDec);
        }
        return null;
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public VariableDeclaration varDeclaration() {
        int save = cursor;
        Node typeSpec;
        if ((typeSpec = typeSpecifier()) != null && check(0, TokenType.ID) && check(1, ";")) {
            Token id = nextToken();
            nextToken();
            return new VariableDeclaration(typeSpec, id.getValue());
        } else if (backtrack(save) && (typeSpec = typeSpecifier()) != null
                && check(0, TokenType.ID) && check(1, "[")
                && check(2, TokenType.NUM) && check(3, "]")
                && check(4, ";")) {
            Token id = nextToken();
            nextToken();
            Token size = nextToken();
            nextToken();
            nextToken();
            return new ArrayDeclaration(typeSpec, id.getValue(), size.getValue());
        }
        return null;
    }

    //Rule #5
    //type-specifier -> int | float | void
    public Node typeSpecifier() {
        if (check("int") || check("float") || check("void")) {
            return new Node(nextToken());
        }
        return null;
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public Node funDeclaration() {
        Node type, params, compound;
        if((type=typeSpecifier())!=null&&check(0,TokenType.ID)&&check(1,TokenType.ID))
        return typeSpecifier()
                && match(TokenType.ID) && match("(")
                && params() && match(")") && compoundStmt();
    }

    //Rule #7
    //params -> param-list | void
    public Node params() {
        int save = cursor;
        return (paramList())
                || (backtrack(save) && match("void"));
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public Node paramList() {
        return param() && paramListPrime();
    }

    private Node paramListPrime() {
        int save = cursor;
        return (match(",") && param() && paramListPrime())
                || (backtrack(save) && empty());
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public Node param() {
        int save = cursor;
        return (typeSpecifier() && match(TokenType.ID) && match("[") && match("]"))
                || (backtrack(save) && typeSpecifier() && match(TokenType.ID));
    }

    //compound-stmt -> { local-declarations statement-list }
    public Node compoundStmt() {
        return match("{") && localDeclarations() && statementList() && match("}");
    }

    //local-declarations -> var-declaration local-declarations | empty
    public Node localDeclarations() {
        int save = cursor;
        return (varDeclaration() && localDeclarations())
                || (backtrack(save) && empty());
    }

    //statement-list -> statement statement-list | empty
    public Node statementList() {
        int save = cursor;
        return (statement() && statementList())
                || (backtrack(save) && empty());
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public Node statement() {
        int save = cursor;
        return (expressionStmt())
                || (backtrack(save) && compoundStmt())
                || (backtrack(save) && selectionStmt())
                || (backtrack(save) && iterationStmt())
                || (backtrack(save) && returnStmt());
    }

    //expression-stmt -> expression ; | ;
    public Node expressionStmt() {
        int save = cursor;
        return (match(";"))
                || (backtrack(save) && expression() && match(";"));
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public Node selectionStmt() {
        int save = cursor;
        return (match("if") && match("(") && expression() && match(")") && statement() && match("else") && statement())
                || (backtrack(save) && match("if") && match("(") && expression() && match(")") && statement());
    }

    //iteration-stmt -> while ( expression ) statement
    public Node iterationStmt() {
        return match("while") && match("(") && expression() && match(")") && statement();
    }

    //return-stmt -> return ; | return expression ;
    public Node returnStmt() {
        int save = cursor;
        return (match("return") && match(";"))
                || (backtrack(save) && match("return") && expression() && match(";"));
    }

    //expression -> var = expression | simple-expression
    public Node expression() {
        int save = cursor;
        return (var() && match("=") && expression())
                || (backtrack(save) && simpleExpression());
    }

    //var -> ID | ID [ expression ]
    public Node var() {
        int save = cursor;
        return (match(TokenType.ID) && match("[") && expression() && match("]"))
                || (backtrack(save) && match(TokenType.ID));
    }

    //simple-expression -> additive-expression relop additive-expression | additive-expression
    public Node simpleExpression() {
        int save = cursor;
        return (additiveExpression() && relop() && additiveExpression())
                || (backtrack(save) && additiveExpression());
    }

    //relop -> <= | < | > | >= | == | !=
    public Node relop() {
        int save = cursor;
        return (match("<="))
                || (backtrack(save) && match("<"))
                || (backtrack(save) && match(">"))
                || (backtrack(save) && match(">="))
                || (backtrack(save) && match("=="))
                || (backtrack(save) && match("!="));
    }

    //additive-expression -> term additive-expression-prime
    //additive-expression-prime -> addop term additive-expression-prime | empty
    public Node additiveExpression() {
        return term() && additiveExpressionPrime();
    }

    private Node additiveExpressionPrime() {
        int save = cursor;
        return (addop() && term() && additiveExpressionPrime())
                || (backtrack(save) && empty());
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public Node term() {
        return factor() && termPrime();
    }

    private Node termPrime() {
        int save = cursor;
        return (mulop() && factor() && termPrime())
                || (backtrack(save) && empty());
    }

    //addop -> + | -
    public Node addop() {
        int save = cursor;
        return (match("+"))
                || (backtrack(save) && match("-"));
    }

    //mulop -> * | /
    public Node mulop() {
        int save = cursor;
        return (match("*"))
                || (backtrack(save) && match("/"));
    }

    //factor -> ( expression ) | var | call | NUM
    public Node factor() {
        int save = cursor;
        return (match("(") && expression() && match(")"))
                || (backtrack(save) && call())
                || (backtrack(save) && var())
                || (backtrack(save) && match(TokenType.NUM));
    }

    //call -> ID ( args )
    public Node call() {
        return match(TokenType.ID) && match("(") && args() && match(")");
    }

    //args -> arg-list | empty
    public Node args() {
        int save = cursor;
        return (argList())
                || (backtrack(save) && empty());
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public Node argList() {
        return expression() && argListPrime();
    }

    private Node argListPrime() {
        int save = cursor;
        return (match(",") && expression() && argListPrime())
                || (backtrack(save) && empty());
    }

    private boolean backtrack(int backtrack) {
        cursor = backtrack;
        return true;
    }

    private Node empty() {
        return new Node();
    }

    private boolean print(String state) {
        System.out.println(state + ", at " + cursor);
        return true;
    }

    private boolean check(String expected) {
        return check(0, expected);
    }

    private boolean check(int offset, String expected) {
        return cursor + offset < tokens.length
                && expected.equals(tokens[cursor + offset].getValue());
    }

    private boolean check(TokenType expected) {
        return check(0, expected);
    }

    private boolean check(int offset, TokenType expected) {
        return cursor + offset < tokens.length
                && expected == tokens[cursor + offset].getType();
    }

    private boolean match(String expected) {
        final Token token = nextToken();
        return token != null && expected.equals(token.getValue());
    }

    private boolean match(TokenType expected) {
        final Token token = nextToken();
        return token != null && expected == token.getType();
    }

    private Token token(int index) {
        if (index >= tokens.length) {
            return null;
        }
        return tokens[index];
    }

    private Token lookahead(int offset) {
        return token(cursor + offset);
    }

    private Token nextToken() {
        return token(cursor++);
    }
}

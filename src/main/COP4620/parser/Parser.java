package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;

public class Parser {
    private int cursor = 0;
    private int backtrack = 0;

    private Token[] tokens;

    public Parser(Token[] tokens) {
        this.tokens = tokens;
    }

    public boolean isValid() {
        return program();
    }

    //Rule #1
    public boolean program() {
        return save() && declarationList();
    }

    //Rule #2
    //declaration-list -> declaration declaration-list-prime
    //declaration-list-prime -> declaration declaration-list-prime | empty
    public boolean declarationList() {
        return (save() && declaration() && declarationListPrime());
    }

    private boolean declarationListPrime() {
        return (save() && declaration() && declarationListPrime()) || (backtrack() && save());
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public boolean declaration() {
        return (save() && varDeclaration())
                || (backtrack() && save() && funDeclaration());
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public boolean varDeclaration() {
        return ((save() && typeSpecifier() && match(TokenType.ID) && match(";"))
                || (backtrack() && save() && typeSpecifier() && match(TokenType.ID) && match("[") && match(TokenType.NUM) && match("]") && match(";")));
    }

    //Rule #5
    //type-specifier -> int | float | void
    public boolean typeSpecifier() {
        return (save() && match("int")
                || (backtrack() && save() && match("float"))
                || (backtrack() && save() && match("void")));
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public boolean funDeclaration() {
        return save() && typeSpecifier()
                && match(TokenType.ID) && match("(")
                && params() && match(")") && compoundStmt();
    }

    //Rule #7
    //params -> param-list | void
    public boolean params() {
        return (save() && paramList())
                || (backtrack() && save() && match("void"));
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public boolean paramList() {
        return save() && param() && paramListPrime();
    }

    private boolean paramListPrime() {
        return (save() && match(",") && param() && paramListPrime()) || (backtrack() && save());
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public boolean param() {
        return (save() && typeSpecifier() && match(TokenType.ID))
                || (backtrack() && save() && typeSpecifier() && match(TokenType.ID) && match("[") && match("]"));
    }

    //compound-stmt -> { local-declarations statement-list }
    public boolean compoundStmt() {
        return save() && match("{") && localDeclarations() && statementList() && match("}");
    }

    //local-declarations -> var-declaration local-declarations | empty
    public boolean localDeclarations() {
        return (save() && varDeclaration() && localDeclarations()) || (backtrack() && save());
    }

    //statement-list -> statement statement-list | empty
    public boolean statementList() {
        return (save() && statement() && statementList()) || (backtrack() && save());
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public boolean statement() {
        return (save() && expressionStmt())
                || (backtrack() && save() && compoundStmt())
                || (backtrack() && save() && selectionStmt())
                || (backtrack() && save() && selectionStmt())
                || (backtrack() && save() && iterationStmt())
                || (backtrack() && save() && returnStmt());
    }

    //expression-stmt -> expression ; | ;
    public boolean expressionStmt() {
        return (save() && expression() && match(";"))
                || (backtrack() && save() && match(";"));
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public boolean selectionStmt() {
        return (save() && match("if") && match("(") && expression() && match(")") && statement())
                || (backtrack() && save() && match("if") && match("(") && expression() && match(")") && statement() && match("else") && statement());
    }

    //iteration-stmt -> while ( expression ) statement
    public boolean iterationStmt() {
        return save() && match("while") && match("(") && expression() && match(")") && statement();
    }

    //return-stmt -> return ; | return expression ;
    public boolean returnStmt() {
        return (save() && match("return") && match(";"))
                || (backtrack() && save() && match("return") && expression() && match(";"));
    }

    //expression -> var = expression | simple-expression
    public boolean expression() {
        return (save() && var() && match("=") && expression())
                || (backtrack() && save() && simpleExpression());
    }

    //var -> ID | ID [ expression ]
    public boolean var() {
        return (save() && match(TokenType.ID))
                || (backtrack() && save() && match("[") && expression() && match("]"));
    }

    //simple-expression -> additive-expression relop additive-expression | additive-expression
    public boolean simpleExpression() {
        return (save() && additiveExpression() && relop() && additiveExpression())
                || (backtrack() && save() && additiveExpression());
    }

    //relop -> <= | < | > | >= | == | !=
    public boolean relop() {
        return (save() && match("<="))
                || (backtrack() && save() && match("<"))
                || (backtrack() && save() && match(">"))
                || (backtrack() && save() && match(">="))
                || (backtrack() && save() && match("=="))
                || (backtrack() && save() && match("!="));
    }

    //additive-expression -> term additive-expression-prime
    //additive-expression-prime -> addop term additive-expression-prime | empty
    public boolean additiveExpression() {
        return save() && term() && additiveExpressionPrime();
    }

    private boolean additiveExpressionPrime() {
        return (save() && addop() && term() && additiveExpressionPrime())
                || (backtrack() && save());
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public boolean term() {
        return save() && factor() && termPrime();
    }

    private boolean termPrime() {
        return (save() && mulop() && factor() && termPrime())
                || (backtrack() && save());
    }

    //addop -> + | -
    public boolean addop() {
        return (save() && match("+"))
                || (backtrack() && save() && match("-"));
    }

    //mulop -> * | /
    public boolean mulop() {
        return (save() && match("*"))
                || (backtrack() && save() && match("/"));
    }

    //factor -> ( expression ) | var | call | NUM
    public boolean factor() {
        return (save() && match("(") && expression() && match(")"))
                || (backtrack() && save() && var())
                || (backtrack() && save() && call())
                || (backtrack() && save() && match(TokenType.NUM));
    }

    //call -> ID ( args )
    public boolean call() {
        return save() && match(TokenType.ID) && match("(") && args() && match(")");
    }

    //args -> arg-list | empty
    public boolean args() {
        return (save() && argList())
                || (backtrack() && save());
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public boolean argList() {
        return save() && expression() && argListPrime();
    }

    private boolean argListPrime() {
        return (save() && match(",") && expression() && argListPrime())
                || (backtrack() && save());
    }

    private boolean save() {
        backtrack = cursor;
        return true;
    }

    private boolean backtrack() {
        cursor = backtrack;
        return true;
    }

    private boolean match(String expected) {
        final Token token = nextToken();
        return token != null && expected.equals(token.getValue());
    }

    private boolean match(TokenType expected) {
        final Token token = nextToken();
        return token != null && expected == token.getType();
    }

    private Token nextToken() {
        if (cursor == tokens.length) {
            return null;
        }
        return tokens[cursor++];
    }
}

package COP4620.parser;

import COP4620.lexer.Token;
import COP4620.lexer.TokenType;
import COP4620.parser.semantics.Node;
import COP4620.parser.semantics.nodes.CompoundStmt;
import COP4620.parser.semantics.nodes.Declaration;
import COP4620.parser.semantics.nodes.DeclarationList;
import COP4620.parser.semantics.nodes.FunDeclaration;
import COP4620.parser.semantics.nodes.LocalDeclarations;
import COP4620.parser.semantics.nodes.Param;
import COP4620.parser.semantics.nodes.ParamList;
import COP4620.parser.semantics.nodes.ParamListPrime;
import COP4620.parser.semantics.nodes.Params;
import COP4620.parser.semantics.nodes.Program;
import COP4620.parser.semantics.nodes.Statement;
import COP4620.parser.semantics.nodes.StatementList;
import COP4620.parser.semantics.nodes.TypeSpecifier;
import COP4620.parser.semantics.nodes.VarDeclaration;

import static COP4620.util.ArrayUtil.addArrays;
import static COP4620.util.ArrayUtil.asArray;

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
    public Program program() {
        DeclarationList declarationList = declarationList();
        if (declarationList != null) {
            return new Program(declarationList);
        }
        return null;
    }

    //Rule #2
    //declaration-list -> declaration declaration-list | declaration
    public DeclarationList declarationList() {
        int save = cursor;
        Declaration declaration = declaration();
        if (declaration != null) {
            DeclarationList declarationList = declarationList();
            if (declarationList != null) {
                return new DeclarationList(addArrays(asArray(declaration), declarationList.getDeclarations()));
            }
        }
        backtrack(save);
        return null;
    }

    //Rule #3
    //declaration -> var-declaration | fun-declaration
    public Declaration declaration() {
        int save = cursor;
        Node node = varDeclaration();
        if (node != null) {
            return new Declaration(Declaration.Type.VARIABLE, node);
        }
        backtrack(save);
        node = funDeclaration();
        if (node != null) {
            return new Declaration(Declaration.Type.FUNCTION, node);
        }
        return null;
    }

    //Rule #4
    //var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
    public VarDeclaration varDeclaration() {
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            String id = nextToken().getValue();
            if (check(";")) {
                nextToken();
                return new VarDeclaration(type, id);
            } else if (check("[") && check(1, TokenType.NUM) && check(2, "]") && check(3, ";")) {
                nextToken();
                String size = nextToken().getValue();
                nextToken();
                nextToken();
                return new VarDeclaration(type, id, size);
            }
        }
        return null;
    }

    //Rule #5
    //type-specifier -> int | float | void
    public TypeSpecifier typeSpecifier() {
        if (check("int")) {
            return new TypeSpecifier(TypeSpecifier.Type.INT, nextToken().getValue());
        } else if (check("float")) {
            return new TypeSpecifier(TypeSpecifier.Type.FLOAT, nextToken().getValue());
        } else if (check("void")) {
            return new TypeSpecifier(TypeSpecifier.Type.VOID, nextToken().getValue());
        }
        return null;
    }

    //Rule #6
    //fun-declaration -> type-specifier ID ( params ) compound-stmt
    public FunDeclaration funDeclaration() {
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            String id = nextToken().getValue();
            if (match("(")) {
                Params params = params();
                if (params != null && match(")")) {
                    CompoundStmt body = compoundStmt();
                    if (body != null) {
                        return new FunDeclaration(type, id, params, body);
                    }
                }
            }
        }
        return null;
    }

    //Rule #7
    //params -> param-list | void
    public Params params() {
        int save = cursor;
        ParamList paramList = paramList();
        if (paramList != null) {
            return new Params(paramList);
        } else if (backtrack(save) && match("void")) {
            return new Params();
        }
        return null;
    }

    //Rule #8
    //param-list -> param param-list-prime
    //param-list-prime -> , param param-list-prime | empty
    public ParamList paramList() {
        Param param = param();
        if (param != null) {
            ParamListPrime prime = paramListPrime();
            return new ParamList(param, prime);
        }
        return null;
    }

    private ParamListPrime paramListPrime() {
        int save = cursor;
        if (match(",")) {
            Param param = param();
            if (param != null) {
                ParamListPrime prime = paramListPrime();
                return new ParamListPrime(param, prime);
            }
        }
        backtrack(save);
        return null;
    }

    //param -> type-specifier ID | type-specifier ID [ ]
    public Param param() {
        int save = cursor;
        TypeSpecifier type = typeSpecifier();
        if (type != null && check(TokenType.ID)) {
            Token id = nextToken();
            if (match("[") && match("]")) {
                return new Param(type, id.getValue(), Param.Type.ARRAY);
            }
            backtrack(save);
            return new Param(type, id.getValue(), Param.Type.SINGLE);
        }
        return null;
    }

    //compound-stmt -> { local-declarations statement-list }
    public CompoundStmt compoundStmt() {
        if (match("{")) {
            LocalDeclarations localDeclarations = localDeclarations();
            StatementList statementList = statementList();
            if (match("}")) {
                return new CompoundStmt(localDeclarations, statementList);
            }
        }
        return null;
    }

    //local-declarations -> var-declaration local-declarations | empty
    public LocalDeclarations localDeclarations() {
        int save = cursor;
        VarDeclaration varDeclaration = varDeclaration();
        if (varDeclaration != null) {
            return new LocalDeclarations(varDeclaration, localDeclarations());
        }
        backtrack(save);
        return null;
    }

    //statement-list -> statement statement-list | empty
    public StatementList statementList() {
        int save = cursor;
        Statement statement = statement();
        if (statement != null) {
            return new StatementList(statement, statementList());
        }
        backtrack(save);
        return null;
    }

    //statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt
    public boolean statement() {
        int save = cursor;
        return (expressionStmt())
                || (backtrack(save) && compoundStmt())
                || (backtrack(save) && selectionStmt())
                || (backtrack(save) && iterationStmt())
                || (backtrack(save) && returnStmt());
    }

    //expression-stmt -> expression ; | ;
    public boolean expressionStmt() {
        int save = cursor;
        return (match(";"))
                || (backtrack(save) && expression() && match(";"));
    }

    //selection-stmt -> if ( expression ) statement | if ( expression ) statement else statement
    public boolean selectionStmt() {
        int save = cursor;
        return (match("if") && match("(") && expression() && match(")") && statement() && match("else") && statement())
                || (backtrack(save) && match("if") && match("(") && expression() && match(")") && statement());
    }

    //iteration-stmt -> while ( expression ) statement
    public boolean iterationStmt() {
        return match("while") && match("(") && expression() && match(")") && statement();
    }

    //return-stmt -> return ; | return expression ;
    public boolean returnStmt() {
        int save = cursor;
        return (match("return") && match(";"))
                || (backtrack(save) && match("return") && expression() && match(";"));
    }

    //expression -> var = expression | simple-expression
    public boolean expression() {
        int save = cursor;
        return (var() && match("=") && expression())
                || (backtrack(save) && simpleExpression());
    }

    //var -> ID | ID [ expression ]
    public boolean var() {
        int save = cursor;
        return (match(TokenType.ID) && match("[") && expression() && match("]"))
                || (backtrack(save) && match(TokenType.ID));
    }

    //simple-expression -> additive-expression relop additive-expression | additive-expression
    public boolean simpleExpression() {
        int save = cursor;
        return (additiveExpression() && relop() && additiveExpression())
                || (backtrack(save) && additiveExpression());
    }

    //relop -> <= | < | > | >= | == | !=
    public boolean relop() {
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
    public boolean additiveExpression() {
        return term() && additiveExpressionPrime();
    }

    private boolean additiveExpressionPrime() {
        int save = cursor;
        return (addop() && term() && additiveExpressionPrime())
                || (backtrack(save) && empty());
    }

    //term -> factor term-prime
    //term-prime -> mulop factor term-prime | empty
    public boolean term() {
        return factor() && termPrime();
    }

    private boolean termPrime() {
        int save = cursor;
        return (mulop() && factor() && termPrime())
                || (backtrack(save) && empty());
    }

    //addop -> + | -
    public boolean addop() {
        int save = cursor;
        return (match("+"))
                || (backtrack(save) && match("-"));
    }

    //mulop -> * | /
    public boolean mulop() {
        int save = cursor;
        return (match("*"))
                || (backtrack(save) && match("/"));
    }

    //factor -> ( expression ) | var | call | NUM
    public boolean factor() {
        int save = cursor;
        return (match("(") && expression() && match(")"))
                || (backtrack(save) && call())
                || (backtrack(save) && var())
                || (backtrack(save) && match(TokenType.NUM));
    }

    //call -> ID ( args )
    public boolean call() {
        return match(TokenType.ID) && match("(") && args() && match(")");
    }

    //args -> arg-list | empty
    public boolean args() {
        int save = cursor;
        return (argList())
                || (backtrack(save) && empty());
    }

    //arg-list -> expression arg-list-prime
    //arg-list-prime -> , expression arg-list-prime | empty
    public boolean argList() {
        return expression() && argListPrime();
    }

    private boolean argListPrime() {
        int save = cursor;
        return (match(",") && expression() && argListPrime())
                || (backtrack(save) && empty());
    }

    private boolean backtrack(int backtrack) {
        cursor = backtrack;
        return true;
    }

    private boolean empty() {
        return true;
    }

    private boolean print(String state) {
        System.out.println(state + ", at " + cursor);
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
}

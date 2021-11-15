import java.util.HashSet;

public class Parser {
    public Token curToken;
    public Token peekToken;
    public Lexer lexer;
    public Emitter emitter;
    public HashSet<String> symbols;
    public HashSet<String> labelsDeclared;
    public HashSet<String> labelsGotoed;

    public Parser(Lexer lexer, Emitter emitter) {
        this.lexer = lexer;
        this.emitter = emitter;

        symbols = new HashSet<>();
        labelsDeclared = new HashSet<>();
        labelsGotoed = new HashSet<>();

        curToken = null;
        peekToken = null;
        nextToken();
        nextToken();
    }

    // Return true if the current token matches
    public boolean checkToken(TokenType candidate) {
        String enumKey = candidate.getKey();
        return enumKey.equals(curToken.kind.getKey());
    }

    // Return true if the peek token matches
    public boolean checkPeek(TokenType candidate) {
        String enumKey = candidate.getKey();
        return enumKey.equals(peekToken.kind.getKey());
    }

    // Try to match current token. If not error. Move token.
    public void match(TokenType candidate) {
        if(!checkToken(candidate)) {
            abort("Expected " + candidate.getKey() + ", got " + curToken.kind);
        }
        nextToken();
    }

    // Move token to next token
    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.getToken();
    }

    // SHEESH
    public void abort(String message) {
        System.out.println("Error. " + message);
        System.exit(0);
    }

    // Our production rules
    // program ::= {statement}
    public void program() {
        emitter.headerLine("#include <stdio.h>");
        emitter.headerLine("int main(void) {");

        while(checkToken(TokenType.NEWLINE)) {
            nextToken();
        }

        while(!checkToken(TokenType.EOF)) {
            statement();
        }

        emitter.emitLine("return 0;");
        emitter.emitLine("}");

        // Check that each label referenced in GOTO is real
        for(String label : labelsGotoed) {
            if(!labelsDeclared.contains(label)) {
                abort("Attempting to GOTO to an undeclared label: " + label);
            }
        }
    }

    // Statement....
    public void statement() {
        // "PRINT" (expression || string)
        if(checkToken(TokenType.PRINT)) {
            System.out.println("STATEMENT-PRINT");
            nextToken();

            if(checkToken(TokenType.STRING)) {
                nextToken();
            } else {
                expression();
            }
        }
        // "IF" comparison "THEN" {statement} "ENDIF"
        else if(checkToken(TokenType.IF)) {
            System.out.println("STATEMENT-IF");
            nextToken();
            comparison();

            match(TokenType.THEN);
            nl();

            // Zero or more statements in the body
            while(!checkToken(TokenType.ENDIF)) {
                statement();
            }

            match(TokenType.ENDIF);
        }
        // "WHILE" comparison "REPEAT" {statement} "ENDWHILE"
        else if(checkToken(TokenType.WHILE)) {
            System.out.println("STATEMENT-WHILE");
            nextToken();
            comparison();

            match(TokenType.REPEAT);
            nl();

            // Zero or more statements in the loop body
            while(!checkToken(TokenType.ENDWHILE)) {
                statement();
            }

            match(TokenType.ENDWHILE);
        }
        // "LABEL" ident nl
        else if(checkToken(TokenType.LABEL)) {
            System.out.println("STATEMENT-LABEL");
            nextToken();

            // Check to see if this label already exists
            if(labelsDeclared.contains(curToken.text)) {
                abort("Label already exists: " + curToken.text);
            }
            labelsDeclared.add(curToken.text);

            match(TokenType.IDENT);
        }
        // "GOTO" ident nl
        else if(checkToken(TokenType.GOTO)) {
            System.out.println("STATEMENT-GOTO");
            nextToken();
            labelsGotoed.add(curToken.text);
            match(TokenType.IDENT);
        }
        // "LET" ident "=" expression nl
        else if(checkToken(TokenType.LET)) {
            nextToken();

            // Check if ident exists in symbol table
            if(!symbols.contains(curToken.text)) {
                symbols.add(curToken.text);
            }

            match(TokenType.IDENT);
            match(TokenType.EQ);
            expression();
        }
        // "INPUT" ident nl
        else if(checkToken(TokenType.INPUT)) {
            nextToken();

            // If variable doesn't exist make it
            if(!symbols.contains(curToken.text)) {
                symbols.add(curToken.text);
            }

            match(TokenType.IDENT);
        }
        // Not valid
        else {
            abort("Invalid statement at " + curToken.text + "(" + curToken.kind.getKey() + ")");
        }
        nl();
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=")expression)+
    public void comparison() {
        System.out.println("COMPARISON");

        expression();
        // Must be at least one comparison operator and another expression
        if(isComparisonOperator()) {
            nextToken();
            expression();
        } else {
            abort("Expected comparison operator at: " + curToken.text);
        }

        while(isComparisonOperator()) {
            nextToken();
            expression();
        }
    }

    // Return true if the current token is a comparison operator
    public boolean isComparisonOperator() {
        return checkToken(TokenType.GT) || checkToken(TokenType.GTEQ) ||
            checkToken(TokenType.LT) || checkToken(TokenType.LTEQ) ||
            checkToken(TokenType.EQEQ) || checkToken(TokenType.NOTEQ);
    }

    // experssion ::= term{("-" | "+") term}
    public void expression() {
        System.out.println("EXPRESSION");

        term();
        // Can have 0 or more +/- and expressions.
        while(checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)) {
            nextToken();
            term();
        }
    }

    // term ::= unary {("/" | "*") unary}
    public void term() {
        System.out.println("TERM");
        unary();

        while(checkToken(TokenType.STAR) || checkToken(TokenType.SLASH)) {
            nextToken();
            unary();
        }
    }

    // unary ::= ["+" | "-"] primary
    public void unary() {
        System.out.println("UNARY");

        // Optional unary +/-
        if(checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)) {
            nextToken();
        }
        primary();
    }

    // primary ::= number | ident
    public void primary() {
        System.out.println("Primary (" + curToken.text + ")");

        if(checkToken(TokenType.NUMBER)) {
            nextToken();
        } else if(checkToken(TokenType.IDENT)) {
            // Does it exist?
            if(!symbols.contains(curToken.text)) {
                abort("Referencing variable before assignment: " + curToken.text);
            }
            nextToken();
        } else {
            // SHEESH
            abort("Unexpected token at " + curToken.text);
        }
    }

    // nl ::= '\n'+
    public void nl() {
        System.out.println("NEWLINE");
        match(TokenType.NEWLINE);
        while(checkToken(TokenType.NEWLINE)) {
            nextToken();
        }
    }
}

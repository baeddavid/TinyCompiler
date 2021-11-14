public class Parser {
    public Token curToken;
    public Token peekToken;
    public Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
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
        System.out.println("PROGRAM");

        while(!checkToken(TokenType.EOF)) {
            statement();
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
                System.out.println("HMMMM");
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
        nl();
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

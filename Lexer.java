public class Lexer {
    public String source;
    public Character curChar;
    public int curPos;

    public Lexer(String input) {
        source = input + '\n';
        curChar = Character.MIN_VALUE;
        curPos = -1;
        nextChar();
    }

    // Process the next character
    public void nextChar() {
        curPos++;
        if(curPos >= source.length()) {
            curChar = '\0';
        } else {
            curChar = source.charAt(curPos);
        }
    }

    // Return the lookahead character
    public Character peek() {
        if(curPos + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(curPos + 1);
    }

    // Error message
    public void abort(String message) {
        System.out.println("Lexing error. " + message);
        System.exit(0);
    }

    // Skip white spaces
    public void skipWhiteSpace() {
        while(curChar == ' ' || curChar == '\t' || curChar == '\r') {
            nextChar();
        }
    }

    // Return the next token
    public Token getToken() {
        skipWhiteSpace();
        Token token = null;

        // Check the first character of the token
        // If it is a long operator like !=, number, identifier, or keyword we will proccess.
        if(curChar == '+') {
            token = new Token(String.valueOf(curChar), TokenType.PLUS);
        } else if(curChar == '-') {
            token = new Token(String.valueOf(curChar), TokenType.MINUS);
        } else if(curChar == '*') {
            token = new Token(String.valueOf(curChar), TokenType.STAR);
        } else if(curChar == '/') {
            token = new Token(String.valueOf(curChar), TokenType.SLASH);
        } else if(curChar == '\n') {
            token = new Token(String.valueOf(curChar), TokenType.NEWLINE);
        } else if(curChar == '\0') {
            token = new Token("", TokenType.EOF);
        } else {
            // WTF is this token
            abort("Unknown token: " + curChar);
        }
        nextChar();
        return token;
    }
}

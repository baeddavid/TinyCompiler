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

    // Skip comments
    public void skipComments() {
        if(curChar == '#') {
            while(curChar != '\n') {
                nextChar();
            }
        }
    }

    // Return the next token
    public Token getToken() {
        skipWhiteSpace();
        skipComments();
        Token token = null;
        String value = String.valueOf(curChar);

        // Check the first character of the token
        // If it is a long operator like !=, number, identifier, or keyword we will proccess.
        if(curChar == '+') {
            token = new Token(value, TokenType.PLUS);
        } else if(curChar == '-') {
            token = new Token(value, TokenType.MINUS);
        } else if(curChar == '*') {
            token = new Token(value, TokenType.STAR);
        } else if(curChar == '/') {
            token = new Token(value, TokenType.SLASH);
        } else if(curChar == '=') {
            // Check if this is EQ or EQEQ
            if(peek() == '=') {
                String lastChar = value;
                nextChar();
                token = new Token(lastChar + curChar, TokenType.EQEQ);
            } else {
                token = new Token(value, TokenType.EQ);
            }
        } else if(curChar == '>') {
            // Check if this is GT or GTEQ
            if(peek() == '=') {
                String lastChar = value;
                nextChar();
                token = new Token(lastChar + curChar, TokenType.GTEQ);
            } else {
                token = new Token(value, TokenType.GT);
            }
        } else if(curChar == '<') {
            // Check if this is LT or LTEQ
            if(peek() == '=') {
                String lastChar = value;
                nextChar();
                token = new Token(lastChar + curChar, TokenType.LTEQ);
            } else {
                token = new Token(value, TokenType.LT);
            }
        } else if(curChar == '!') {
            if(peek() == '=') {
                String lastChar = value;
                nextChar();
                token = new Token(lastChar + curChar, TokenType.NOTEQ);
            } else {
                abort("Expected !=, got!" + peek());
            }
        } else if(curChar == '\"') {
            // Get characters in between quotations
            nextChar();
            Integer startPos = curPos;

            while(curChar != '\"') {
                if(curChar == '\r' || curChar == '\n' || curChar == '\t' || curChar == '\\' || curChar == '%') {
                    abort("Illegal character in string");
                }
                nextChar();
            }

            String tokText = source.substring(startPos, curPos);
            token = new Token(tokText, TokenType.STRING);
        } else if(Character.isDigit(curChar)) {
            Integer startPos = curPos;
            while(Character.isDigit(peek())) {
                nextChar();
            }
            if(peek() == '.') {
                nextChar();

                // Must have one digit after decimal
                if(!Character.isDigit(peek())) {
                    // SHEESH
                    abort("Illegal character in number");
                }
                while(Character.isDigit(peek())) {
                    nextChar();
                }
            }

            String tokText = source.substring(startPos, curPos + 1);
            token = new Token(tokText, TokenType.NUMBER);
        }

        else if(curChar == '\n') {
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

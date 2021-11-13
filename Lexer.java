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
}

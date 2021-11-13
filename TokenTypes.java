enum TokenType {
    EOF("EOF", -1),
    NEWLINE("NEWLINE", 0),
    NUMBER("NUMBER", 1),
    IDENT("IDENT", 2),
    STRING("STRING", 3),
    // Keywords
    LABEL("LABEl", 101),
    GOTO("GOTO", 102),
    PRINT("PRINT", 103),
    INPUT("INPUT", 104),
    LET("LET", 105),
    IF("IF", 106),
    THEN("THEN", 107),
    ENDIF("ENDIF", 108),
    WHILE("WHILE", 109),
    REPEAT("REPEAT", 110),
    ENDWHILE("ENDWHILE", 111),
    // Operators
    EQ("EQ", 201),
    PLUS("PLUS", 202),
    MINUS("MINUS", 203),
    ASTERISK("ASTERISK", 204),
    SLASH("SLASH", 205),
    EQEQ("EQEQ", 206),
    NOTEQ("NOTEQ", 207),
    LT("LT", 208),
    LTEQ("LTEQ", 209),
    GT("GT", 210),
    GTEQ("GTEQ", 211);

    private final String key;
    private final Integer value;

    TokenType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}

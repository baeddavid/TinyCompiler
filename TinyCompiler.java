// A java based compiler that will compile Tiny into C.
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class TinyCompiler {
    public static void main(String[] args) throws IOException {
        System.out.println("Teeny Tiny Compiler");
        if(args.length < 1) {
            System.out.print("Error: Compiler needs source file as argument");
        }
        Path fileName = Path.of(args[0]);
        Lexer lex = new Lexer(Files.readString(fileName));
        Emitter emitter = new Emitter();
        Parser parser = new Parser(lex, emitter);

        parser.program();
        emitter.writeFile();
        System.out.println("Parsing Completed");
    }
}

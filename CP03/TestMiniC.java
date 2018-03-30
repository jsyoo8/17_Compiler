import java.io.IOException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TestMiniC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MiniCLexer lexer = null;
		
		try {
			lexer = new MiniCLexer(new ANTLRFileStream("test.c"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniCParser parser = new MiniCParser(tokens);
		ParseTree tree = parser.program();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new MiniCPrintListener(), tree);
	}

}

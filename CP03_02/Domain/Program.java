package Domain;

import java.util.List;
import Domain.Decl.Declaration;

public class Program extends MiniCNode{
	List<Declaration> decls;
	
	public Program(List<Declaration> decls){
		this.decls = decls;
	}
	
	@Override
	public String toString(){
		return decls.stream()
					.map(t -> t.toString() + "\n")
					.reduce("", (acc, decl) -> acc + decl);
	}
}

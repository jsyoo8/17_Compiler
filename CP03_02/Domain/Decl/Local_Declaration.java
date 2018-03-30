package Domain.Decl;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.MiniCNode;
import Domain.Type_spec.TypeSpecification;

public class Local_Declaration extends MiniCNode{
	TypeSpecification type;
	TerminalNode lhs;
	
	public Local_Declaration(TypeSpecification type, TerminalNode lhs) {
		this.type = type;
		this.lhs = lhs;
	}
	
	@Override
	public String toString(){
		return type.toString() + " " + lhs.getText() + ";";
	}
}

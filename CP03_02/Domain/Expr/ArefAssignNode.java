package Domain.Expr;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ArefAssignNode extends Expression{
	TerminalNode t_node;
	Expression lhs;
	Expression rhs;
	
	public ArefAssignNode(TerminalNode t_node, Expression lhs, Expression rhs) {
		super();
		this.t_node = t_node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public String toString(){
		return t_node.toString() + "[" + lhs.toString() + "] = " + rhs.toString();
	}
}

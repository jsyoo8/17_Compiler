
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

import Domain.ASTVisitor;
import Domain.MiniCNode;
import Domain.Program;
import Domain.Args.Arguments;
import Domain.Decl.Declaration;
import Domain.Decl.Function_Declaration;
import Domain.Decl.Local_Declaration;
import Domain.Decl.Local_Variable_Declaration_Array;
import Domain.Decl.Local_Variable_Declaration_Assign;
import Domain.Decl.Variable_Declaration;
import Domain.Decl.Variable_Declaration_Array;
import Domain.Decl.Variable_Declaration_Assign;
import Domain.Expr.ArefAssignNode;
import Domain.Expr.ArefNode;
import Domain.Expr.AssignNode;
import Domain.Expr.BinaryOpNode;
import Domain.Expr.Expression;
import Domain.Expr.FuncallNode;
import Domain.Expr.ParenExpression;
import Domain.Expr.TerminalExpression;
import Domain.Expr.UnaryOpNode;
import Domain.Param.ArrayParameter;
import Domain.Param.Parameter;
import Domain.Param.Parameters;
import Domain.Stmt.Compound_Statement;
import Domain.Stmt.Expression_Statement;
import Domain.Stmt.If_Statement;
import Domain.Stmt.Return_Statement;
import Domain.Stmt.Statement;
import Domain.Stmt.While_Statement;
import Domain.Type_spec.TypeSpecification;
import Domain.Type_spec.TypeSpecification.Type;

public class UcodeGenVisitor implements ASTVisitor {
	Stack<Integer> state = new Stack<>();
	int useState = 1;
	HashMap<String, Integer> localVariable = new HashMap<String, Integer>();
	Stack<String> localSTMT = new Stack<>();
	/*
	 * @Override public void visitProgram(Program node) { // TODO Auto-generated
	 * method stub int numOfDecl = node.decls.size();
	 * 
	 * for (int i = 0; i < numOfDecl; i++) {
	 * this.visitDeclaration(node.decls.get(i)); } String writeString =
	 * String.format("%s%s %d", this.space, "bgn", this.numOfGlobalVar);
	 * this.printU(writeString); writeString = this.space + "ldp";
	 * this.printU(writeString); writeString = this.space + "call main ";
	 * this.printU(writeString); writeString = this.space + "end";
	 * this.printU(writeString);
	 * 
	 * try { this.UCodeWriter.close(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	@Override
	public void visitProgram(Program node) {
		// TODO Auto-generated method stub
		int i = node.decls.size();
		state.push(useState++);
		while (i > 0) {
			visit(node.decls.get((node.decls.size() - i)));
			i--;
		}
		System.out.printf("\n%-6s%-6s%d", "", "bgn", (node.decls.size() - 1));
		System.out.printf("\n%-6s%s", "", "ldp");
		System.out.printf("\n%-6s%s", "", "call main ");
		System.out.printf("\n%-6s%s\n", "", "end");
	}

	@Override
	public void visitDeclaration(Declaration node) {
		// TODO Auto-generated method stub
		if (node instanceof Variable_Declaration) {
			visit((Variable_Declaration) node);
		}
		visit((Function_Declaration) node);

	}

	@Override
	public void visitFunction_Declaration(Function_Declaration node) {
		// TODO Auto-generated method stub
		state.push(useState++);
		System.out.printf("\n%-6s%-6s", node.t_node, "proc");
		visit(node.params);
		localVariable = new HashMap<String, Integer>();
		localVariable.put("size", node.compount_stmt.local_decls.size());
		System.out.printf("%d %d 2", localVariable.get("size"), state.peek());
		visit(node.compount_stmt);
	}

	@Override
	public void visitLocal_Declaration(Local_Declaration node) {
		// TODO Auto-generated method stub
		if (node instanceof Local_Variable_Declaration_Array) {
			visit((Local_Variable_Declaration_Array) node);
		} else if (node instanceof Local_Variable_Declaration_Assign) {
			visit((Local_Variable_Declaration_Assign) node);
		} else {
			localVariable.put(node.lhs.toString(), localVariable.size());
			System.out.printf("\n%-6s%-6s%d %d %d", "", "sym", state.peek(), localVariable.get(node.lhs.toString()), 1);
		}
	}

	@Override
	public void visitLocal_Variable_Declaration_Array(Local_Variable_Declaration_Array node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s%-6s", node.lhs, node.rhs);
	}

	@Override
	public void visitLocal_Variable_Declaration_Assign(Local_Variable_Declaration_Assign node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s%-6s", node.lhs, node.rhs);
	}

	@Override
	public void visitVariable_Declaration_Array(Variable_Declaration_Array node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s%-6s", node.lhs, node.rhs);
	}

	@Override
	public void visitVariable_Declaration_Assign(Variable_Declaration_Assign node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s%-6s", node.lhs, node.rhs);
	}

	@Override
	public void visitVariable_Declaration(Variable_Declaration node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s", node.lhs);

	}

	@Override
	public void visitArguments(Arguments node) {
		// TODO Auto-generated method stub
		int count = node.exprs.size();
		for (int i = 0; i < count; i++) {
			visit(node.exprs.get(i));
		}
	}

	@Override
	public void visitExpression(Expression node) {
		// TODO Auto-generated method stub
		if (node instanceof TerminalExpression) {
			visit((TerminalExpression) node);
		} else if (node instanceof UnaryOpNode) {
			visit((UnaryOpNode) node);
		} else if (node instanceof AssignNode) {
			visit((AssignNode) node);
		} else if (node instanceof ParenExpression) {
			visit((ParenExpression) node);
		} else if (node instanceof BinaryOpNode) {
			visit((BinaryOpNode) node);
		} else if (node instanceof FuncallNode) {
			visit((FuncallNode) node);
		} else if (node instanceof ArefNode) {
			visit((ArefNode) node);
		} else {
			visit((ArefAssignNode) node);
		}
	}

	@Override
	public void visitArefAssignNode(ArefAssignNode node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s%-6s", node.t_node, "sym");
		visit(node.lhs);
		visit(node.rhs);

	}

	@Override
	public void visitArefNode(ArefNode node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s%-6s", node.t_node, "sym");
		visit(node.expr);
	}

	@Override
	public void visitAssignNode(AssignNode node) {
		// TODO Auto-generated method stub
		if (node.expr instanceof BinaryOpNode) {
			visit(node.expr);
			System.out.printf("\n%-6s%-6s%s", "", "ldc", localVariable.get(node.t_node.toString()));
		} else {
			System.out.printf("\n%-6s%-6s", "", "ldc");
			visit(node.expr);
			System.out.printf("\n%-6s%-6s%d %d", "", "str", state.peek(), localVariable.get(node.t_node.toString()));
		}
	}

	@Override
	public void visitBinaryOpNode(BinaryOpNode node) {
		// TODO Auto-generated method stub
		String op;
		System.out.printf("\n%-6s%-6s%d ", "", "lod", state.peek());
		visit(node.lhs);
		if ((node.op.equals(">")) || (node.op.equals("<")) || (node.op.equals(">=")) || (node.op.equals("<="))
				|| (node.op.equals("==")) || (node.op.equals("!="))) {
			System.out.printf("\n%-6s%-6s", "", "ldc");
			visit(node.rhs);
			if (node.op.equals(">")) {
				op = "gt";
			} else if (node.op.equals("<")) {
				op = "lt";
			} else if (node.op.equals(">=")) {
				op = "ge";
			} else if (node.op.equals("<=")) {
				op = "le";
			} else if (node.op.equals("==")) {
				op = "eq";
			} else {
				op = "ne";
			}
			System.out.printf("\n%-6s%-6s", "", op);
		} else {
			if (node.op.equals("+")) {
				op = "add";
			} else if (node.op.equals("-")) {
				op = "sub";
			} else if (node.op.equals("*")) {
				op = "mul";
			} else if (node.op.equals("/")) {
				op = "div";
			} else {
				op = "mod";
			}
			System.out.printf("\n%-6s%-6s", "", "ldc");
			visit(node.rhs);
			System.out.printf("\n%-6s%-6s", "", op);
		}
	}

	@Override
	public void visitFuncallNode(FuncallNode node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s", node.t_node);
		visit(node.args);
	}

	@Override
	public void visitParenExpression(ParenExpression node) {
		// TODO Auto-generated method stub
		visit(node.expr);
	}

	@Override
	public void visitTerminalExpression(TerminalExpression node) {
		// TODO Auto-generated method stub
		if (localVariable.get(node.t_node.toString()) != null) {
			System.out.printf("%d", localVariable.get(node.t_node.toString()));
		} else {
			System.out.printf("%s", node.t_node);
		}
	}

	@Override
	public void visitUnaryOpNode(UnaryOpNode node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s", node.op);
		visit(node.expr);
	}

	@Override
	public void visitParameters(Parameters node) {
		// TODO Auto-generated method stub
		if (node.str.length() != 0) {
			System.out.printf("%-6s", node.str);
		} else if (node.type_spec != null) {
			visit(node.type_spec);
		} else if (node.params != null) {
			int count = node.params.size();
			for (int i = 0; i < count; i++) {
				visit(node.params.get(i));
			}
		} else {

		}
	}

	@Override
	public void visitParameter(Parameter node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s", node.t_node);
	}

	@Override
	public void visitArrayParameter(ArrayParameter node) {
		// TODO Auto-generated method stub
		visit(node.type);
		System.out.printf("%-6s", node.t_node);
	}

	@Override
	public void visitStatement(Statement node) {
		// TODO Auto-generated method stub
		if (node instanceof Expression_Statement) {
			visit((Expression_Statement) node);
		} else if (node instanceof Compound_Statement) {
			visit((Compound_Statement) node);
		} else if (node instanceof If_Statement) {
			visit((If_Statement) node);
		} else if (node instanceof While_Statement) {
			visit((While_Statement) node);
		} else {
			visit((Return_Statement) node);
		}
	}

	@Override
	public void visitCompound_Statement(Compound_Statement node) {
		// TODO Auto-generated method stub
		for (int i = 0; i < node.local_decls.size(); i++) {
			visit(node.local_decls.get(i));
		}
		for (int j = 0; j < node.stmts.size(); j++) {
			visit(node.stmts.get(j));
		}
	}

	@Override
	public void visitExpression_Statement(Expression_Statement node) {
		// TODO Auto-generated method stub
		visit(node.expr);
	}

	@Override
	public void visitIf_Statement(If_Statement node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s", node.ifnode);
		visit(node.expr);
		visit(node.if_stmt);
		if (node.else_stmt != null) {
			System.out.printf("%-6s", node.elsenode);
			visit(node.else_stmt);
		}
	}

	@Override
	public void visitReturn_Statement(Return_Statement node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s", node.return_node);
		if (node.expr != null) {
			visit(node.expr);
		}
	}

	@Override
	public void visitWhile_Statement(While_Statement node) {
		// TODO Auto-generated method stub
		localSTMT.push("$$" + localSTMT.size());
		System.out.printf("\n%-6s%-6s", localSTMT.peek(), "nop");
		visit(node.expr);
		localSTMT.push("$$" + localSTMT.size());
		System.out.printf("\n%-6s%s %s", "", "fjp", localSTMT.peek());
		visit(node.stmt);
	}

	@Override
	public void visitTypeSpecification(TypeSpecification node) {
		// TODO Auto-generated method stub
		System.out.printf("%-6s", "type");
	}

	@Override
	public void visit(MiniCNode node) {
		// TODO Auto-generated method stub
		node.accept(this);
	}

}

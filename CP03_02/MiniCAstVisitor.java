import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

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

public class MiniCAstVisitor extends MiniCBaseVisitor<MiniCNode> {

	@Override
	public MiniCNode visitProgram(MiniCParser.ProgramContext ctx) {
		List<Declaration> decls = new ArrayList<Declaration>();
		Program program;
		int i = ctx.getChildCount();
		while (i > 0) {
			decls.add((Declaration) visit(ctx.decl(ctx.getChildCount() - i)));
			i--;
		}
		program = new Program(decls);
		System.out.println(program.toString());
		return program;
	}

	@Override
	public MiniCNode visitDecl(MiniCParser.DeclContext ctx) {
		if (ctx.getChild(0) == ctx.var_decl()) {
			return visit(ctx.var_decl());
		}
		return visit(ctx.fun_decl());

	}

	@Override
	public MiniCNode visitVar_decl(MiniCParser.Var_declContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 3:
			Variable_Declaration var_decl = new Variable_Declaration((TypeSpecification) visit(ctx.type_spec()),
					(TerminalNode) ctx.getChild(1));
			return var_decl;
		case 5:
			Variable_Declaration_Assign var_decl_assign = new Variable_Declaration_Assign(
					(TypeSpecification) visit(ctx.type_spec()), (TerminalNode) ctx.getChild(1),
					(TerminalNode) ctx.getChild(3));
			return var_decl_assign;
		default:
			Variable_Declaration_Array var_decl_array = new Variable_Declaration_Array(
					(TypeSpecification) visit(ctx.type_spec()), (TerminalNode) ctx.getChild(1),
					(TerminalNode) ctx.getChild(3));
			return var_decl_array;
		}
	}

	@Override
	public MiniCNode visitType_spec(MiniCParser.Type_specContext ctx) {
		Type type = Type.valueOf(ctx.getChild(0).getText().toUpperCase());
		TypeSpecification type_spec = new TypeSpecification(type);
		return type_spec;
	}

	@Override
	public MiniCNode visitFun_decl(MiniCParser.Fun_declContext ctx) {
		Function_Declaration fun_decl = new Function_Declaration((TypeSpecification) visit(ctx.type_spec()),
				(TerminalNode) ctx.getChild(1), (Parameters) visit(ctx.params()),
				(Compound_Statement) visit(ctx.compound_stmt()));
		return fun_decl;
	}

	@Override
	public MiniCNode visitParams(MiniCParser.ParamsContext ctx) {
		Parameters param;
		int size = ctx.param().size();
		int count = ctx.getChildCount();
		switch (size) {
		case 0:
			if (count == 0) {
				param = new Parameters();
				return param;
			} else {
				Type type = Type.valueOf(ctx.getChild(0).getText().toUpperCase());
				TypeSpecification type_spec = new TypeSpecification(type);
				param = new Parameters((TypeSpecification) type_spec);
				return param;
			}
		default:
			List<Parameter> params = new ArrayList<Parameter>();
			for (int i = 0; i < size; i++) {
				params.add((Parameter) visit(ctx.param(i)));
			}
			param = new Parameters(params);
			return param;
		}
	}

	@Override
	public MiniCNode visitParam(MiniCParser.ParamContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 2:
			Parameter param = new Parameter((TypeSpecification) visit(ctx.type_spec()), (TerminalNode) ctx.getChild(1));
			return param;
		default:
			ArrayParameter arr_param = new ArrayParameter((TypeSpecification) visit(ctx.type_spec()),
					(TerminalNode) ctx.getChild(1));
			return arr_param;
		}
	}

	@Override
	public MiniCNode visitStmt(MiniCParser.StmtContext ctx) {
		if (ctx.getChild(0) == ctx.expr_stmt()) {
			return visit(ctx.expr_stmt());
		} else if (ctx.getChild(0) == ctx.compound_stmt()) {
			return visit(ctx.compound_stmt());
		} else if (ctx.getChild(0) == ctx.if_stmt()) {
			return visit(ctx.if_stmt());
		} else if (ctx.getChild(0) == ctx.while_stmt()) {
			return visit(ctx.while_stmt());
		} else {
			return visit(ctx.return_stmt());
		}
	}

	@Override
	public MiniCNode visitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		Expression_Statement expr_stmt = new Expression_Statement((Expression) visit(ctx.expr()));
		return expr_stmt;
	}

	@Override
	public MiniCNode visitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		While_Statement while_stmt = new While_Statement((TerminalNode) ctx.getChild(0), (Expression) visit(ctx.expr()),
				(Statement) visit(ctx.stmt()));
		return while_stmt;
	}

	@Override
	public MiniCNode visitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		Compound_Statement compound_stmt;
		List<Local_Declaration> local_decls = new ArrayList<Local_Declaration>();
		List<Statement> stmts = new ArrayList<Statement>();
		for (int i = 0; i < ctx.local_decl().size(); i++) {
			local_decls.add((Local_Declaration) visit(ctx.local_decl(i)));
		}
		for (int j = 0; j < ctx.stmt().size(); j++) {
			stmts.add((Statement) visit(ctx.stmt(j)));
		}
		compound_stmt = new Compound_Statement(local_decls, stmts);
		return compound_stmt;
	}

	@Override
	public MiniCNode visitLocal_decl(MiniCParser.Local_declContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 3:
			Local_Declaration local_decl = new Local_Declaration((TypeSpecification) visit(ctx.type_spec()),
					(TerminalNode) ctx.getChild(1));
			return local_decl;
		case 5:
			Local_Variable_Declaration_Assign local_decl_assign = new Local_Variable_Declaration_Assign(
					(TypeSpecification) visit(ctx.type_spec()), (TerminalNode) ctx.getChild(1),
					(TerminalNode) ctx.getChild(3));
			return local_decl_assign;
		default:
			Local_Variable_Declaration_Array local_decl_array = new Local_Variable_Declaration_Array(
					(TypeSpecification) visit(ctx.type_spec()), (TerminalNode) ctx.getChild(1),
					(TerminalNode) ctx.getChild(3));
			return local_decl_array;
		}
	}

	@Override
	public MiniCNode visitIf_stmt(MiniCParser.If_stmtContext ctx) {
		int count = ctx.getChildCount();
		If_Statement if_stmt;
		if (count == 5) {
			if_stmt = new If_Statement((TerminalNode) ctx.getChild(0), (Expression) visit(ctx.expr()),
					(Statement) visit(ctx.stmt(0)));
			return if_stmt;
		} else {
			if_stmt = new If_Statement((TerminalNode) ctx.getChild(0), (Expression) visit(ctx.expr()),
					(Statement) visit(ctx.stmt(0)), (TerminalNode) ctx.getChild(5), (Statement) visit(ctx.stmt(1)));
			return if_stmt;
		}
	}

	@Override
	public MiniCNode visitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		int count = ctx.getChildCount();
		Return_Statement return_stmt;
		if (count == 2) {
			return_stmt = new Return_Statement((TerminalNode) ctx.getChild(0));
			return return_stmt;
		} else {
			return_stmt = new Return_Statement((TerminalNode) ctx.getChild(0), (Expression) visit(ctx.expr()));
			return return_stmt;
		}
	}

	@Override
	public MiniCNode visitExpr(MiniCParser.ExprContext ctx) {
		int count = ctx.getChildCount();
		if (count == 1) {
			TerminalExpression terminal_expr = new TerminalExpression((TerminalNode) ctx.getChild(0));
			return terminal_expr;
		} else if (count == 2) {
			UnaryOpNode unary_expr = new UnaryOpNode((String) ctx.getChild(0).getText(),
					(Expression) visit(ctx.expr(0)));
			return unary_expr;
		} else if (count == 3) {
			if (isAssign(ctx)) {
				AssignNode assign_expr = new AssignNode((TerminalNode) ctx.getChild(0),
						(Expression) visit(ctx.expr(0)));
				return assign_expr;
			} else if (isParen(ctx)) {
				ParenExpression paren_expr = new ParenExpression((Expression) visit(ctx.expr(0)));
				return paren_expr;
			} else {
				BinaryOpNode binary_expr = new BinaryOpNode((Expression) visit(ctx.expr(0)),
						(String) ctx.getChild(1).getText(), (Expression) visit(ctx.expr(1)));
				return binary_expr;
			}
		} else if (count == 4) {
			if (isFunc(ctx)) {
				FuncallNode func_expr = new FuncallNode((TerminalNode) ctx.getChild(0), (Arguments) visit(ctx.args()));
				return func_expr;
			} else {
				ArefNode array_expr = new ArefNode((TerminalNode) ctx.getChild(0), (Expression) visit(ctx.expr(0)));
				return array_expr;
			}
		} else {
			ArefAssignNode array_assign_expr = new ArefAssignNode((TerminalNode) ctx.getChild(0),
					(Expression) visit(ctx.expr(0)), (Expression) visit(ctx.expr(1)));
			return array_assign_expr;
		}
	}

	@Override
	public MiniCNode visitArgs(MiniCParser.ArgsContext ctx) {
		int count;
		Arguments args;
		try {
			count = ctx.expr().size();
		} catch (NullPointerException e) {
			args = new Arguments();
			return args;
		}
		List<Expression> exprs = new ArrayList<Expression>();
		for (int i = 0; i < count; i++) {
			exprs.add((Expression) visit(ctx.expr(i)));
		}
		args = new Arguments(exprs);
		return args;
	}

	// 3
	boolean isAssign(MiniCParser.ExprContext ctx) {
		return ctx.getChild(1).getText().equals("=");
	}

	// 3
	boolean isParen(MiniCParser.ExprContext ctx) {
		return ctx.getChild(2).getText().equals(")");
	}

	// 3
	boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
		return ctx.getChild(1) != ctx.expr();
	}

	// 4
	boolean isArray(MiniCParser.ExprContext ctx) {
		return ctx.getChild(1).getText().equals("[");
	}

	// 4
	boolean isFunc(MiniCParser.ExprContext ctx) {
		return ctx.getChild(1).getText().equals("(");
	}
}

public class MiniCPrintVisitor extends MiniCBaseVisitor<String> {
	int depth = 0;

	@Override
	public String visitProgram(MiniCParser.ProgramContext ctx) {
		int i = ctx.getChildCount();
		while (i > 0) {
			System.out.println(visit(ctx.decl(ctx.getChildCount() - i)));
			i--;
		}
		return visitChildren(ctx);
	}

	@Override
	public String visitDecl(MiniCParser.DeclContext ctx) {
		if (ctx.getChild(0) == ctx.var_decl()) {
			return visit(ctx.var_decl());
		}
		return visit(ctx.fun_decl());

	}

	@Override
	public String visitVar_decl(MiniCParser.Var_declContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 3:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText();
		case 5:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + " " + ctx.getChild(2).getText() + " "
					+ ctx.getChild(3).getText() + ctx.getChild(4).getText();
		default:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText()
					+ ctx.getChild(3).getText() + ctx.getChild(4).getText() + ctx.getChild(5).getText();
		}
	}

	@Override
	public String visitType_spec(MiniCParser.Type_specContext ctx) {
		return ctx.getChild(0).getText();
	}

	@Override
	public String visitFun_decl(MiniCParser.Fun_declContext ctx) {
		String text = "";
		try {
			text = visit(ctx.params());
		} catch (NullPointerException e) {
			text = "";
		} finally {
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText() + text
					+ ctx.getChild(4).getText() + visit(ctx.compound_stmt());
		}
	}

	@Override
	public String visitParams(MiniCParser.ParamsContext ctx) {
		if (ctx == null) {
			return "";
		}
		int count = ctx.getChildCount();
		String text = "";
		switch (count) {
		case 1:
			return visit(ctx.param(0));
		default:
			for (int i = 1; i < (count / 2); i++) {
				text += ", " + visit(ctx.param(i));
			}
			return visit(ctx.param(0)) + text;
		}
	}

	@Override
	public String visitParam(MiniCParser.ParamContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 2:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText();
		default:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText()
					+ ctx.getChild(3).getText();
		}
	}

	@Override
	public String visitStmt(MiniCParser.StmtContext ctx) {
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
	public String visitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		return visit(ctx.expr()) + ctx.getChild(1);
	}

	@Override
	public String visitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		return ctx.getChild(0).getText() + " " + ctx.getChild(1).getText() + visit(ctx.expr())
				+ ctx.getChild(3).getText() + visit(ctx.stmt());
	}

	@Override
	public String visitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String text;
		text = "\n" + autoIndent(depth++) + "{\n";
		for (int i = 0; i < ctx.local_decl().size(); i++) {
			text += autoIndent(depth) + visit(ctx.local_decl(i)) + "\n";
		}
		for (int j = 0; j < ctx.stmt().size(); j++) {
			text += autoIndent(depth) + visit(ctx.stmt(j)) + "\n";
		}
		text += autoIndent(--depth) + "}";
		return text;
	}

	@Override
	public String visitLocal_decl(MiniCParser.Local_declContext ctx) {
		int count = ctx.getChildCount();
		switch (count) {
		case 3:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText();
		case 5:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + " " + ctx.getChild(2).getText() + " "
					+ ctx.getChild(3).getText() + ctx.getChild(4).getText();
		default:
			return visit(ctx.type_spec()) + " " + ctx.getChild(1).getText() + ctx.getChild(2).getText()
					+ ctx.getChild(3).getText() + ctx.getChild(4).getText() + ctx.getChild(5).getText();
		}
	}

	@Override
	public String visitIf_stmt(MiniCParser.If_stmtContext ctx) {
		int count = ctx.getChildCount();
		if (count == 5) {
			return ctx.getChild(0).getText() + " " + ctx.getChild(1).getText() + visit(ctx.expr())
					+ ctx.getChild(3).getText() + "\n" + autoIndent(depth++) + "{\n" + autoIndent(depth)
					+ visit(ctx.stmt(0)) + "\n" + autoIndent(--depth) + "}";
		} else {
			return ctx.getChild(0).getText() + " " + ctx.getChild(1).getText() + visit(ctx.expr())
					+ ctx.getChild(3).getText() + visit(ctx.stmt(0)) + ctx.getChild(5).getText() + visit(ctx.stmt(1));
		}
	}

	@Override
	public String visitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		int count = ctx.getChildCount();
		if (count == 2) {
			return ctx.getChild(0).getText() + ctx.getChild(1).getText();
		} else {
			return ctx.getChild(0).getText() + visit(ctx.expr()) + ctx.getChild(2).getText();
		}
	}

	@Override
	public String visitExpr(MiniCParser.ExprContext ctx) {
		int count = ctx.getChildCount();
		if (count == 1) {
			return ctx.getChild(0).getText();
		} else if (count == 2) {
			return ctx.getChild(0).getText() + visit(ctx.expr(0));
		} else if (count == 3) {
			if (isAssign(ctx)) {
				return ctx.getChild(0).getText() + " = " + visit(ctx.expr(0));
			} else if (isGualho(ctx)) {
				return "(" + visit(ctx.expr(0)) + ")";
			} else {
				return visit(ctx.expr(0)) + " " + ctx.getChild(1).getText() + " " + visit(ctx.expr(1));
			}
		} else if (count == 4) {
			if (isFunc(ctx)) {
				return ctx.getChild(0).getText() + "(" + visit(ctx.args()) + ")";
			} else {
				return ctx.getChild(0).getText() + "[" + visit(ctx.expr(0)) + "]";
			}
		} else {
			return ctx.getChild(0).getText() + "[" + visit(ctx.expr(0)) + "]" + " = " + visit(ctx.expr(1));
		}
	}

	@Override
	public String visitArgs(MiniCParser.ArgsContext ctx) {
		int count;
		try {
			count = ctx.expr().size();
		} catch (NullPointerException e) {
			return "";
		}
		String text = "";
		text = visit(ctx.expr(0));
		for (int i = 1; i < count; i++) {
			text += ", " + visit(ctx.expr(i));
		}
		return text;
	}

	// 3
	boolean isAssign(MiniCParser.ExprContext ctx) {
		return ctx.getChild(1).getText().equals("=");
	}

	// 3
	boolean isGualho(MiniCParser.ExprContext ctx) {
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

	String autoIndent(int depth) {
		String result = "";
		for (int i = 0; i < depth; i++) {
			for (int j = 0; j < 4; j++) {
				result += ".";
			}
		}
		return result;
	}
}
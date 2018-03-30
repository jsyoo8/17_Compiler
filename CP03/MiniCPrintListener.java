import org.antlr.v4.runtime.tree.ParseTreeProperty;
import java.util.ArrayList;

public class MiniCPrintListener extends MiniCBaseListener{
	private ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	private ParseTreeProperty<ArrayList<String>> arrayTexts = new ParseTreeProperty<ArrayList<String>>();
	
	/*******************************************************************
	 * Override Methods
	 *******************************************************************/
	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx){
		String s1 = null, s2 = "";
		int index = 0;
		
		s1 = newTexts.get(ctx.decl(index++)) + "\n";
		s2 += s1;
		
		while(hasMoreDecl(ctx, index)){
			s1 = newTexts.get(ctx.decl(index++)) + "\n";
			s2 += s1;
		}
		
		newTexts.put(ctx, s2);
		System.out.println(s2);
		System.out.println("Exit: program");
	}
	
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx){
		String s1 = null;
		
		if(isVarDecl(ctx)){
			s1 = newTexts.get(ctx.var_decl());
		}else if(isFunDecl(ctx)){
			s1 = newTexts.get(ctx.fun_decl());
		}
		
		newTexts.put(ctx, s1);
	}
	
	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx){
		String s1 = null; String s2 = null; String s3 = null;
		
		if(isDeclWithInit(ctx)){
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			s3 = ctx.getChild(3).getText();
			
			newTexts.put(ctx, s1 + " " + s2 + " = " + s3 + ";");
		}else if(isArrayDecl(ctx)){
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			s3 = ctx.getChild(3).getText();

			newTexts.put(ctx, s1 + " " + s2 + "[" + s3 + "]" + ";");
		}else{
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			
			newTexts.put(ctx, s1 + " " + s2 + ";");
		}
		
	}
	
	@Override
	public void exitType_spec(MiniCParser.Type_specContext ctx){
		String s1 = null;
		s1 = ctx.getChild(0).getText();
		
		newTexts.put(ctx, s1);
	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx){
		String s1 = null, s2 = null, s3 = null;
		String n1 = null;
		
		n1 = ctx.getChild(1).getText();
		s1 = newTexts.get(ctx.type_spec());
		s2 = newTexts.get(ctx.params());
		s3 = newTexts.get(ctx.compound_stmt());
		
		newTexts.put(ctx, s1 + " " + n1 + "(" + s2 + ")\n" + s3);
	}
	
	@Override
	public void exitParams(MiniCParser.ParamsContext ctx){
		if(hasParams(ctx)){
			String s1 = null;

			if(isVoid(ctx)){
				s1 = ctx.getChild(0).getText();
				newTexts.put(ctx, s1);
			}
			else{
				int i = 0;
				s1 = newTexts.get(ctx.param(i++));
				
				while(hasMoreParam(ctx, i++)){
					s1 += ',' + " " + newTexts.get(ctx.param(i++));
				}
				newTexts.put(ctx, s1);
			}
		}else{
			newTexts.put(ctx, "");
		}
		
	}
	
	@Override
	public void exitParam(MiniCParser.ParamContext ctx){
		String s1 = null, s2 = null;
		
		s1 = newTexts.get(ctx.type_spec());
		s2 = ctx.getChild(1).getText();
		
		if(isArray(ctx)){
			newTexts.put(ctx, s1 + " " + s2 + "[]");
		}else{
			newTexts.put(ctx, s1 + " " + s2);
		}
		
	}
	
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx){
		String s1 = null;
		ArrayList<String> a1 = null;
		
		if(isExprStmt(ctx)){
			s1 = newTexts.get(ctx.expr_stmt());
		}else if(isCompoundStmt(ctx)){
			a1 = arrayTexts.get(ctx.compound_stmt());
			s1 = "";
			
			for(int i = 0; i < a1.size(); i++){
				s1 += a1.get(i) + '\n';
			}
		}else if(isIfStmt(ctx)){
			a1 = arrayTexts.get(ctx.if_stmt());
			s1 = "";
			
			for(int i = 0; i < a1.size(); i++){
				s1 += a1.get(i) + '\n';
			}
		}else if(isWhileStmt(ctx)){
			a1 = arrayTexts.get(ctx.while_stmt());
			s1 = "";
			
			for(int i = 0; i < a1.size(); i++){
				s1 += a1.get(i) + '\n';
			}
		}else if(isReturnStmt(ctx)){
			s1 = newTexts.get(ctx.return_stmt());
		}else{
			
		}
		arrayTexts.put(ctx, a1);
		newTexts.put(ctx, s1);
	}

	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx){
		String s1 = null;
		
		s1 = newTexts.get(ctx.expr());
		newTexts.put(ctx, s1 + ';');
	}
	
	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx){
		String s1 = null; String s2 = null; String s3 = null;
		
		if(isDeclWithInit(ctx)){
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			s3 = ctx.getChild(3).getText();
			
			newTexts.put(ctx, s1 + " " + s2 + " = " + s3 + ";");
		}else if(isArrayDecl(ctx)){
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			s3 = ctx.getChild(3).getText();

			newTexts.put(ctx, s1 + " " + s2 + "[" + s3 + "]" + ";");
		}else{
			s1 = newTexts.get(ctx.type_spec());
			s2 = ctx.getChild(1).getText();
			
			newTexts.put(ctx, s1 + " " + s2 + ";");
		}
		
	}

	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx){
		String s1 = null, s2 = null;
		if(hasExpr(ctx)){
			s1 = ctx.getChild(0).getText();
			s2 = newTexts.get(ctx.expr());
			
			newTexts.put(ctx, s1 + " " + s2 + ";");
		}else{
			s1 = ctx.getChild(0).getText();
			
			newTexts.put(ctx, s1 + ";");
		}
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx){
		String s1 = null, s2 = null, op = null;
		
		if(isBinaryOperation(ctx)){
			s1 = newTexts.get(ctx.expr(0));
			s2 = newTexts.get(ctx.expr(1));
			op = ctx.getChild(1).getText();
			
			newTexts.put(ctx, s1 + " " + op + " " + s2);
		}else if(isAssignment(ctx)){
			s1 = ctx.getChild(0).getText();
			s2 = newTexts.get(ctx.expr(0));
			op = ctx.getChild(1).getText();
			
			newTexts.put(ctx, s1 + " " + op + " " + s2);
		}else if(isUnaryOperation(ctx)){
			op = ctx.getChild(0).getText();
			s1 = newTexts.get(ctx.expr(0));
			
			newTexts.put(ctx, op + s1);
		}else if(isArray(ctx)){
			s1 = ctx.getChild(0).getText();
			s2 = newTexts.get(ctx.expr(0));
			
			newTexts.put(ctx, s1+'['+s2+']');
		}else if(isCall(ctx)){
			s1 = ctx.getChild(0).getText();
			s2 = newTexts.get(ctx.args());
			
			newTexts.put(ctx, s1+'('+s2+')');
		}else if(isArrayAssignment(ctx)){
			s1 = newTexts.get(ctx.expr(0));
			s2 = newTexts.get(ctx.expr(1));
			op = ctx.getChild(4).getText();
			
			newTexts.put(ctx, ctx.getChild(0).getText() + '[' + s1 + ']' + " " + op + " " + s2);
		}else if(isParentFormula(ctx)){
			s1 = newTexts.get(ctx.expr(0));
			
			newTexts.put(ctx, "(" + s1 + ")");
		}else{
			newTexts.put(ctx, ctx.getChild(0).getText());
		}
	}
	
	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx){
		String s1 = null, s2 = null, s3 = null;
		ArrayList<String> a1 = null;
		s1 = ctx.getChild(0).getText();
		s2 = newTexts.get(ctx.expr());
		
		if( isCompoundStmt( ctx.stmt() ) ){
			a1 = arrayTexts.get(ctx.stmt());
			s3 = "";
			a1.add(0, s1 + "(" + s2 + ")");
			for(int i = 0; i < a1.size(); i++){
				s3 += "...." + a1.get(i) + '\n';
			}
		}else if(isIfStmt( ctx.stmt() ) || isWhileStmt( ctx.stmt() ) ){
			a1 = arrayTexts.get(ctx.stmt());
			s3 = "";
			a1.add(0, s1 + "(" + s2 + ")");
			a1.add(1, "{");
			a1.add(a1.size(), "}");
			for(int i = 2; i < a1.size() - 1; i++){
				a1.add(i, "...." + a1.remove(i));
			}
			for(int i = 0; i < a1.size(); i++){
				s3 += "...." + a1.get(i) + '\n';
			}
		}else{
			a1 = new ArrayList<String>();
			s3 = "";
			a1.add(s1 + "(" + s2 + ")");
			a1.add("{");
			a1.add("...." + newTexts.get(ctx.stmt()));
			a1.add("}");
		}
		arrayTexts.put(ctx, a1);
		newTexts.put(ctx, s1 + "(" + s2 + ")\n" + s3);
	}
	
	@Override
	public void enterIf_stmt(MiniCParser.If_stmtContext ctx){
		
	}
	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx){
		String s1 = null, s2 = null, s3 = null;
		s1 = newTexts.get(ctx.expr());
		s2 = newTexts.get(ctx.stmt(0));
		ArrayList<String> a1 = null;
		
		if(hasElse(ctx)){
			s3 = newTexts.get(ctx.stmt(1));
			/* if */
			if( isExprStmt( ctx.stmt(0) ) || isReturnStmt(ctx.stmt(0)) ){
				a1 = new ArrayList<String>();
				a1.add(ctx.getChild(0).getText() + "(" + s1 + ")");
				a1.add("{");
				a1.add("...." + s2);
				a1.add("}");
			}else if(isWhileStmt(ctx.stmt(0)) || isIfStmt(ctx.stmt(0)) ){
				ArrayList<String> temp = arrayTexts.get(ctx.stmt(0));
				a1 = new ArrayList<String>();
				a1.add(ctx.getChild(0).getText() + "(" + s1 + ")");
				a1.add("{");
				
				for(int i = 0; i < temp.size(); i++){
					a1.add("...." + temp.get(i));
				}
				
				a1.add("}");
			}
			else{
				a1 = arrayTexts.get(ctx.stmt(0));
				a1.add(0, ctx.getChild(0).getText() + "(" + s1 + ")");
			}
			/* 여기부터 Else */
			if( isExprStmt( ctx.stmt(1) ) || isReturnStmt(ctx.stmt(1)) ){
				a1.add(ctx.getChild(5).getText());
				a1.add("{");
				a1.add("...." + s3);
				a1.add("}");
			}else if(isWhileStmt(ctx.stmt(1)) || isIfStmt(ctx.stmt(1)) ){
				ArrayList<String> temp = arrayTexts.get(ctx.stmt(1));
				a1.add(ctx.getChild(5).getText());
				a1.add("{");
				
				for(int i = 0; i < temp.size(); i++){
					a1.add("...." + temp.get(i));
				}
				
				a1.add("}");
			}
			else{
				ArrayList<String> temp = arrayTexts.get(ctx.stmt(1));
				a1.add(ctx.getChild(5).getText());
				for(int i = 0; i < temp.size(); i++){
					a1.add(temp.get(i));
				}
			}

			arrayTexts.put(ctx, a1);
			newTexts.put(ctx, ctx.getChild(0).getText() + "(" + s1 + ") " + s2 + " " + ctx.getChild(5).getText() + " " + s3);
		}else{
			if( isExprStmt( ctx.stmt(0) ) || isReturnStmt(ctx.stmt(0)) ){
				a1 = new ArrayList<String>();
				a1.add(ctx.getChild(0).getText() + "(" + s1 + ")");
				a1.add("{");
				a1.add("...." + s2);
				a1.add("}");
			}else if(isWhileStmt(ctx.stmt(0)) || isIfStmt(ctx.stmt(0)) ){
				ArrayList<String> temp = arrayTexts.get(ctx.stmt(0));
				a1 = new ArrayList<String>();
				a1.add(ctx.getChild(0).getText() + "(" + s1 + ")");
				a1.add("{");
				
				for(int i = 0; i < temp.size(); i++){
					a1.add("...." + temp.get(i));
				}
				
				a1.add("}");
			}
			else{
				a1 = arrayTexts.get(ctx.stmt(0));
				a1.add(0, ctx.getChild(0).getText() + "(" + s1 + ")");
			}
			
			arrayTexts.put(ctx, a1);
			newTexts.put(ctx, ctx.getChild(0).getText() + "(" + s1 + ")\n" + s2);
		}
	}

	@Override 
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx){
		int index = 1;
		String s1 = null, s2 = "";
		ArrayList<String> a1 = new ArrayList<String>(), a2 = null;
		
		a1.add(ctx.getChild(0).getText());
		
		while(hasMoreLocalDecl(ctx, index)){
			s1 = "...." + newTexts.get(ctx.getChild(index++));
			a1.add(s1);
		}
		while(hasMoreStmt(ctx, index)){
			if(isCompoundStmt((MiniCParser.StmtContext)ctx.getChild(index))){
				a2 = arrayTexts.get(ctx.getChild(index++));
				
				for(int i = 0; i < a2.size(); i++){
					s1 = "...." + a2.get(i);
					a1.add(s1);
				}
			}else if(isWhileStmt((MiniCParser.StmtContext)ctx.getChild(index))){
				a2 = arrayTexts.get(ctx.getChild(index++));
				
				for(int i = 0; i < a2.size(); i++){
					s1 = "...." + a2.get(i);
					a1.add(s1);
				}
			}else if(isIfStmt((MiniCParser.StmtContext)ctx.getChild(index))){
				a2 = arrayTexts.get(ctx.getChild(index++));
				
				for(int i = 0; i < a2.size(); i++){
					s1 = "...." + a2.get(i);
					a1.add(s1);
				}
			}
			else{
				s1 = "...." + newTexts.get(ctx.getChild(index++));
				a1.add(s1);
			}
		}
		
		a1.add(ctx.getChild(ctx.getChildCount() - 1).getText());
		
		for(int i = 0 ; i < a1.size(); i++){
			s2 += a1.get(i) + '\n';
		}
		
		newTexts.put(ctx, s2);
		arrayTexts.put(ctx, a1);
	}
	
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx){
		String s1 = null;
		int i = 0;
		s1 = newTexts.get(ctx.expr(i++));
		
		while(hasMoreExpr(ctx, i++)){
			s1 += ',' + " " + newTexts.get(ctx.expr(i++));
		}
		newTexts.put(ctx, s1);
	}
	
	/**********************************************************************************
	 * ExprContext Assist Methods
	 **********************************************************************************/
	public boolean isBinaryOperation(MiniCParser.ExprContext ctx){
		return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr() && ctx.getChild(0) == ctx.expr(0);	//ChildCount == 3인 구성 중, (expr) 배제
	}
	
	public boolean isUnaryOperation(MiniCParser.ExprContext ctx){
		return ctx.getChildCount() == 2 && ctx.getChild(1) == ctx.expr(0);
	}
	
	public boolean isArray(MiniCParser.ExprContext ctx){
		//IDENT '[' expr ']'
		return ctx.getChildCount() == 4 && ctx.getChild(1).getText().equals("[");
	}
	
	public boolean isCall(MiniCParser.ExprContext ctx){
		//IDENT '[' expr ']'
		return ctx.getChildCount() == 4 && ctx.getChild(1).getText().equals("(");
	}
	
	public boolean isAssignment(MiniCParser.ExprContext ctx){
		return ctx.getChildCount() == 3 && ctx.getChild(0) != ctx.expr(0) && ctx.getChild(1).getText().equals("=");
	}
	
	public boolean isArrayAssignment(MiniCParser.ExprContext ctx){
		//IDENT '[' expr ']' '=' expr
		return ctx.getChildCount() == 6;
	}
	
	public boolean isParentFormula(MiniCParser.ExprContext ctx){
		return ctx.getChildCount() == 3 && ctx.getChild(1) == ctx.expr(0) && ctx.getChild(0).getText().equals("(");
	}
	/**********************************************************************************
	 * ArgsContext Assist Methods
	 **********************************************************************************/
	public boolean hasMoreExpr(MiniCParser.ArgsContext ctx, int index){
		return ctx.getChildCount() > index && ctx.getChild(index).getText().equals(",");
	}
	
	/**********************************************************************************
	 * Local_declContext Assist Methods
	 **********************************************************************************/
	public boolean isDeclWithInit(MiniCParser.Local_declContext ctx){
		return ctx.getChildCount() == 5 && ctx.getChild(2).getText().equals("=");
	}
	
	public boolean isArrayDecl(MiniCParser.Local_declContext ctx){
		return ctx.getChildCount() == 6 && ctx.getChild(2).getText().equals("[");
	}
	
	/**********************************************************************************
	 * Compound_stmtContext Assist Methods
	 **********************************************************************************/
	public boolean hasMoreLocalDecl(MiniCParser.Compound_stmtContext ctx, int index){
		return ctx.getChildCount() > index && ctx.getChild(index).getClass().getName().equals(MiniCParser.Local_declContext.class.getName());
	}
	
	public boolean hasMoreStmt(MiniCParser.Compound_stmtContext ctx, int index){
		return ctx.getChildCount() > index && ctx.getChild(index).getClass().getName().equals(MiniCParser.StmtContext.class.getName());
	}
	
	/**********************************************************************************
	 * StmtContext Assist Methods
	 **********************************************************************************/
	public boolean isExprStmt(MiniCParser.StmtContext ctx){
		return ctx.getChild(0) == ctx.expr_stmt();
	}
	
	public boolean isCompoundStmt(MiniCParser.StmtContext ctx){
		return ctx.getChild(0) == ctx.compound_stmt();
	}
	
	public boolean isIfStmt(MiniCParser.StmtContext ctx){
		return ctx.getChild(0) == ctx.if_stmt();
	}
	
	public boolean isWhileStmt(MiniCParser.StmtContext ctx){
		return ctx.getChild(0) == ctx.while_stmt();
	}
	
	public boolean isReturnStmt(MiniCParser.StmtContext ctx){
		return ctx.getChild(0) == ctx.return_stmt();
	}
	
	/**********************************************************************************
	 * ReturnContext Assist Methods
	 **********************************************************************************/
	public boolean hasExpr(MiniCParser.Return_stmtContext ctx){
		return ctx.getChildCount() == 3 && ctx.getChild(1) == ctx.expr();
	}
	
	/**********************************************************************************
	 * If_stmtContext Assist Methods
	 **********************************************************************************/
	public boolean hasElse(MiniCParser.If_stmtContext ctx){
		return ctx.getChildCount() == 7 && ctx.getChild(4) == ctx.stmt(0) && ctx.getChild(6) == ctx.stmt(1);
	}
	
	/**********************************************************************************
	 * ParamsContext Assist Methods
	 **********************************************************************************/
	public boolean hasParams(MiniCParser.ParamsContext ctx){
		return ctx.getChildCount() > 0;
	}
	
	public boolean isVoid(MiniCParser.ParamsContext ctx){
		return ctx.getChildCount() == 1;
	}
	
	public boolean hasMoreParam(MiniCParser.ParamsContext ctx, int index){
		return ctx.getChildCount() > index && ctx.getChild(index).getText().equals(",");
	}
	
	/**********************************************************************************
	 * ParamsContext Assist Methods
	 **********************************************************************************/
	public boolean isArray(MiniCParser.ParamContext ctx){
		return ctx.getChildCount() == 4 && ctx.getChild(2).getText().equals("[");
	}
	
	/**********************************************************************************
	 * Var_declContext Assist Methods
	 **********************************************************************************/
	public boolean isDeclWithInit(MiniCParser.Var_declContext ctx){
		return ctx.getChildCount() == 5 && ctx.getChild(2).getText().equals("=");
	}
	
	public boolean isArrayDecl(MiniCParser.Var_declContext ctx){
		return ctx.getChildCount() == 6 && ctx.getChild(2).getText().equals("[");
	}

	/**********************************************************************************
	 * DeclContext Assist Methods
	 **********************************************************************************/
	public boolean isVarDecl(MiniCParser.DeclContext ctx){
		return ctx.getChildCount() > 0 && ctx.getChild(0) == ctx.var_decl();
	}
	
	public boolean isFunDecl(MiniCParser.DeclContext ctx){
		return ctx.getChildCount() > 0 && ctx.getChild(0) == ctx.fun_decl();
	}
	
	/**********************************************************************************
	 * ProgramContext Assist Methods
	 **********************************************************************************/
	public boolean hasMoreDecl(MiniCParser.ProgramContext ctx, int index){
		return ctx.getChildCount() > index && ctx.getChild(index) == ctx.decl(index);
	}
}

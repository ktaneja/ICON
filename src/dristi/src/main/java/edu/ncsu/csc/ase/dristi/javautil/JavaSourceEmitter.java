package edu.ncsu.csc.ase.dristi.javautil;

import java.util.ArrayList;
import java.util.List;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;

public class JavaSourceEmitter {

	public static void main(String[] args) {
		CompilationUnit cu = createCU();
		System.out.println(cu);
	}

	/**
	 * creates the compilation unit
	 */
	private static CompilationUnit createCU() 
	{
		CompilationUnit cu = new CompilationUnit();
		// set the package
		cu.setPackage(new PackageDeclaration(ASTHelper.createNameExpr("java.parser.test")));

		// create the type declaration
		ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, true, "GeneratedClass");
		
		List<ClassOrInterfaceType> list = new ArrayList<ClassOrInterfaceType>();
		ASTHelper.addTypeDeclaration(cu, type);
		
		// create implements and extends
		list.add(new ClassOrInterfaceType("hello"));
		type.setExtends(list);
		type.setImplements(list);

		// create a method
		MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE, "main");
		method.setModifiers(ModifierSet.addModifier(method.getModifiers(), ModifierSet.STATIC));
		ASTHelper.addMember(type, method);

		// add a parameter to the method
		Parameter param = ASTHelper.createParameter(ASTHelper.createReferenceType("String", 1), "args");
		// param.setVarArgs(true);
		ASTHelper.addParameter(method, param);

		// add managed exceptions
		List<NameExpr> exceptionList = new ArrayList<NameExpr>();
		exceptionList.add(ASTHelper.createNameExpr("ABCException"));
		method.setThrows(exceptionList);
		
		// add a body to the method
		BlockStmt block = new BlockStmt();
		// method.setBody(block);

		// add a statement do the method body
		NameExpr clazz = new NameExpr("System");
		FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
		MethodCallExpr call = new MethodCallExpr(field, "println");
		ASTHelper.addArgument(call, new StringLiteralExpr("Hello World!"));
		ASTHelper.addStmt(block, call);

		return cu;

	}

}

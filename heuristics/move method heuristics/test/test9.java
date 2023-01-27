import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 

 

import java.io.FileInputStream; 

import java.util.HashSet; 

import java.util.Set; 

 

public class test9 { 

    public static void main(String[] args) throws Exception { 

        // Parse the file 

        FileInputStream in = new FileInputStream("path/to/your/file.java"); 

        CompilationUnit cu = JavaParser.parse(in); 

 

        // Visit the methods and get a set of invoked methods 

        Set<String> invokedMethods = new HashSet<>(); 

        new MethodVisitor().visit(cu, invokedMethods); 

 

        // Print the set of invoked methods 

        System.out.println(invokedMethods); 

    } 

 

    private static class MethodVisitor extends VoidVisitorAdapter<Set<String>> { 

        @Override 

        public void visit(MethodDeclaration n, Set<String> invokedMethods) { 

            super.visit(n, invokedMethods); 

            n.getBody().ifPresent(b -> b.getStatements().forEach(s -> s.findAll(com.github.javaparser.ast.expr.MethodCallExpr.class) 

                                                            .forEach(m -> invokedMethods.add(m.getNameAsString())))); 

        } 

    } 

}
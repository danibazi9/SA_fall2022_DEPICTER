package com;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration; 
import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 
import com.MethodcallExpr;
import java.io.FileInputStream; 
import java.util.HashSet; 
import java.util.Set; 
 
public class invoked_methods { 
    public static void main(String[] args) throws Exception { 
        // Parse the file 
        FileInputStream in = new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GanttLookAndFeels.java"); 
        CompilationUnit cu = StaticJavaParser.parse(in); 
 
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
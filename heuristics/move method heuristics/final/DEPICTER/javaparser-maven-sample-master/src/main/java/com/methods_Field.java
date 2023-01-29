package com;

import com.github.javaparser.StaticJavaParser; 
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.invoked_methods;

import java.io.FileInputStream; 
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.invoked_methods;
import javax.swing.text.html.HTMLDocument.Iterator; 
 
public class methods_Field { 
 
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception { 
        all_class_field();
        
    }
    public static void all_class_field() throws Exception { 
        // Parse the file 
        FileInputStream in = new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\ProjectOpenDiagnosticImpl.java"); 
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
            n.getBody().ifPresent(b -> b.getStatements().forEach(s -> s.findAll(com.github.javaparser.ast.body.FieldDeclaration.class)));
                                                         
        } 
    }    
        //A method is called multiple times in a class
} 

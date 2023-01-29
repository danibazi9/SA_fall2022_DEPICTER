package com;

import com.github.javaparser.StaticJavaParser; 
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.invoked_methods;

import java.io.FileInputStream; 
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.invoked_methods;
import javax.swing.text.html.HTMLDocument.Iterator; 
 
public class MethodcallExpr { 
 
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException { 
        // Parse the Java file using the StaticJavaParser utility 
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GanttLookAndFeels.java")); 
 
        // Get a list of all MethodDeclaration nodes in the AST 
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class); 
        
        // //new
        // // creating a My HashTable Dictionary
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (MethodDeclaration method : methodDeclarations) { 
            // System.out.println("this method" + method.getNameAsString()); 
            List<MethodCallExpr> calls = method.findAll(MethodCallExpr.class);
            System.out.println("all method call in current method" + Arrays.toString(calls.toArray()));
            System.out.println("------------------------------------------------------------------------");
            int size = calls.size();

            map.put(method.getNameAsString(), size);
        
        }
        
        //A method is called multiple times in a class
    } 
}
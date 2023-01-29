package com;

//import invoked
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration; 
import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 
import com.MethodcallExpr;
import java.io.FileInputStream; 
import java.util.HashSet; 
import java.util.Set; 

//import methodcallexpr
import com.github.javaparser.StaticJavaParser; 
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.invoked_methods;

import java.io.FileInputStream; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.invoked_methods;
import javax.swing.text.html.HTMLDocument.Iterator; 


public class Dependency {
    //invoked_methods
    public static Set<String> invoked_methods() throws Exception{
        // Parse the file 
        FileInputStream in = new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GTextField.java");
        CompilationUnit cu = StaticJavaParser.parse(in); 
        // Visit the methods and get a set of invoked methods 
        Set<String> invokedMethods = new HashSet<>(); 
        new MethodVisitor().visit(cu, invokedMethods); 
        // Print the set of invoked methods 
        // System.out.println(invokedMethods);    
        return invokedMethods;
    }


    private static class MethodVisitor extends VoidVisitorAdapter<Set<String>> { 
        @Override 
        public void visit(MethodDeclaration n, Set<String> invokedMethods) { 
            super.visit(n, invokedMethods); 
            n.getBody().ifPresent(b -> b.getStatements().forEach(s -> s.findAll(com.github.javaparser.ast.expr.MethodCallExpr.class)  
            .forEach(m -> invokedMethods.add(m.getNameAsString())))); 
        } 
    }
    
    
    //MethodcallExpr

    public static ArrayList<String> MethodcallExpr() throws Exception{
        // Parse the Java file using the StaticJavaParser utility 
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GanttStatusBar.java")); 
        // Get a list of all MethodDeclaration nodes in the AST 
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class); 

        ArrayList<String> methodDeclarations_list = new ArrayList<String>();
        for (MethodDeclaration method : methodDeclarations) { 
            methodDeclarations_list.add(method.getNameAsString());
            System.out.println(methodDeclarations_list);

        }

        return methodDeclarations_list;
    }


    public static void MethodDependancy() throws Exception{

        Set<String> invoked = invoked_methods();

        List<String> Methodcall = MethodcallExpr();

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < Methodcall.size(); i++){
            String method = Methodcall.get(i);
            int count_of_call = 0;
            for (String ele : invoked) {
                // System.out.println("ele" + "---------" + ele);
                // System.out.println("---------------------------------------------------------");
                // System.out.println("method" + "---------" + method);
                if(method == ele){
                    count_of_call =+ 1;
                }
            }
            map.put(method, count_of_call);
        }
        System.out.println(map);
        
    }


    public static void main(String[] args) throws Exception { 
        MethodDependancy();

    }
      
    
}

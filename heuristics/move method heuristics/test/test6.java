
import com.github.javaparser.StaticJavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.MethodDeclaration; 

 

import java.io.File; 

import java.io.FileNotFoundException; 

import java.util.List; 

public class test6 { 

 

    public static void main(String[] args) throws Exception { 

        // creates an input stream for the file to be parsed 

        FileInputStream in = new FileInputStream("test.java"); 

 

        CompilationUnit cu; 

        try { 

            // parse the file 

            cu = JavaParser.parse(in); 

        } finally { 

            in.close(); 

        } 

 

        // visit and print the methods names 

        new MethodVisitor().visit(cu, null); 

    } 

 

    /** 

     * Simple visitor implementation for visiting MethodDeclaration nodes.  

     */ 

    private static class MethodVisitor extends VoidVisitorAdapter { 

 

        @Override 

        public void visit(MethodDeclaration n, Object arg) { 

            // here you can access the attributes of the method. 

            // this method will be called for all methods in this  

            // CompilationUnit, including inner class methods 

            System.out.println(n.getName()); 

        } 

    } 

}
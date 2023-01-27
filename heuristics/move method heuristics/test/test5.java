
import com.github.javaparser.StaticJavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.MethodDeclaration; 

 

import java.io.File; 

import java.io.FileNotFoundException; 

import java.util.List; 

 

public class test5 { 

    public static void main(String[] args) throws FileNotFoundException { 

        // Parse the source file 

        File file = new File("MyClass.java"); 

        CompilationUnit cu = StaticJavaParser.parse(file); 

 

        // Visit the class declaration 

        cu.accept(new MethodVisitor(), null); 

    } 

 

    private static class MethodVisitor extends VoidVisitorAdapter<Void> { 

        @Override 

        public void visit(MethodDeclaration n, Void arg) { 

            // Print the method name 

            System.out.println(n.getNameAsString()); 

            super.visit(n, arg); 

        } 

    } 

}
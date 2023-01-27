import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 

 

import java.io.FileInputStream; 

import java.io.FileNotFoundException; 

import java.util.ArrayList; 

import java.util.List; 

 

public class test4 { 

    public static void main(String[] args) { 

        try { 

            FileInputStream in = new FileInputStream("path/to/MyClass.java"); 

            MethodVisitor methodVisitor = new MethodVisitor(); 

            JavaParser.parse(in).accept(methodVisitor, null); 

            List<MethodDeclaration> methods = methodVisitor.getMethods(); 

            for (MethodDeclaration method : methods) { 

                System.out.println(method.getNameAsString()); 

            } 

        } catch (FileNotFoundException e) { 

            e.printStackTrace(); 

        } 

    } 

    private static class MethodVisitor extends VoidVisitorAdapter<Void> { 

        private final List<MethodDeclaration> methods = new ArrayList<>(); 

 

        @Override 

        public void visit(MethodDeclaration n, Void arg) { 

            super.visit(n, arg); 

            methods.add(n); 

        } 

 

        public List<MethodDeclaration> getMethods() { 

            return methods; 

        } 

    } 

}
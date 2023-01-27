import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import com.github.javaparser.ast.body.TypeDeclaration; 

 

import java.io.FileInputStream; 

import java.io.FileNotFoundException; 

import java.util.List; 

 

public class test3 { 

 

    public static void main(String[] args) { 

        try { 

            FileInputStream in = new FileInputStream("path/to/MyClass.java"); 

            TypeDeclaration<?> typeDeclaration = JavaParser.parse(in).getType(0); 

            List<MethodDeclaration> methods = typeDeclaration.findAll(MethodDeclaration.class); 

            for (MethodDeclaration method : methods) { 

                System.out.println(method.getNameAsString()); 

            } 

        } catch (FileNotFoundException e) { 

            e.printStackTrace(); 

        } 

    } 

}
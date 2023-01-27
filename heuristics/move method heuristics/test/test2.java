import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import java.io.FileInputStream; 

import java.io.FileNotFoundException; 

import java.util.List; 

 

public class test2 { 

    public static void main(String[] args) { 

        try { 

            FileInputStream in = new FileInputStream("path/to/MyClass.java"); 

            CompilationUnit cu = JavaParser.parse(in); 

            List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class); 

            classOrInterfaceDeclarations.forEach(classOrInterfaceDeclaration -> { 

                List<MethodDeclaration> methodDeclarations = classOrInterfaceDeclaration.findAll(MethodDeclaration.class); 

                methodDeclarations.forEach(methodDeclaration -> { 

                    System.out.println(methodDeclaration.getNameAsString()); 

                }); 

            }); 

        } catch (FileNotFoundException e) { 

            e.printStackTrace(); 

        } 

    } 

}
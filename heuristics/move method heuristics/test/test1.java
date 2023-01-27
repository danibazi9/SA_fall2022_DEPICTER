import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import java.io.FileInputStream; 

import java.io.FileNotFoundException; 

import java.nio.file.Path; 

import java.nio.file.Paths; 

import java.util.List; 

 

public class test1 { 

    public static void main(String[] args) { 

        // specify the file path 

        Path path = Paths.get("path/to/MyClass.java"); 

        try { 

            //parse the file and get the compilationUnit 

            CompilationUnit cu = JavaParser.parse(new FileInputStream(path.toFile())); 

            // get all class or interface declarations 

            List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class); 

            //iterate through the list 

            classOrInterfaceDeclarations.forEach(classOrInterfaceDeclaration -> { 

                //get all the method declarations 

                List<MethodDeclaration> methodDeclarations = classOrInterfaceDeclaration.findAll(MethodDeclaration.class); 

                //iterate through the methods 

                methodDeclarations.forEach(methodDeclaration -> { 

                    //print the method name 

                    System.out.println(methodDeclaration.getNameAsString()); 

                }); 

            }); 

        } catch (FileNotFoundException e) { 

            e.printStackTrace(); 

        } 

    } 

}
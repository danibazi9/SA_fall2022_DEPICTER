import com.github.javaparser.StaticJavaParser; 
import com.github.javaparser.ast.CompilationUnit; 
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.io.FileInputStream; 
import java.io.IOException;
import java.util.Hashtable;
import java.util.List; 
 
public class MethodExtractor { 
 
    public static void main(String[] args) throws IOException { 
        // Parse the Java file using the StaticJavaParser utility 
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GanttLookAndFeels.java")); 
 
        // Get a list of all MethodDeclaration nodes in the AST 
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class); 
 
        // Iterate through the list and print the name of each method 
        for (MethodDeclaration method : methodDeclarations) { 
            System.out.println(method.getNameAsString()); 
        }
        
    } 
}
package com;

import com.github.javaparser.StaticJavaParser; 
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch; 
 
public class class_Field { 
 
    public static void main(String[] args) throws Exception { 
        // MethodDependancy();
        moveMethod();
    } 


    //only class field
    public static void get_class_field(String inputFilePath) {
    try {
        CompilationUnit cu = StaticJavaParser.parse(new File(inputFilePath));
        cu.findAll(FieldDeclaration.class).forEach(variable -> {
            System.out.println(variable);
        });
    } catch (FileNotFoundException fe) {

    } catch (IOException e) {

    }
    }
    
    //all field in class (class + method)
    public static Set<String> all_class_field() throws Exception { 
        // Parse the file 
        FileInputStream in = new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\ActionUtil.java"); 
        CompilationUnit cu = StaticJavaParser.parse(in); 
 
        // Visit the methods and get a set of invoked methods 
        Set<String> invokedMethods = new HashSet<>(); 
        new MethodVisitor().visit(cu, invokedMethods); 
 
        // Print the set of invoked methods 
        System.out.println(invokedMethods); 

        return invokedMethods;

    }
 
    private static class MethodVisitor extends VoidVisitorAdapter<Set<String>> { 
        @Override 
        public void visit(MethodDeclaration n, Set<String> invokedMethods) { 
            super.visit(n, invokedMethods); 
            n.getBody().ifPresent(b -> b.getStatements().forEach(s -> s.findAll(com.github.javaparser.ast.body.FieldDeclaration.class)));
                                                         
        } 
    }


    //all method field
    /**
     * @return
     * @throws Exception
     */
    public static Map<String, List> all_method_field() throws Exception{
        // Parse the Java file using the StaticJavaParser utility 
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\ActionUtil.java")); 
 
        // Get a list of all MethodDeclaration nodes in the AST 
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class); 
        
        // //new
        // // creating a My HashTable Dictionary
        Map<String, List> map = new HashMap<String, List>();

        for (MethodDeclaration method : methodDeclarations) { 
            // System.out.println("this method" + method.getNameAsString()); 
            List<FieldDeclaration> fields = method.findAll(FieldDeclaration.class);
            // System.out.println(method.getNameAsString() + "all field in method" + Arrays.toString(fields.toArray()));
            // System.out.println("------------------------------------------------------------------------");

            String temp = Arrays.toString(fields.toArray());
            map.put(method.getNameAsString(), fields);
                
        }
        // System.out.println(map);
        return map;

    }

    public static Map<String, Integer> MethodDependancy() throws Exception{

        Set<String> classField = all_class_field();

        Map<String, List> MethodField = all_method_field();

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (Entry<String, List> entry : MethodField.entrySet()){ 
            // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            int count = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                // System.out.println(entry.getValue().get(i));
                for (String ele : classField){
                    if(entry.getValue().get(i) == ele){
                        count =+ 1;
                    } 
                }
            }
            map.put(entry.getKey(), count);
        }
        
        return map;
    }



    //move method huristic
    /**
     * @throws FileNotFoundException
     * 
     */
    public static void moveMethod () throws FileNotFoundException{
        File directory=new File("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source");
        int fileCount=directory.list().length;
        System.out.println("File Count:"+fileCount);

        if (fileCount > 2){
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream("C:\\Users\\mr.ranjbar\\PycharmProjects\\DEPISTER\\source\\GanttLookAndFeels.java")); 
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class); 
        int CountOfMethod = methodDeclarations.size();
        System.out.println("File Count:"+CountOfMethod);
        }
    }

}
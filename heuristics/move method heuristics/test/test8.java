
import java.io.FileInputStream; 

import java.util.HashSet; 

import java.util.Set; 

 

import com.github.javaparser.JavaParser; 

import com.github.javaparser.ast.expr.Expression; 

import com.github.javaparser.ast.expr.MethodCallExpr; 

import com.github.javaparser.ast.expr.NameExpr; 

import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 

import com.github.javaparser.ast.CompilationUnit; 

 

public class test8 { 

    public static void main(String[] args) throws Exception { 

        class MethodVisitor extends VoidVisitorAdapter<Void> { 

            private Set<String> methods = new HashSet<>(); 

            private String currentClass; 

 

            public MethodVisitor(String currentClass) { 

                this.currentClass = currentClass; 

            } 

 

            @Override 

            public void visit(MethodCallExpr n, Void arg) { 

                if (n.getScope().isPresent()) { 

                    Expression scope = n.getScope().get(); 

                    if (scope instanceof NameExpr) { 

                        NameExpr nameExpr = (NameExpr) scope; 

                        if (nameExpr.getNameAsString().equals(currentClass)) { 

                            methods.add(n.getNameAsString()); 

                        } 

                    } 

                } 

            } 

 

            public Set<String> getMethods() { 

                return methods; 

            } 

        } 

 

        FileInputStream in = new FileInputStream("MyClass.java"); 

        CompilationUnit cu = JavaParser.parse(in); 

        MethodVisitor methodVisitor = new MethodVisitor("MyClass"); 

        cu.accept(methodVisitor, null); 

 

        Set<String> methods = methodVisitor.getMethods(); 

        System.out.println("Methods that call each other within the class: " + methods); 

    } 

}
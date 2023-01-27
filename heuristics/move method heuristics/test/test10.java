
import com.github.javaparser.StaticJavaParser; 

import com.github.javaparser.ast.CompilationUnit; 

import com.github.javaparser.ast.body.MethodDeclaration; 

import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration; 

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade; 

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver; 

import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver; 

import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver; 

 

import java.io.FileInputStream; 

import java.util.HashMap; 

import java.util.Map; 

 

public class test10 { 

    public static void main(String[] args) throws Exception { 

        // Parse the file 

        FileInputStream in = new FileInputStream("path/to/your/file.java"); 

        CompilationUnit cu = StaticJavaParser.parse(in); 

         

        // Create a type solver and a facade 

        CombinedTypeSolver typeSolver = new CombinedTypeSolver(); 

        typeSolver.add(new ReflectionTypeSolver()); 

        typeSolver.add(new JavaParserTypeSolver(new File("path/to/your/project"))); 

        JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver); 

 

        // Visit the methods and get a map of invoked methods with class names 

        Map<String, String> invokedMethods = new HashMap<>(); 

        new MethodVisitor(javaParserFacade).visit(cu, invokedMethods); 

 

        // Print the map of invoked methods 

        System.out.println(invokedMethods); 

    } 

 

    private static class MethodVisitor extends VoidVisitorAdapter<Map<String, String>> { 

        private final JavaParserFacade javaParserFacade; 

 

        public MethodVisitor(JavaParserFacade javaParserFacade) { 

            this.javaParserFacade = javaParserFacade; 

        } 

         

        @Override 

        public void visit(MethodDeclaration n, Map<String, String> invokedMethods) { 

            super.visit(n, invokedMethods); 

            n.getBody().ifPresent(b -> b.getStatements().forEach(s -> s.findAll(com.github.javaparser.ast.expr.MethodCallExpr.class) 

                                                            .forEach(m -> { 

                                                              ResolvedMethodDeclaration method = javaParserFacade.solve(m).getCorrespondingDeclaration(); 

                                                              invokedMethods.put(method.getNameAsString(), method.declaringType().getQualifiedName()); 

                                                            }))); 

        } 

    } 

}
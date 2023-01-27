class MethodVisitor extends test11<Void> { 

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

                    System.out.println("Invoked method: " + n.getNameAsString()); 

                } 

            } 

        } 

    } 

} 

 

FileInputStream in = new FileInputStream("MyClass.java"); 

CompilationUnit cu = JavaParser.parse(in); 

cu.accept(new MethodVisitor("MyClass"), null);
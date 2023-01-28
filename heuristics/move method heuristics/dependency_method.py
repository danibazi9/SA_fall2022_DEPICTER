from javaparser.ast.CompilationUnit import CompilationUnit
from javaparser.ast.body.MethodDeclaration import MethodDeclaration
from javaparser.ast.expr.MethodCallExpr import MethodCallExpr

# Parse the Java source code
cu = CompilationUnit.from_source("../src/ganttproject-master/biz.ganttproject.app.libs/lib")

# Find all methods in the class
methods = cu.find_all(MethodDeclaration)

# Create a dictionary to store the method dependencies
dependencies = {}

# Iterate over the methods
for method in methods:
    # Find all method call expressions in the method
    calls = method.find_all(MethodCallExpr)
    # Iterate over the calls
    for call in calls:
        # Get the name of the called method
        called_method = call.name
        # Add the dependency to the dictionary
        if method.name in dependencies:
            dependencies[method.name].append(called_method)
        else:
            dependencies[method.name] = [called_method]

# Print the dependencies
print(dependencies)

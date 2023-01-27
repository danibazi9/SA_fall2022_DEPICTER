#from org.antlr.v4.runtime import ANTLRFileStream
#from antlr.v4.runtime import CommonTokenStream
from JavaLexer import JavaLexer
from JavaParserLabeled import JavaParserLabeled

from antlr4 import ANTLRFileStream
from antlr4 import CommonTokenStream

# Create the lexer and parser
input_stream = ANTLRFileStream("C:\Users\mr.ranjbar\PycharmProjects\DEPISTER\source")
lexer = JavaLexer(input_stream)
tokens = CommonTokenStream(lexer)
parser = JavaParserLabeled(tokens)

# Parse the compilation unit
cu = parser.compilationUnit()

# Find the class that contains the method
class_ = cu.classDeclaration(name="SourceClass")

# Find the method to move
method = class_.methodDeclaration(name="methodToMove")

# Create the target class
target_class = cu.classDeclaration(name="TargetClass")

# Add the method to the target class
target_class.methods().add(method)

# Remove the method from the source class
class_.methods().remove(method)

# Print the modified Java source code
print(cu.toStringTree())
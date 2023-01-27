from antlr4 import CommonTokenStream
from antlr4.TokenStreamRewriter import TokenStreamRewriter

from gen.javaLabeled.JavaParserLabeled import JavaParserLabeled
from gen.javaLabeled.JavaParserLabeledListener import JavaParserLabeledListener


class MyListener(JavaParserLabeledListener):
    """
    To detect whether class needs refactoring or not.
    """

    def __init__(self, common_token_stream: CommonTokenStream = None, package_classes_dict=None):
        """
        :param common_token_stream:
        """
        self.token_stream = common_token_stream
        self.method_count = 0
        self.package_classes_dict = package_classes_dict
        self.package_name = None

        # Move all the tokens in the source code in a buffer, token_stream_rewriter.
        if common_token_stream is not None:
            self.token_stream_rewriter = TokenStreamRewriter(common_token_stream)
        else:
            raise TypeError('common_token_stream is None')

    # Enter a parse tree produced by JavaParserLabeled#methodDeclaration.
    def exitMethodDeclaration(self, ctx: JavaParserLabeled.MethodDeclarationContext):
        self.method_count = self.method_count + 1

    # Exit a parse tree produced by JavaParserLabeled#classDeclaration.
    def exitClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext):
        self.package_classes_dict[self.package_name] += 1

    # Enter a parse tree produced by JavaParserLabeled#packageDeclaration.
    def enterPackageDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext):
        self.package_name = ctx.qualifiedName().getText()
        if self.package_name not in self.package_classes_dict:
            self.package_classes_dict[self.package_name] = 0

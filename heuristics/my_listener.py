from antlr4 import CommonTokenStream
from antlr4.TokenStreamRewriter import TokenStreamRewriter

from gen.javaLabeled.JavaParserLabeled import JavaParserLabeled
from gen.javaLabeled.JavaParserLabeledListener import JavaParserLabeledListener


class MyListener(JavaParserLabeledListener):
    """
    To detect whether class needs refactoring or not.
    """

    def __init__(self, common_token_stream: CommonTokenStream = None, package_classes_dict=None,class_method_dict=None):
        """
        :param common_token_stream:
        """
        self.token_stream = common_token_stream
        self.method_count = 0
        self.package_classes_dict = package_classes_dict
        self.class_method_dict=class_method_dict
        self.package_name = None

        # Move all the tokens in the source code in a buffer, token_stream_rewriter.
        if common_token_stream is not None:
            self.token_stream_rewriter = TokenStreamRewriter(common_token_stream)
        else:
            raise TypeError('common_token_stream is None')

    # Enter a parse tree produced by JavaParserLabeled#methodDeclaration.
    def exitMethodDeclaration(self, ctx: JavaParserLabeled.MethodDeclarationContext):
        self.method_count = self.method_count + 1

    # def enterMethodDeclaration(self, ctx: JavaParserLabeled.MethodDeclarationContext):
    #     self.class_methods_dict[self.class_name][self.method_name].append(ctx.IDENTIFIER().getText())

    # Exit a parse tree produced by JavaParserLabeled#classDeclaration.
    def exitClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext):
        self.package_classes_dict[self.package_name] += 1

    # Enter a parse tree produced by JavaParserLabeled#packageDeclaration.
    def enterPackageDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext):
        self.package_name = ctx.qualifiedName().getText()
        if self.package_name not in self.package_classes_dict:
            self.package_classes_dict[self.package_name] = 0


    # def enterMethodCall(self,ctx: JavaParserLabeled.ClassDeclarationContext):
    #     self.method_name=ctx.IDENTIFIER().getText()
    #     if self.method_name not in self.class_methods_dict:
    #         self.class_methods_dict[self.method_name]=[]

#########for huristic move method

    def enterMethodDeclaration(self, ctx: JavaParserLabeled.MethodDeclarationContext): ##method 2
        self.package_classes_dict[self.package_name][self.class_name].append(ctx.IDENTIFIER().getText())


    # Exit a parse tree produced by JavaParserLabeled#classDeclaration.
    # def enterClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext): ###method1
    #     self.class_name=ctx.IDENTIFIER().getText() #######IDENTIFIER
    #
    #     self.package_classes_dict[self.package_name] [self.class_name]=[]
    def enterMethodDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext): ###method1
        self.method_name=ctx.IDENTIFIER().getText() #######IDENTIFIER

        self.class_method_dict[self.class_name] [self.method_name]=[]

    # Enter a parse tree produced by JavaParserLabeled#packageDeclaration.
    # def enterPackageDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext): ##class
    #     self.package_name = ctx.qualifiedName().getText()
    #     if self.package_name not in self.package_classes_dict:
    #         self.package_classes_dict[self.package_name] = {}

    def enterClassDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext): ##class
        self.class_name=ctx.IDENTIFIER().getText()
        if self.class_name not in self.class_method_dict:
            self.class_method_dict[self.class_name] = {}
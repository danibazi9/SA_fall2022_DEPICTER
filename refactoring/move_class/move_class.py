import os

from antlr4 import *
from antlr4.TokenStreamRewriter import TokenStreamRewriter

from gen.javaLabeled.JavaParserLabeled import JavaParserLabeled
from gen.javaLabeled.JavaParserLabeledListener import JavaParserLabeledListener


class MoveClassRefactoringRecognizer(JavaParserLabeledListener):
    def __init__(self, common_token_stream: CommonTokenStream = None, class_identifier: str = None,
                 source_package: str = None, target_package: str = None):
        """
        :param common_token_stream:
        """
        self.enter_source_package = False
        self.enter_target_package = False
        self.class_found = False
        self.status = 0
        self.token_stream = common_token_stream

        # Move all the tokens in the source code in a buffer, token_stream_rewriter.
        if common_token_stream is not None:
            self.token_stream_rewriter = TokenStreamRewriter(common_token_stream)
        else:
            raise TypeError('common_token_stream is None')

        if class_identifier is not None:
            self.class_identifier = class_identifier
        else:
            raise ValueError("class_identifier is None")

        if source_package is not None:
            self.source_package = source_package
        else:
            raise ValueError("source_package is None")

        if target_package is not None:
            self.target_package = target_package
        else:
            raise ValueError("target_package is None")

    # Enter a parse tree produced by JavaParserLabeled#packageDeclaration.
    def enterPackageDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext):
        package_name = ctx.qualifiedName().getText()
        if package_name == self.source_package:
            self.enter_source_package = True
        elif package_name == self.target_package:
            self.enter_target_package = True

    # Exit a parse tree produced by JavaParserLabeled#compilationUnit.
    def exitCompilationUnit(self, ctx: JavaParserLabeled.CompilationUnitContext):
        if not self.class_found and self.enter_source_package:
            self.status = -2

        self.enter_source_package = False
        self.enter_target_package = False

    # Enter a parse tree produced by JavaParserLabeled#classDeclarationContext.
    def enterClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext):
        class_name = ctx.IDENTIFIER().getText()

        if class_name == self.class_identifier:
            if self.enter_source_package:
                self.status = 1
                self.class_found = True
            if self.enter_target_package:
                self.status = -1


class MoveClassRefactoringListener(JavaParserLabeledListener):
    """
    To implement the move class refactoring
    a stream of tokens is sent to the listener, to build an object token_stream_rewriter
    and we move all class methods and fields from the source package to the target package
    """

    def __init__(self, common_token_stream: CommonTokenStream = None, class_identifier: str = None,
                 source_package: str = None, target_package: str = None, filename: str = None, directory: str = None):
        """
        :param common_token_stream:
        """
        self.enter_class = False
        self.token_stream = common_token_stream
        self.class_fields = []
        self.class_methods = []

        # Move all the tokens in the source code in a buffer, token_stream_rewriter.
        if common_token_stream is not None:
            self.token_stream_rewriter = TokenStreamRewriter(common_token_stream)
        else:
            raise TypeError('common_token_stream is None')

        if class_identifier is not None:
            self.class_identifier = class_identifier
        else:
            raise ValueError("class_identifier is None")

        if filename is not None:
            self.filename = filename.replace('\\', '/')
        else:
            raise ValueError("filename is None")

        if directory is not None:
            self.directory = directory
        else:
            raise ValueError("directory is None")

        if source_package is not None:
            self.source_package = source_package
        else:
            raise ValueError("source_package is None")

        if target_package is not None:
            self.target_package = target_package
        else:
            raise ValueError("target_package is None")

        self.TAB = "\t"
        self.NEW_LINE = "\n"
        self.code = ""

    # Exit a parse tree produced by JavaParserLabeled#importDeclaration.
    def exitImportDeclaration(self, ctx: JavaParserLabeled.ImportDeclarationContext):
        self.code += ctx.getText() + self.NEW_LINE

    # Enter a parse tree produced by JavaParserLabeled#packageDeclaration.
    def enterPackageDeclaration(self, ctx: JavaParserLabeled.PackageDeclarationContext):
        package_name = ctx.qualifiedName().getText()
        if package_name != self.source_package:
            raise ValueError(f"The package {package_name} in the file isn't equal to the source package!")

    # Enter a parse tree produced by JavaParserLabeled#classBodyDeclaration2.
    def enterClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext):
        if ctx.IDENTIFIER().getText() != self.class_identifier:
            return

        self.enter_class = True
        print("Refactoring started, please wait...")

        start_index = ctx.start.tokenIndex
        stop_index = ctx.stop.tokenIndex

        # get the class body from the token_stream_rewriter
        class_body = self.token_stream_rewriter.getText(
            program_name=self.token_stream_rewriter.DEFAULT_PROGRAM_NAME,
            start=start_index,
            stop=stop_index
        )

        self.code = f"package {self.target_package};"
        self.code += self.NEW_LINE * 2
        self.code += f"// Class \"{self.class_identifier}\" moved here " \
                     f"from package {self.source_package}" + self.NEW_LINE + \
                     f"{class_body}"

        # delete class declaration from source class
        self.token_stream_rewriter.delete(
            program_name=self.token_stream_rewriter.DEFAULT_PROGRAM_NAME,
            from_idx=start_index,
            to_idx=stop_index
        )

        old_file = open(self.filename, 'w')
        old_file.write(self.token_stream_rewriter.getDefaultText().replace("\r", ""))

    # Exit a parse tree produced by JavaParserLabeled#classBodyDeclaration2.
    def exitClassDeclaration(self, ctx: JavaParserLabeled.ClassDeclarationContext):
        if self.enter_class:
            print("----------------------------")
            print("Class attributes: ", str(self.class_fields))
            print("Class methods: ", str(self.class_methods))
            print("----------------------------")
            self.enter_class = False

    # Enter a parse tree produced by JavaParserLabeled#fieldDeclaration.
    def enterFieldDeclaration(self, ctx: JavaParserLabeled.FieldDeclarationContext):
        if not self.enter_class:
            return

        list_of_fields = ctx.variableDeclarators().getText().split(",")

        for field in list_of_fields:
            self.class_fields.append(field)

    # Enter a parse tree produced by JavaParserLabeled#methodDeclaration.
    def enterMethodDeclaration(self, ctx: JavaParserLabeled.MethodDeclarationContext):
        if not self.enter_class:
            return

        method_name = ctx.IDENTIFIER().getText()
        self.class_methods.append(method_name)

    # Exit a parse tree produced by JavaParserLabeled#compilationUnit.
    def exitCompilationUnit(self, ctx: JavaParserLabeled.CompilationUnitContext):
        file_address = self.directory + '/' + self.target_package.replace('.', '/') + '/' + self.class_identifier + '.java'

        new_file = open(file_address, 'w')
        new_file.write(self.code.replace("\r", ""))
        print(f"The class \"{self.class_identifier}\" moved to the target package successfully!")

        need_to_be_removed = False
        old_file = open(self.filename, 'r')
        old_file_context = old_file.read()
        if 'class' not in old_file_context and 'enum' not in old_file_context and 'interface' not in old_file_context:
            need_to_be_removed = True

        old_file.close()
        if need_to_be_removed:
            os.remove(self.filename)

        print("Finished Processing...")


class ReplaceDependentObjectsListener(JavaParserLabeledListener):
    """
    To implement the move class refactoring
    a stream of tokens is sent to the listener, to build an object token_stream_rewriter
    and we move all class methods and fields from the source package to the target package
    """

    def __init__(self, common_token_stream: CommonTokenStream = None, class_identifier: str = None,
                 source_package: str = None, target_package: str = None, filename: str = None, has_import: bool = None):
        """
        :param common_token_stream:
        """
        self.token_stream = common_token_stream

        # Move all the tokens in the source code in a buffer, token_stream_rewriter.
        if common_token_stream is not None:
            self.token_stream_rewriter = TokenStreamRewriter(common_token_stream)
        else:
            raise TypeError('common_token_stream is None')

        if class_identifier is not None:
            self.class_identifier = class_identifier
        else:
            raise ValueError("class_identifier is None")

        if filename is not None:
            self.filename = filename
        else:
            raise ValueError("filename is None")

        if has_import is not None:
            self.has_import = has_import
        else:
            raise ValueError("has_import is None")

        if source_package is not None:
            self.source_package = source_package
        else:
            raise ValueError("source_package is None")

        if target_package is not None:
            self.target_package = target_package
        else:
            raise ValueError("target_package is None")

        self.need_import = False
        self.TAB = "\t"
        self.NEW_LINE = "\n"
        self.code = ""

    # Exit a parse tree produced by JavaParserLabeled#importDeclaration.
    def exitImportDeclaration(self, ctx: JavaParserLabeled.ImportDeclarationContext):
        if not ctx.qualifiedName().getText().endswith(self.source_package + '.' + self.class_identifier):
            return

        start_index = ctx.start.tokenIndex
        stop_index = ctx.stop.tokenIndex

        text_to_replace = "import " + self.target_package + '.' + self.class_identifier + ';'
        if ctx.STATIC() is not None:
            text_to_replace = text_to_replace.replace("import", "import static")

        # replace the import source package with target package
        self.token_stream_rewriter.replace(
            program_name=self.token_stream_rewriter.DEFAULT_PROGRAM_NAME,
            from_idx=start_index,
            to_idx=stop_index,
            text=text_to_replace
        )

    # Exit a parse tree produced by JavaParserLabeled#classOrInterfaceType.
    def exitClassOrInterfaceType(self, ctx: JavaParserLabeled.ClassOrInterfaceTypeContext):
        if not self.has_import or not self.need_import:
            if self.class_identifier in ctx.getText().split('.'):
                self.need_import = True

    # Exit a parse tree produced by JavaParserLabeled#createdName0.
    def exitCreatedName0(self, ctx: JavaParserLabeled.CreatedName0Context):
        if not self.has_import or not self.need_import:
            if self.class_identifier in ctx.getText().split('.'):
                self.need_import = True

    # Exit a parse tree produced by JavaParserLabeled#expression1.
    def exitExpression1(self, ctx: JavaParserLabeled.Expression1Context):
        if not self.has_import or not self.need_import:
            if ctx.expression().getText == self.class_identifier:
                self.need_import = True

    # Exit a parse tree produced by JavaParserLabeled#typeDeclaration.
    def exitTypeDeclaration(self, ctx: JavaParserLabeled.TypeDeclarationContext):
        if ctx.classDeclaration() is not None:
            if not self.has_import or self.need_import:
                index = ctx.start.tokenIndex

                # delete class declaration from source class
                self.token_stream_rewriter.insertBefore(
                    program_name=self.token_stream_rewriter.DEFAULT_PROGRAM_NAME,
                    index=index,
                    text="import " + self.target_package + '.' + self.class_identifier + ';' + self.NEW_LINE
                )

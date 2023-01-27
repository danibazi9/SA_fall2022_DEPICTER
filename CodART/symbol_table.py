from antlr4 import ParseTreeWalker, FileStream, CommonTokenStream
from antlr4.TokenStreamRewriter import TokenStreamRewriter

from gen.javaLabeled import JavaLexer
from gen.javaLabeled import JavaParserLabeled


def create_project_parse_tree(file_path):
    # Step 1: Load input source into stream
    stream = FileStream(file_path, encoding='utf8')
    # Step 2: Create an instance of AssignmentStLexer
    lexer = JavaLexer(stream)
    # Step 3: Convert the input source into a list of tokens
    token_stream = CommonTokenStream(lexer)
    # Step 4: Create an instance of the AssignmentStParser
    parser = JavaParserLabeled(token_stream)
    parser.getTokenStream()
    # Step 5: Create parse tree
    parse_tree = parser.compilationUnit()
    # Step 6: Create an instance of AssignmentStListener
    tokens = parse_tree.parser.getInputStream()
    rewriter = TokenStreamRewriter(tokens)

    return parse_tree, rewriter


def parse_and_walk(file_path: str, listener_class, has_write=False, debug=False, **kwargs):
    tree, rewriter = create_project_parse_tree(file_path)
    if has_write:
        if rewriter is None:
            raise Exception("Failed to create rewriter.")
        kwargs.update({'rewriter': rewriter})
    listener = listener_class(**kwargs)
    ParseTreeWalker().walk(
        listener,
        tree
    )

    if has_write:
        if not debug:
            with open(file_path, mode='w', encoding='utf-8', errors='ignore', newline='') as f:
                f.write(listener.rewriter.getDefaultText())
        else:
            print(listener.rewriter.getDefaultText())

    return listener

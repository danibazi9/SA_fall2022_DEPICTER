import argparse
import os

from gen.javaLabeled.JavaLexer import *
from gen.javaLabeled.JavaParserLabeled import JavaParserLabeled
from move_class import MoveClassRefactoringListener, ReplaceDependentObjectsListener, MoveClassRefactoringRecognizer

class_identifier = 'C'
source_package = 'a.aa'
target_package = 'c'

directory = '../../src/move_class'
file_counter = 0
flag = False
changed_filename = directory + '/' + target_package.replace('.', '/') + '/' + class_identifier + '.java'


def check_need_to_be_refactored(args):
    global flag

    if not flag:
        # Step 1: Load input source into stream
        stream = FileStream(args.file, encoding='utf8')
        # Step 2: Create an instance of AssignmentStLexer
        lexer = JavaLexer(stream)
        # Step 3: Convert the input source into a list of tokens
        token_stream = CommonTokenStream(lexer)
        # Step 4: Create an instance of the AssignmentStParser
        parser = JavaParserLabeled(token_stream)
        # Step 5: Create parse tree
        parse_tree = parser.compilationUnit()
        # Step 6: Create an instance of RecognizerListener
        recognizer_listener = MoveClassRefactoringRecognizer(
            common_token_stream=token_stream, source_package=source_package,
            target_package=target_package, class_identifier=class_identifier
        )
        walker = ParseTreeWalker()
        walker.walk(t=parse_tree, listener=recognizer_listener)

        if recognizer_listener.status == -1:
            print(f"[Redundant]: doesn't need to refactor."
                  f"\nThe class \"{class_identifier}\" already exists in package \"{target_package}\"!")
            flag = True


def main(args):
    global flag

    if not flag:
        # Step 1: Load input source into stream
        stream = FileStream(args.file, encoding='utf8')
        # Step 2: Create an instance of AssignmentStLexer
        lexer = JavaLexer(stream)
        # Step 3: Convert the input source into a list of tokens
        token_stream = CommonTokenStream(lexer)
        # Step 4: Create an instance of the AssignmentStParser
        parser = JavaParserLabeled(token_stream)
        parser.getTokenStream()
        # Step 5: Create parse tree
        parse_tree = parser.compilationUnit()
        # Step 6: Create an instance of RecognizerListener
        recognizer_listener = MoveClassRefactoringRecognizer(
            common_token_stream=token_stream, source_package=source_package,
            target_package=target_package, class_identifier=class_identifier
        )
        walker = ParseTreeWalker()
        walker.walk(t=parse_tree, listener=recognizer_listener)

        if recognizer_listener.status == -2:
            print(f"The class \"{class_identifier}\" NOT FOUND in package {source_package}!")
            flag = True
        elif recognizer_listener.status == 1:
            my_listener = MoveClassRefactoringListener(
                common_token_stream=token_stream, source_package=source_package,
                target_package=target_package, class_identifier=class_identifier,
                filename=args.file, directory=directory
            )
            walker = ParseTreeWalker()
            walker.walk(t=parse_tree, listener=my_listener)


def replace_dependent_objects(args):
    global file_counter, changed_filename

    has_import = False
    has_exact_import = False

    file_to_check = open(file=args.file, mode='r')
    if args.file == changed_filename:
        has_exact_import = True
    else:
        for line in file_to_check.readlines():
            text_line = line.replace('\n', '').replace('\r', '').strip()
            if text_line.startswith('import') and text_line.endswith(source_package + '.' + class_identifier + ';'):
                has_import = True
                break
            if text_line.startswith('import') and text_line.endswith(target_package + '.' + class_identifier + ';'):
                has_exact_import = True
                break

    if not has_exact_import:
        print(f"Start checking file \"{file_to_check.name}\" *** {file_counter}/100")

        # Step 1: Load input source into stream
        stream = FileStream(args.file, encoding='utf8')
        # Step 2: Create an instance of AssignmentStLexer
        lexer = JavaLexer(stream)
        # Step 3: Convert the input source into a list of tokens
        token_stream = CommonTokenStream(lexer)
        # Step 4: Create an instance of the AssignmentStParser
        parser = JavaParserLabeled(token_stream)
        parser.getTokenStream()
        # Step 5: Create parse tree
        parse_tree = parser.compilationUnit()
        # Step 6: Create an instance of ReplaceDependentObjectsListener
        my_listener = ReplaceDependentObjectsListener(
            common_token_stream=token_stream, source_package=source_package, target_package=target_package,
            class_identifier=class_identifier, filename=args.file, has_import=has_import
        )
        walker = ParseTreeWalker()
        walker.walk(t=parse_tree, listener=my_listener)

        with open(args.file, mode='w', newline='') as f:
            f.write(my_listener.token_stream_rewriter.getDefaultText().replace("\r", ""))

        print(f"Finish checking file \"{file_to_check.name}\" *** {file_counter}/100")
        file_counter += 1


def recursive_walk(dir, status):
    for dirname, dirs, files in os.walk(dir):
        for file in files:
            file_without_extension, extension = os.path.splitext(file)
            if extension == '.java':
                process_file("{}/{}".format(dirname, file), status)


def process_file(file, status):
    argparser = argparse.ArgumentParser()
    argparser.add_argument(
        '-n', '--file',
        help='Input source', default=file)

    args = argparser.parse_args()
    if status == 1:
        check_need_to_be_refactored(args)
    elif status == 2:
        main(args)
    elif status == 3:
        replace_dependent_objects(args)


if __name__ == '__main__':
    if not os.path.exists(directory + '/' + source_package.replace('.', '/')):
        raise NotADirectoryError(f"The package \"{source_package}\" NOT FOUND!")

    if not os.path.exists(directory + '/' + target_package.replace('.', '/')):
        raise NotADirectoryError(f"The package \"{target_package}\" NOT FOUND!")

    # First: Check to sure need to be refactored
    recursive_walk(directory, status=1)

    # Second: for doing move class refactoring
    if not flag:
        recursive_walk(directory, status=2)

    # Second: for checking and updating other files
    if not flag:
        recursive_walk(directory, status=3)

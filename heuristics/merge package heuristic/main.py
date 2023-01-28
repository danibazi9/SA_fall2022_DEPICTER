from antlr4 import *

import argparse
import os

from my_listener import MyListener
from gen.javaLabeled.JavaLexer import JavaLexer
from gen.javaLabeled.JavaParserLabeled import JavaParserLabeled
from merge_package_heuristic import choose_packages

package_classes_dict = {}
directory = '../../src/ganttproject-master/biz.ganttproject.core/src/main'


def main(args):
    global package_classes_dict

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
    # Step 6: Create an instance of AssignmentStListener
    my_listener = MyListener(common_token_stream=token_stream, package_classes_dict=package_classes_dict)
    walker = ParseTreeWalker()
    walker.walk(t=parse_tree, listener=my_listener)

    package_classes_dict = my_listener.package_classes_dict
    # print(args.file, '=>', my_listener.method_count)


def recursive_walk(directory):
    for dirname, dirs, files in os.walk(directory):
        for filename in files:
            filename_without_extension, extension = os.path.splitext(filename)
            if extension == '.java':
                process_file("{}/{}".format(dirname, filename))


def process_file(file):
    argparser = argparse.ArgumentParser()
    argparser.add_argument(
        '-n', '--file',
        help='Input source', default=file)

    args = argparser.parse_args()
    main(args)


if __name__ == '__main__':
    recursive_walk(directory)

    for first_package in package_classes_dict:
        for second_package in package_classes_dict:
            if first_package in second_package and first_package != second_package:
                package_classes_dict[first_package] += package_classes_dict[second_package]

    for package, num_of_classes in package_classes_dict.items():
        print(package, '=>', num_of_classes)

    print('Less than average p1 & p2:')
    print(choose_packages(package_classes_dict))
    # merge_packages(package_classes_dict, directory)

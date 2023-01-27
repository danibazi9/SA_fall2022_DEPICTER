import os
import subprocess
import understand as und

from antlr4 import FileStream, CommonTokenStream
from antlr4.TokenStreamRewriter import TokenStreamRewriter

from gen.javaLabeled import JavaLexer
from gen.javaLabeled import JavaParserLabeled

BENCHMARKS = {
    # Project Path
    'PROJ': [
        '10_water-simulator',  # 0
        '61_noen',  # 1
        '88_jopenchart',  # 2
        '104_vuze',  # 3, Not ready
        '105_freemind',  # 4
        '107_weka',  # 5
        'commons-codec',  # 6
        'ganttproject_1_11_1_original',  # 7
        'jfreechart-master',  # 8
        'JSON20201115',  # 9
        'jvlt-1.3.2',  # 10
        'tabula-java',  # 11
        'JHotDraw-7.0.6',  # 12
        'jhotdraw-6.0b1',  # 13
    ],

    # Understand DB Path
    'UDB': [
        # '10_water-simulator.udb',
        '10_water-simulator.und',
        # '61_noen.udb',
        '61_noen.und',
        # '88_jopenchart.udb',
        '88_jopenchart.und',
        # '104_vuze.udb',  # Not ready
        '104_vuze.und',  # Not ready
        # '105_freemind.udb',
        '105_freemind.und',
        # '107_weka.udb',
        '107_weka.und',
        # 'commons-codec.udb',
        'commons-codec.und',
        # 'ganttproject_1_11_1_original.udb',
        'ganttproject_1_11_1_original.und',
        # 'jfreechart-master.udb',
        'jfreechart-master.und',
        # 'JSON20201115.udb',
        'JSON20201115.und',
        # 'jvlt-1.3.2.udb',
        'jvlt-1.3.2.und',
        # 'tabula-java.udb',
        'tabula-java.und',
        # 'JHotDraw-7.0.6.udb',  # 12
        'JHotDraw-7.0.6.und',  # 12
        # 'jhotdraw-6.0b1.udb',  # 13
        'jhotdraw-6.0b1.und',  # 13
    ],

    # CSV files path containing code smells identified by JDeodorant
    'LONG_METHOD': [
        '10_water-simulator/fake.csv',
        '61_noen/fake.csv',
        '88_jopenchart/fake.csv',
        '104_vuze/fake.csv',
        '105_freemind/Long-Method2_FreeMind-0.9.0.csv',
        '107_weka/Long-Method2-Weka3.8.csv',
        'commons-codec/fake.csv',
        'ganttproject_1_11_1_original/Long-Method2_ganttproject-1.11.1.csv',
        'jfreechart-master/Long-Method2_JFreeChart-1.0.19.csv',
        'JSON20201115/Long-Method2_JASON-20201115.csv',
        'jVLT-1.3.2/Long-Method2_jvlt-1.3.2.csv',
        'tabula-java/fake.csv',
        'JHotDraw-7.0.6/Long-Method2_JHotDraw-7.0.6.csv',
        'jhotdraw-6.0b1/Long-Method2_jhotdraw-6.0b1.csv',
    ],

    'FEATURE_ENVY': [
        '10_water-simulator/Feature-Envy_10_water-simulator-v2.csv',
        '61_noen/Feature-Envy_Noen-v0.1.csv',
        '88_jopenchart/Feature-Envy_JOpenChart-v0.94.csv',
        '104_vuze/Feature-Envy_104_vuze.csv',
        '105_freemind/Feature-Envy2_FreeMind-0.9.0.csv',
        '107_weka/Feature-Envy2-Weka3.8.csv',
        'commons-codec/Feature-Envy_Commons-codec-v1.15.csv',
        'ganttproject_1_11_1_original/Feature-Envy2_ganttproject-1.11.1.csv',
        'jfreechart-master/Feature-Envy2-JFreeChart-1.0.19.csv',
        'JSON20201115/Feature-Envy2_JASON-20201115.csv',
        'jVLT-1.3.2/Feature-Envy_jvlt-1.3.2.csv',
        'tabula-java/Feature-Envy-Tabula-v1.0.6.csv',
        'JHotDraw-7.0.6/Featuer-Envy2_JHotDraw-7.0.6.csv',
        'jhotdraw-6.0b1/Featuer-Envy2_jhotdraw-6.0b1.csv',
    ],

    'GOD_CLASS': [
        '10_water-simulator/God-Class_10_water-simulator-v2.csv',
        '61_noen/God-Class_Noen-v0.1.csv',
        '88_jopenchart/God-Class_JOpenChart-v0.94.csv',
        '104_vuze/God-Class_104_vuze.csv',
        '105_freemind/God-Class_FreeMind-0.9.0.csv',
        '107_weka/God-Class-Weka3.8.csv',
        'commons-codec/God-Class_Commons-codec-v1.15.csv',
        'ganttproject_1_11_1_original/God-Class_ganttproject-1.11.1.csv',
        'jfreechart-master/God-Class-JFreeChart-1.0.19.csv',
        'JSON20201115/God-Class_JASON-20201115.csv',
        'jVLT-1.3.2/God-Class_jvlt-1.3.2.csv',
        'tabula-java/God-Class-Tabula-v1.0.6.csv',
        'JHotDraw-7.0.6/God-Class_JHotDraw-7.0.6.csv',
        'jhotdraw-6.0b1/God-Class_jhotdraw-6.0b1.csv',
    ],
}
BENCHMARK_INDEX = int(os.environ.get("BENCHMARK_INDEX", 0))

UDB_ROOT_DIR = "//"
UDB_PATH = os.path.join(UDB_ROOT_DIR, BENCHMARKS['UDB'][BENCHMARK_INDEX]).replace('/', '\\')


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


def update_understand_database(udb_path):
    understand_6_cmd = ['und', 'analyze', '-changed', udb_path]  # -rescan option is not required for understand >= 6.0

    result = subprocess.run(understand_6_cmd,
                            stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE)

    trials = 0
    while result.returncode != 0:
        try:
            db: und.Db = und.open(UDB_PATH)
            db.close()
        except:
            pass
        finally:

            result = subprocess.run(understand_6_cmd,  #
                                    stdout=subprocess.PIPE,
                                    stderr=subprocess.PIPE)
            # info_ = result.stdout.decode('utf-8')
            error_ = result.stderr.decode('utf-8')
            # print(info_[:85])
            print(f'return code: {result.returncode} msg: {error_}')
            trials += 1
            if trials > 5:
                break

    # Try to close und.exe process if it has not been killed automatically
    result = subprocess.run(['taskkill', '/f', '/im', 'und.exe'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode != 0:
        print('The und.exe process is not running')
    else:
        print('The und.exe process killed manually')

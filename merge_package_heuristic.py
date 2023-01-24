from statistics import mean



def calculate_avg(package_classes_dict):
    return mean(package_classes_dict)


def choose_packages(package_classes_dict):
    average = int(calculate_avg(list(package_classes_dict.values())))
    chosen_list = []

    for key, value in package_classes_dict.items():
        if value < average:
            chosen_list.append([key, value])

    return chosen_list[:2]


def merge_packages(package_classes_dict, directory):
    packages_list = choose_packages(package_classes_dict)

    first_file_path = directory + 'java/' + packages_list[0][0].replace('.', '/') + '/'
    second_file_path = directory + 'java/' + packages_list[1][0].replace('.', '/') + '/'
    first_file = open(first_file_path, 'r')
    second_file = open(second_file_path, 'w')

    second_file_context = ''
    contexts = ''
    import_sets = set()
    for line in second_file.readlines():
        if 'package ' + packages_list[1][0] + ';' in line:
            second_file_context += line
        elif 'import' in line:
            import_sets.add(line)
        else:
            contexts += line

    for line in first_file.readlines():
        if 'package ' + packages_list[0][0] + ';' in line:
            continue
        elif 'import' in line:
            import_sets.add(line)
        else:
            contexts += line

    for x in import_sets:
        second_file_context += x

    second_file_context += contexts
    second_file.write(second_file_context)

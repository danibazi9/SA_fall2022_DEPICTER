from statistics import mean


def get_uniq_packages(package_classes_dict):
    first_appear = False
    package_classes_list = list(package_classes_dict.keys())
    chosen_package_list = []
    temp = [package_classes_list[0], package_classes_dict[package_classes_list[0]]]
    for first_package in package_classes_list:
        for second_package in package_classes_list:
            if first_package in second_package and first_package != second_package:
                first_appear = True
                if len(first_package) <= len(temp[0]):
                    temp = [first_package, package_classes_dict[first_package]]

            if not (first_package in second_package) and first_package != second_package:
                if len(second_package) <= len(first_package):
                    chosen_package_list.append([second_package, package_classes_dict[second_package]])
                    first_appear=True
        if not first_appear:
            first_appear=False
            chosen_package_list.append([first_package, package_classes_dict[first_package]])
    chosen_package_list.append(temp)

    return chosen_package_list


def calculate_avg(package_classes_dict):

    chosen_package_list = get_uniq_packages(package_classes_dict)
    return mean([item[1] for item in chosen_package_list])


def choose_packages(package_classes_dict):

    chosen_list = []

    chosen_package_list = get_uniq_packages(package_classes_dict)
    for item in chosen_package_list:
        if item[1] < average:
            chosen_list.append(item)

    for key, value in package_classes_dict.items():
        if value < average:
            chosen_list.append([key, value])

    return chosen_list[:2]




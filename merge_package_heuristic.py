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



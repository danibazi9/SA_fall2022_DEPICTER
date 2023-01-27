#from player import Player
import numpy as np
import random
import copy


class Evolution():

    def __init__(self, mode):
        self.mode = mode
        self.param = {}

    # calculate fitness of players
    def calculate_fitness(self, players, delta_xs):
        for i, p in enumerate(players):
            p.fitness = delta_xs[i]

            self.param[i] = p.fitness




    def mutate(self, child):

        # TODO
        # child: an object of class `Player`
        # Mutation changes a single gene in each offspring randomly.
        random_value = np.random.uniform(-1.0, 1.0, size=(child.nn.layers[1],child.nn.layers[0]))
        child.nn.params["W1"] = child.nn.params["W1"] + random_value

        random_value = np.random.uniform(-1.0, 1.0, size=(child.nn.layers[1],1))
        child.nn.params["b1"] = child.nn.params["b1"] + random_value

        random_value = np.random.uniform(-1.0, 1.0, size=(child.nn.layers[2], child.nn.layers[1]))
        child.nn.params["W2"] = child.nn.params["W2"] + random_value

        return child

    def generate_new_population(self, num_players, prev_players=None):

        new_players = []
        # in first generation, we create random players
        if prev_players is None:
            return [Player(self.mode) for _ in range(num_players)]

        else:

            # TODO
            # num_players example: 150
            # prev_players: an array of Player objects

            # TODO (additional): a selection method other than fitness proportionate
            # TODO (additional): implementing crossover

            for i in range(len(prev_players)):
                offspring_size = copy.deepcopy(prev_players[i])
                if i != len(prev_players) - 1:
                    #crossover
                    new_player_crossover = crossover(prev_players[i],prev_players[i+1],offspring_size)
                    #mutation
                    new_players_mutation = self.mutate(new_player_crossover)

                else:
                    #crossover
                    new_player_crossover = crossover(prev_players[i],prev_players[0],offspring_size)
                    #mutation
                    new_players_mutation = self.mutate(new_player_crossover)

                new_players.append(new_players_mutation)

        return new_players[:num_players]



    def next_population_selection(self, players, num_players):

        # TODO
        # num_players example: 100
        # players: an array of `Player` objects

        new_players = copy.deepcopy(players)
        new_players.sort(reverse=True,key=lambda x:x.fitness)

        total_fitness = sum([player.fitness for player in new_players])

        player_probabilities = [player.fitness / total_fitness for player in new_players]

        result = np.random.choice(new_players,num_players,replace=False,p=player_probabilities).tolist()
        # TODO (additional): a selection method other than `top-k`
        # TODO (additional): plotting

        return result



def crossover(parent1,parent2,offspring_size):

    child = offspring_size

    for k in range(child.nn.layers[1]):

        #input_weight
        parent1_idx = np.array_split(parent1.nn.params["W1"][k],2)[0]
        parent2_idx = np.array_split(parent2.nn.params["W1"][k],2)[1]
        child.nn.params["W1"][k] = np.concatenate((parent1_idx,parent2_idx))

    for j in range(child.nn.layers[1]):
        #input_bias
        parent1_idx = np.array_split(parent1.nn.params["b1"][j], 2)[0]
        parent2_idx = np.array_split(parent2.nn.params["b1"][j], 2)[1]
        child.nn.params["b1"][j] = np.concatenate((parent1_idx, parent2_idx))

    #hidden_weight
    parent1_idx = np.array_split(parent1.nn.params["W2"], 2)[0]
    parent2_idx = np.array_split(parent2.nn.params["W2"], 2)[1]
    child.nn.params["W2"] = np.concatenate((parent1_idx, parent2_idx))


    return child





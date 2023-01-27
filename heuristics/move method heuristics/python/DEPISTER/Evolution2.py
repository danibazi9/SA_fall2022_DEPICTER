from deap import algorithms, base, tools
from Evolution import *

# Define the toolbox
toolbox = base.Toolbox()
toolbox.register("evaluate", Evolution)
toolbox.register("mate", tools.cxTwoPoint)
toolbox.register("mutate", tools.mutGaussian, mu=0, sigma=1, indpb=0.1)
toolbox.register("select", tools.selNSGA2)

# Create an initial population
population = toolbox.population(n=200)

# Run the algorithm
result = algorithms.eaMuPlusLambda(population, toolbox, mu=200, lambda_=200, cxpb=0.5, mutpb=0.2, ngen=100)
# SA_G2_DEPICTER

**"Software Architecture" course project, Dr. Mehrdad Ashtiani, Fall 2022**

School of Computer Engineering, IUST, Tehran, Iran

In this project, we tried to implement one of the top papers in Software Automated Refactoring, which named **`DEPICTER`**, a **DE**sign-**P**r**I**n**C**iple guided and heuris**T**ic-rul**E** constrained software **R**efactoring recommendation approach.

Project contributors:

+ Danial Bazmandeh, BSc, Computer Engineering
+ Parisa Alaei, MSc, Software Engineering
+ Fatemeh Ranjbar, MSc, Software Engineering

You can access the paper using [this link](https://github.com/Parisa78/AS_G2_Depicter/blob/main/2022_DEPICTER_A_Design-Principle_Guided_and_Heuristic-Rule_Constrained_Software_Refactoring_Approach.pdf).

You can also access the final project report via these links: [English](https://github.com/danibazi9/SA_fall2022_DEPICTER/blob/main/SA_G2_DEPICTER_Report_ENG.pdf) | [Persian](https://github.com/danibazi9/SA_fall2022_DEPICTER/blob/main/SA_G2_DEPICTER_Report.pdf)

## Abstract

As we know, the more suitable design patterns a software uses, the higher its scalability, readability and maintainability. The quality of software design is affected by the software evolution cycle. This may be due to the addition of new features or possible future bugs. This made the developers turn to **code refactoring**.

Software Refactoring means improving the internal structure and architecture of the software, without changing its external performance and functions, which leads to improving the maintainability of the software. But the operation of automatic code refactoring is a very challenging task. Because it requires a comprehensive view of the entire software system. For this purpose, recent studies introduced search-based algorithms to facilitate software reconstruction. However, they still have the following major limitations:

1.	Searched solutions may violate the design principles, because their Fitness Functions do not directly reflect and measure the degree of compliance of the software with the design principles.

2.	Most approaches start the search process from a completely random initial population, which may lead to suboptimal solutions.

In this paper, we aim to develop an effective search-based refactoring approach to recommend better refactoring activities for developers, which can improve the degree of software conformance to design principles as well as the quality of software design.

In this research, we investigated and implemented `DEPICTER`. To develop and increase the population, we have used the genetic algorithm `NSGA-II`, which uses metrics and design patterns as fitness functions. In addition, `DEPICTER` uses heuristic rules to improve the quality of the initial population for subsequent general evolutions.

Therefore, the main parts of this project, which have been implemented, include the following three parts:

1.	Implementation of genetic algorithm `NSGA-II`

2.	Implementation of the heuristic functions

3.	Implementation of automated refactoring operation

We used Java language to implement the genetic algorithm. For this purpose, we defined the initial population and population evolution using Fitness Functions. It should be mentioned that because the implementation of fitness functions for our paper was a very difficult process (because we had to find a numerical measure to calculate the amount of dependence between classes and methods) and in coordination with the professor, who left us open for implementation, we used the fit functions for another paper that used this algorithm similar to our paper.

For the heuristic functions part, with the confirmation we got from the relevant TA, we decided to only calculate the functions and there was no need to consider pre-conditions or post-conditions. For this purpose, we implemented three heuristic functions for each refactoring of `Merge Package` and `Move Method` functions.

In the implementation of automated refactoring, we used the widely used `ANTLR` library in Python and did our work by inheriting the reference Listener class for the Java language grammar. Then we used its walker to parse the code and when entering or leaving the rules that we needed, we did the refactoring process and wrote and replaced the tokens using `token_stream_rewriter`.

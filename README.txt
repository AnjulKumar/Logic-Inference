Anjul Kumar
CS561 - Homework #3
----------------------------------------------------
File submitted:
1)agent.java
2)Makefile
----------------------------------------------------
Assumptions:
The agent doesn't work on infinite loops.
---------------------------------------------------

Problem 
Given a knowledge base and a query sentence, the program determines if the query can be inferred from the information given in the knowledge base. The algorithm used to solve this problem is backward-chaining.


Input:
The knowledge base and the query in a text file called input.txt.
The first line of the input file contains the query. The second line contains an integer n specifying the number of clauses in the knowledge base. The remaining lines contain the clauses in the
knowledge base, one per line. Each clause is written in one of the following forms:
1) as an implication of the form p1 ∧ p2 ∧ ... ∧ pn ⇒ q, whose premise is a conjunction of
atomic sentences and whose conclusion is a single atomic sentence.
2) as a fact with a single atomic sentence: q
Each atomic sentence is a predicate applied to a certain number of arguments. Note that negation is not used.


Output:
If the query sentence can be inferred from the knowledge base, the output is TRUE, otherwise, FALSE. The answer (TRUE/FALSE) is made available in the file output.txt.

# heartbeat-tactic-detection

Example code to experiment heartbeat detection algorithm. This  code is just to experiment and explain the how heatbeat algorithm can be realized. 

A. Prerequisite : 

1. AProVE tool is used to find all non terminating components (infinite loops) in the code (line 4 in algorithm 1).

2. To parse Java program and collect all nodes such as class, declared methods, invoked methods and declared variables Java AST parser (supported by Eclipse JDT plugin) is used. After the above components are identified, we have analyzed the static structure among parsed AST nodes.


B. Main Class : HBTAlgorithm.java

C. TestCases :
						
															
A1 = 	      heartbeat,										
A2 =	      heartbeat_testcase_pacemaker,		
A3 =	      heartbeat_testcase4,					
A4 =	      heartbeat_testcase_pacemaker_1,	
A5 =	      heartbeat_testcase3,						
A6 =	      heartbeat_testcase_pacemaker_2 .	







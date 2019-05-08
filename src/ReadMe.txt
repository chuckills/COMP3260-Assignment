COMP3260 Assignment 2 ReadMe.txt
Gregory Choice(c9311718) & Christopher Booth(c3229932)

=========================================================================================================
Class Descriptions
=========================================================================================================

Application.java	- The main runnable class, mostly responsible for I/O operations

Round.java			- Contains the operations needed to complete a single round of encoding or decoding

SBox.java 			- Contains two static HashMaps for table lookup of SBox and Inverse SBox

AES.java 			- Performs the required sequences of round transformations according to the 
					  specification and performs comparison of each version

AES0.java 			- Derived classes from AES.java with differing encoding algorithms
AES1.java
AES2.java
AES3.java
AES4.java
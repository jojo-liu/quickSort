# Multithread QuickSort

Here are the priorities of attributes in Car information:
For Car.destination: New York > Maim > Huston > Los Angeles
For Car.color: Blue > Green > Red
For Car.serialNumber: with lexicographical order

The maximum size of thread is N = 100 and the threshold S, that is when the size of sequence is larger than S the sequence needs to be partitioned, is set to be 3_0000. The whole amount of car information item is 20_0000.

The program contains several components which needs to be clarified:
1.	The system contains the file read/write operation, including permission control, BufferedReader close, BufferedWriter close.
2.	I recorded all the time consumed by different operation, such as writing randomly generated car information into file, reading file into the system, sorting all car information with multithread quicksort, sorting all car information with multithread quicksort based on Fork/Join framework, writing sorted car information into file, checking if sorted car information is sorted or not.
3.	I also compared the time consumed for two different methods: normal multithread quicksort (sort() function in QuickSort class) and multithread quicksort based on Fork/Join framework (forkJoinSort() function in QuickSort class). With the same threshold, the performance of forkJoinSort() is better than sort(). To be clarified, Fork/Join framework has the advantage where its Work-stealing algorithm can leverage thread to process parallel computing and reduce the competition of threads. 
4.	The system is user-friendly and it can inform user what current component is being executed.
5.	The explanation of each file in the system: 

    1>	Car class including the definition of car information

    2>	QuickSort including the file operation, sorting function, and other main components.

    3>	SortByCar is a comparator which allows the system to compare the relationship between two cars

    4>	SortTask class which extends RecursiveAction is set to implement the function of Fork/Join framework. 

    5>	./carFile/ includes the file or unsorted and sorted car information. 

Considering the real production environment, I set the serial number of the car to be String type for the following reason: String is efficient to be compared with lexicographical order and easy to write into and read from file. However, it is fine if we set it to be Integer and the we only need to transfer it into String. I combined all attributes of the Car into a String and compared its relationship with lexicographical order, then I got the sequence of sorted car information via multithread quicksort. 

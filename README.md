# compressionApp
In this project, we built an Android application named HCoder and a server for the transmission of compressed text files. Huffman coding is used as the compression algorithm. Android application is coded using Java and Nodejs is used at the server side. Our Nodjs server is deployed on Heroku.

Our application uses simple web service architecture for communicating between devices and between device and server. During both sending and receiving file operations, whether it be between mobile device and mobile device or mobile device and server, file is sent to server first by an HTTP POST request then sent to receiving user from the server via HTTP GET request. 

HCoder Server, allow’s user to zip/unzip files using Huffman Coding. Zipped files are stored as byte array and frequency map related to that one specific file. HCoder Server also allows users to communicate with each other , as in sending and receiving files, by responding to related HTTP requests. While logged in as admin, user can restart the server by deleting all registered users and files sent to them. While receiving a file from the mobile devices, if the sent file is compressed; server unzips the file and saves the both version of the file and vice versa.

At the android device the users and files are sent to server by HTTP POST requests and get back from the server using HTTP GET request. An activity is created when application is started, all screens are fragments, there is only one activity.

Huffman Coding Algorithm is a kind of text based compression algorithm that has the complexity of O(n*logn) [Cornen 2009]. That algorithm uses tree including binary numbered elements holding the characters of a string and heap functions. Huffman coding algorithm is different than the other compression algorithms on account of those characteristics. Furthermore, Huffman Coding algorithm generally causes no loss of data [Cornen 2009]. When using Huffman Coding, each character in a 30-character alphabet can be encoded with average 6 bits; even though, character size is 16 bits in java.

Git url of server code: https://github.com/GunesYurdakul/compressionApp.git

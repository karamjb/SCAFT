# Secure Chat And File Transfer - SCAFT
![Secure Chat And File Transfer](/TestSpace/chat.png)
## Communication and Information Security - Assignment 1: SCAFT

## Students:
 Karam Jabareen - 204495923
 Adam Sagas - 311483630


## Roles:

* Karam Jabareen – Building and designing the interface, building the transference of messages and files and working on the encryption and decryption methods.(Worked for 70 hours)

* Adam Sagas – Assisting in building the transference, encryption and decryption algorithms and testing the program.(Worked for 30 hours)





## Instructions for compiling the program:
Read the file `HowToRunTest.txt` for instructions.

## Instructions for running the program:
In order to run the program, you must execute the command:
`java -jar SCAFTCore.jar {ui port} {neighbors text file path} `

for example:
`java -jar SCAFTCore.jar 8080 C:\Users\TOSHIBA\Desktop\cis\hw1\neighbors5000.txt`

If you don’t insert any port or choose any file, the program will use the following default parameters:
*	Default neighbor file path: `C://SCAFT/neighbors.txt`.
*	Default ui port: `8080` (this is the connection between the SCAFTUi And SCFATCore).
*	Default SCAFT port listener: `5000`.

Note: After executing the command, the program will start. In the program, you must insert the same ui port as the command before joining the chat.
After inserting all the parameters, you can click on the `Join` button to join the chat.
 

The format of the neighbor file should contain each user’s IP, port and password, for example:
 




## Explanation of how the program works:

All messages are in JSON format. Every message has a `message type` property alongside other different properties.
The different types of messages:
*	`startListen` – When the SCAFTCore receives this type of message, it starts to listen to it. It contains the user’s name and port.
*	`getUsersList` – When the SCAFTCore receives this type of message, it returns all users with each one’s status.
*	`Hello` – SCAFTCore sends this type of message every 30 seconds. It informs the other users that the sender is connected.
*	`txtMessage` – This type of message contains the plain text and to whom the message is sent to (receiver’s information). If the `to` property equals `*` the message is sent to everyone, else if it equals to a specific user, then it is sent to him personality.
*	`fileRequest` – Sends a request to a specific user if he agrees to accept the upcoming file. If the receiver accepts it will answer with a ``OK`` message else it will answer with a ``Fail`` message.
*	`fileMSG` – This type of message contains the name and data of the file. The maximum size of file to be sent is 20 megabytes.
*	`File` – The SCAFTCore sends this type of message to itself when the transference of the file is completed. When receiving this message, the program opens the folder containing the file.

Before the program sends any kind of message, it creates a message that contains the sender’s listener port, the IV and the encrypted message.

The program takes the password, hashes it to 256 bits by `SHA2-256` and uses it as a key to encrypt the message. Every message is sent with a new generated IV.

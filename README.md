# Java Programming Interview

This is a java take-home programming assignment. Its purpose is to test your ability to manipulate a concurrent Java program. The base version of this assignment is a basic java echo server, where `Client.java` connects to `Server.java`, accepts text strings from console input, transmits them across the socket to the server, and has them echoed back exactly as sent. Your goal is to convert this echo server into a chatroom such that each connected client sees messages sent by all other clients. 

The project was tested with Intellij Idea with Java 11 on Ubuntu 18.04. 

## Tasks

You are not meant to solve all the tasks listed here-- they increase in difficulty in order to provide a challenge for Java programmers of all skill levels. You may use whatever libraries, programming patterns, or resources to solve these tasks. 

Do not modify the client in any task, but do read it over and try to understand it-- the code there will be helpful in solving some of these tasks. 

### Task 1: Chatroom

You can connect more than one client to the echoserver at once, but each client will only hear back their own echo. Convert the echo server to a chatroom, so two seperate terminals connected as clients can send messages back and forth. When you connect two clients to a single server and type into one of the clients, you should see your message in the other client. 

### Task 2: Timestamps

Add timestamps on messages from other users that show the number of seconds that have elapsed since *that user* connected. 

For example, when one client connects, then three seconds later sends a message `pizza later?`, waits 3 seconds, then `or maybe pasta` the other client should see:

```
< 3 pizza later? 
< 6 or maybe pasta?
```

The `3` indicates the first message arrived 3 seconds after that client connected. 

### Task 3: Pagers

Add a feature in the server to page users to get their attention. If any client sends the text string `page` to the server, the server should send `PAGING` to every connected client once a second until *another* client types anything-- not the original client. The client who sent the page must still be able to send normal chat messages while a page is active, and they should not see the `PAGING` alert printed in their chat window. 

### Task 4: Private Chat


Add a private chat feature that lets two users enter a private chat in such a way that they only see each other's messages, and no other connected client can see their messages. The "name" associated with each user should be some number that doesn't conflict with any other user's ID and stays fixed while the user is connected. If that client disconnects and reconnects, they should get a new ID thats never been used. Then create a command `invite <name>` in the server that will move another user to a private chat and tell them who they're chatting with. Users should be able to leave a chat with `leave`. If either user disconnects from the server, the other user should be dropped back into the regular chatroom. 

For example, if client 1 connects before client 2, client 1 should see:

```
< Client 2 connected
```

Then if client 1 types `invite 1` into the chat, client 2 should see:

```
Private chatting with Client 1
```

and client 1 should see:

```
Private chatting with Client 2
```

If client 2 types `leave`, client 1 should see:

```
Private chat ended.
```

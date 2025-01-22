# LAN Chat

## Overview

Chat Server is a simple LAN chatroom that anyone can use. The GUI texting
application comes with a server backend that must be run prior launching the
former.

## Instructions

To use the chatroom, 
[download](https://github.com/KevinZhang2135/Chat-Server/releases) the latest 
version of the application first run server on a specified port and navigate to
the location of the unzipped folder in the terminal.

Upon running the server using `java Server {port number}` in the terminal, the
server will output the host address where it is running on. Using the address 
and port of the server, you can connect users to the server by typing 
`java Client {username} {host address} {port number}`.

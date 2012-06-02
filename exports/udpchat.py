#!/usr/bin/env python3
import sys

if sys.version < '3':
    print("Sorry, this program requires Python 3.x to run")
    sys.exit(-1)

import threading
import socket
import struct

GROUP_ADDR = "224.0.0.100"
GROUP_PORT = 25000
BUFSIZE    = 1024

class Server(threading.Thread):
    def message_arrive(self, msg):
        print("Srv > ", msg)

    def run(self):
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        sock.bind(('', GROUP_PORT))
        
        # Join group
        group_bin = socket.inet_pton(socket.AF_INET, GROUP_ADDR)
        mreq = group_bin + struct.pack("=I", socket.INADDR_ANY)
        sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

        while True:
            data, sender = sock.recvfrom(BUFSIZE)
            data = data.rstrip(b'\x00').decode()
            self.message_arrive(data)

class Client:
    def __init__(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        ttl_bin = struct.pack('@i', 1)
        self.sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl_bin)

    def send(self, msg):
        data = msg.encode()
        self.sock.sendto(data, (GROUP_ADDR, GROUP_PORT))

c = Client()
s = Server()
s.start()
while True:
    line = input()
    if line == "":
        break
    c.send(line)


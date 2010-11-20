# Copyright 2010 Christian Zommerfelds
# 
# This file is part of RemoteControl for Android.
# 
# RemoteControl for Android is free software: you can redistribute it
# and/or modify it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the License,
# or (at your option) any later version.
#
# RemoteControl for Android is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with RemoteControl for Android.
# If not, see <http://www.gnu.org/licenses/>.

import socket

class ButtonDefinition:
    def __init__(self):
        self.__buttons = {}
        self.__curIndex = 0
        
    def addButton(self, name, func):
        if name == '':
            print 'Error, name = ""'
            return
        self.__buttons[self.__curIndex] = (name, func)
        self.__curIndex += 1
        
    def runFunc(self, num):
        if num in self.__buttons:
            self.__buttons[num][1]()
            
    def getName(self, num):
        if num in self.__buttons:
            return self.__buttons[num][0]
        return ''

class Server:
    def __init__(self, butDef):
        self.__butDef = butDef
        
    def run(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind(('', self.__PORT))
        s.listen(1)
        while True:
            #print 'Waiting for connection, port:', self.__PORT
            conn, addr = s.accept()
            #print 'Connected with', addr
            #print 'Sending button labels'
            
            i = 0
            while True:
                name = self.__butDef.getName(i)
                if name == '':
                    break
                conn.sendall(name+'\n')
                i += 1
            conn.sendall('\n')
            while 1:
                data = conn.recv(self.__PACKET_SIZE)
                if not data: break
                but = self.__butDef.getName(ord(data))
                #print 'Received '+str(ord(data))+':"'+str(but)+'"'
                self.__butDef.runFunc(ord(data))
            conn.close()
        
    __PORT = 57891
    __PACKET_SIZE = 1

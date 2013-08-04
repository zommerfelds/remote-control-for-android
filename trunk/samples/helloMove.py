#! /usr/bin/env python
# coding: utf-8

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

import RemoteControlServer as rcs
import sys
import random

RANGE = 80
p = RANGE//2
s = 'X'

random.seed()

def draw():
    sys.stdout.write('|')
    for i in range(p):
        sys.stdout.write(' ')
    sys.stdout.write(s)
    for i in range(RANGE-p):
        sys.stdout.write(' ')
    sys.stdout.write('|\n')
    sys.stdout.flush()
def right():
    global p
    p += 1
    p=min(p,RANGE)
    draw()
def left():
    global p
    p -= 1
    p=max(p,0)
    draw()
def mutate():
    global s
    s = random.choice(['X','T','?','!','.','&','=','-',':','@',
                       '%','+','^','*','~','$','#','§','0','8'])
    draw()

butDef = rcs.ButtonsDefinition()
butDef.addButton("Left",left)
butDef.addButton("Right",right)
butDef.addButton("Mutate",mutate)

server = rcs.Server(butDef)

draw()

server.run()

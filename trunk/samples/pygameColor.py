#! /usr/bin/env python

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

import pygame
import threading
import sys
import time

import RemoteControlServer as rcs

color = (0,0,0)

def red():
    global color
    color = (255,0,0)
def green():
    global color
    color = (0,255,0)
def blue():
    global color
    color = (0,0,255)
def purple():
    global color
    color = (255,0,255)
def cyan():
    global color
    color = (0,255,255)
def yellow():
    global color
    color = (255,255,0)

def run_pygame():
    screen = pygame.display.set_mode((640, 400))
    running = 1

    while running:
        event = pygame.event.poll()
        if event.type == pygame.QUIT:
            running = 0
        screen.fill(color)
        pygame.display.flip()
        time.sleep(0.1)
        
    pygame.display.quit()
        
def run_server():
    butDef = rcs.ButtonDefinition()
    butDef.addButton("Red",red)
    butDef.addButton("Green",green)
    butDef.addButton("Blue",blue)
    butDef.addButton("Yellow",yellow)
    butDef.addButton("Cyan",cyan)
    butDef.addButton("Purple",purple)

    server = rcs.Server(butDef)
    server.run()

thread = threading.Thread(target=run_server)
thread.setDaemon(True)
thread.start()

run_pygame()


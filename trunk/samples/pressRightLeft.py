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


# Simulates pressing the right or left arrow keys
# Can be used for controlling a presentation or slide show
# needs pyatspi

import pyatspi
import functools
import RemoteControlServer as rcs

def pressKey(keycode):
   """Takes an integer keycode. You can find these with xev."""
   pyatspi.Registry.generateKeyboardEvent(keycode, None,
                                          pyatspi.KEY_PRESSRELEASE)

butDef = rcs.ButtonsDefinition()
butDef.addButton("Left",functools.partial(pressKey,113))
butDef.addButton("Right",functools.partial(pressKey,114))

server = rcs.Server(butDef)
server.run()

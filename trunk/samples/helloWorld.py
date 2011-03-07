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
# If not, see <http://www.gnu.org/licenses/>.import RemoteControlServer as rcs

import RemoteControlServer as rcs

def onButton1Press():
    print 'User pressed button 1 - Hello'
def onButton2Press():
    print 'User pressed button 2 - World'

butDef = rcs.ButtonDefinition()
butDef.addButton("Hello", onButton1Press)
butDef.addButton("World", onButton2Press)

server = rcs.Server(butDef)
server.run()

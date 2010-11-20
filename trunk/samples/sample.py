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

def f1():
    print 'running f1'
def f2():
    print 'running f2'

butDef = rcs.ButtonDefinition()
butDef.addButton("MyButton1",f1)
butDef.addButton("MyButton2",f2)

server = rcs.Server(butDef)
server.run()

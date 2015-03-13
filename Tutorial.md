This small tutorial explains how to setup a basic remote control system and how to write a simple server.

You need to have basic knowledge on Python and know how to execute commands on your operating system

System requirements:
  * A computer where Python 3 is installed
  * An Android phone
  * Phone has connection over IP to the server (port 57891)<br />

## Download Remote Control for Android ##

First, download the remote-control-for-android source. At the moment there is no package to download in the Downloads section. You'll have to use the svn source repository.

  * Install Subversion if you haven't installed it yet.

  * Download the latest revision:

```
svn checkout http://remote-control-for-android.googlecode.com/svn/trunk/ RemoteControlForAndroid
```


## Install client on the phone ##

Next step is to install the client app on your Android phone. First you will have to get the .apk file. You can build the APK file using Eclipse.
  * Connect your phone with the USB cable (or any other connection type where you can access the SD card)

  * Copy the file **`RemoteControlClient/bin/RemoteControlClient.apk`** over to the SD card

  * For browsing the SD card, install an app from the Android Market like "ES File Explorer" or "ASTRO"

  * Open the installed file browser app and search for the .apk file. Click it and install


## Setup the server ##

  * Create a folder on your PC where you want the server project to be stored.

  * Copy the file **`RemoteControlServer/RemoteControlServer.py`** to this folder

  * Create a Python text file named **`myserver.py`**

  * Put this code into the file:
```
import RemoteControlServer as RCS

# define the callback function
def sayHello():
    print "Hello! It works!"

# create the button definition
# this is what will be displayed on the phone when it connects to the server
butDef = RCS.ButtonsDefinition()
butDef.addButton("Hello", sayHello)

# create server object
server = RCS.Server(butDef)

# run the server (blocks)
print 'Running server'
server.run()
```
All this code does is start the server and tell it to run sayHello when the person on the phone presses "Hello".

Now run the server with the command **`python myserver.py`**


## Connect your phone to the server ##

  * First, find out the IP address of your computer (run **`ifconfig`** on GNU/Linux or **`ipconfig`** on Windows).

  * Now open the client app on the phone.

  * You should see a screen asking you to enter an IP address.

  * Enter the IP address of the server.

  * If everything goes well, you should see now a screen with one button labeled "Hello"
<br />
That should be everything you need to setup the hello world server.

Inside the callback functions you can execute now anything you want. To run a command use this:
```
import subprocess

def myFunc():
    subprocess.call(['my_prog','my_arg1','my_arg2'])
```

If you encounter any problems, feel free to ask a question in http://groups.google.com/group/remote-control-for-android
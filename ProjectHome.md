**Remote Control for Android** is a small experimental client app and a server library that allows the user to easily create a simple system where you can trigger actions from your Android phone via TCP/IP.

This project consists of two parts:
  * The Android Remote Control server library (Python 3)
  * The Android Remote Control client app (Java)

To use the library you need to:
  1. Install the app on your Android phone
  1. Create your server application using the library

The app will receive the GUI data from the server and send back an event to the server when a button is pressed by the user.

See [Tutorial](Tutorial.md) for more information and a simple hello world application.

Current limitations:
  * Only simple push buttons are supported (no toggle buttons, sliders, etc)
  * Security: no encryption, no authentication (what means that anybody who has access to the server can connect to it and trigger actions)

Screenshots of the app:

![http://remote-control-for-android.googlecode.com/svn/wiki/screen2.png](http://remote-control-for-android.googlecode.com/svn/wiki/screen2.png) ![http://remote-control-for-android.googlecode.com/svn/wiki/screen1.png](http://remote-control-for-android.googlecode.com/svn/wiki/screen1.png)

Left: connection screen, Right: example of a server allowing to control a media player
## Meditronix 2.0
Meditronix 2.0 is an **updated GUI version of Meditronix** that was written in C with a terminal based interface.
Meditronix 2.0 builds up on its predecessor with robust data handling done using MySQL database instead of simple file handling .
The software is build using JavaFX & MySQL database & drivers.
All relevant project info and software structure can be found in the project documentation.

---
## How to launch the application
Intellij project file structure was followed. We recommend using intellij IDE to launch the app

 - To enable database, you can go to database java class file and change the connection credentials in `dbConnect() ` method.
 - We recommend using MySQL for database handling since all sql statements were written with respect to  MySQL  syntax.
 - Login info is stored in the `users` table.
 - Currently no install wizard has been employed.
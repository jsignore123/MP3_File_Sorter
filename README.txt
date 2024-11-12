README


Description: 
-------------------
This program will sort all your MP3 song files into their respective band and album folders. It also prints the song/album/band for each MP3 file to the terminal.

library used if .jar file doesn't work: https://github.com/mpatric/mp3agic

Instructions:
-------------------
 -update Exif data to ID3v2 or ID3v1

 -Open command prompt 
   search cmd in search bar and run as administrator

 -Install JDK (compiler) 
   install to default directory in installer - C:\ProgramFiles\Java\jdk-17
   JDK download: 
   https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe

 -Set path for JDK in command prompt:
   Enter these commands into command prompt:
    setx -m JAVA_HOME "C:\Program Files\Java\jdk-17"
    setx PATH "%PATH%;%JAVA_HOME%\bin";

 -Navigate to mp3_sort folder in command prompt:
   cd ..             move to parent directory
   cd folder         move to folder in current directory 
   cd name\name      go down two levels of documents at once. For example: cd Admin\Downloads
   dir               displays contents of current directory

 -COMPILE
   You only need to do this ONCE to create .CLASS files for run
   
   Compile Command:
     javac -classpath .;mp3agic-0.9.1.jar mp3Sorter.java Main.java

 -RUN
   Run command:
    java -classpath .;mp3agic-0.9.1.jar Main "INSERT DIRECTORY FOR MP3 FOLDER IN QUOTES"


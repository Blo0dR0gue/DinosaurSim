[//]: # (Author Daniel Czeschner)

# TheSim

This project is created as part of the _'Software Engineering'_ lecture in summer semester 2022 in the Applied Computer
Science course at DHBW Mannheim. The aim is to develop a tool that allows the user to code information into images
using steganography and cryptography.

---

## Requirements

The final software product is compiled as a JAR archive with
[Java version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). The corresponding Java
version (JRE) is therefore required to run the software on all supported platforms.

Once the JAR archive has been downloaded, __no__ installation of the application is necessary.

## Start Application

From discussions with the client, it appears that the finished application will NOT be installed on the user's systems
by the contractor. As a gesture of goodwill, the contractor is willing to help in case of problems with the start of the
software. Following are some ways that can lead to the successful launch of the software:

- **If the `PATH` variable was filled correctly during the installation of Java, the application can be started with a
  _double click_ on the file.**
- Run JAR via `Rigth CLick > Open with > Select correct JRE`
- If none of the above methods work, which unfortunately can happen especially if several Java versions are installed on
  the system, it is recommended to start the application via the console (under Windows):
    1. [open console](https://www.howtogeek.com/235101/10-ways-to-open-the-command-prompt-in-windows-10/)
       (e.g. `Win + R > enter "cmd" > OK`)
    2. enter the following command:
       ```
       C:\PATH\TO\JRE\bin\java.exe -jar C:\PATH\TO\JAR\TheSim.jar
       ```

## Possible Errors

- If the OS is trying to open the JAR file with the wrong Java Version, errors like this can occur:

  ![grafik](https://user-images.githubusercontent.com/35914049/145673566-65f11bf2-6d52-4e5f-b6af-0a9e1f2e1ef6.png)

  **Possible Solution:** modify PATH variable to point to the correct
  version ([see](https://www.java.com/en/download/help/path.html)) or use another way to start the application.


## Contributors

- Kai Grübener
- Tamina Mühlenberg
- Robin Khatri Chetri
- Eric Stefan
- Lucas Schaffer
- Daniel Czeschner
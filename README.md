# MamePlaylistBuilder

Just a little program to build a MAME ROM playlist for the RetroArch frontend.

## How to

1. [Download the latest release](https://github.com/D4Delta/MamePlaylistBuilder/releases/download/v1.0/MamePlaylistBuilder-1.0-SNAPSHOT.jar)

2. Install Java 8 if you don't have it already

3. If you're on linux, add the executable bit to the jar or you won't be able to launch it (using the file explorer or `chmod +x MamePlaylistBuilder-1.1-SNAPSHOT.jar`)

4. Launch MamePlaylistBuilder (if you can't do it with your file explorer, use `java -jar MamePlaylistBuilder-1.0-SNAPSHOT.jar`)

5. MamePlaylistBuilder will ask for the location of your MAME Roms folder. Select it, and confirm.

6. A "MAME.lpl" should have been created; Copy it.

7. If you're on linux, paste this file to `~/.config/retroarch/playlists`. If you're on Windows, the `playlists` folder is located in the same folder as retroarch.exe

8. Enjoy your (hopefully full) new playlist. 


# DesClientSider (DCS) (API em desenvolvimento)

DesClientSider (or DCS) is a Java tool designed to efficiently detect potential client-side mods, automatically disabling those that are not compatible with servers.

DCS compares mod file names with a list of keywords and moves corresponding files to a designated folder, effectively disabling them.

Check out the DCS API project!
[DesClientSider-API: Simple API for DCS in GoLang](https://github.com/Paique/DesClientSider-API)

## Features

- **Automatic Mod Disabling**: DCS detects client-side mods incompatible with specific servers and automatically disables them, moving them to a separate folder.
- **SFTP Compatibility**: Users have the option to connect to the target server using SFTP to facilitate remote mod management.
- **Local Usage**: DCS can also be used locally, allowing users to manually set the path to the mods folder.

## How to Use

**Locally**: If used locally, simply run the DCS JAR file and provide the path to the mods folder when prompted.
![](https://i.imgur.com/rHdsAV8.png)

**SFTP**: To use SFTP, provide the connection credentials.
![](https://i.imgur.com/wv39V3u.png)
Additionally, it's possible to use the password by setting the PASSWORD arg in the console:
![](https://i.imgur.com/HFP8QfK.png)

**JavaAgent**: Can be executed as a JavaAgent along with the server to remove mods before the server starts.

## System Requirements

- Java 17 or higher.
- Internet connection for API requests.

## Installation

1. Download the DCS JAR file from the tags.
2. Have Java installed.
3. Execute the DCS JAR file.

## Usage Examples

`java -jar DesClientSider-1.2.2.jar`

`java -javaagent:DesClientSider-1.2.2.jar -jar server.jar --nogui`

After selecting the desired options, DCS will analyze the mod files, disable those matching the specified keywords, and move them to the newly created "client" folder.

## Contribution

Contributions are welcome! If you find bugs or have suggestions for improvements, feel free to open an issue or send a pull request to the official DCS repository.

## License

This project is licensed under the MIT License.

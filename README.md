# Daedalus
Daedalus Jury-software written in Java

## Download
```shell script
git clone https://github.com/CodecupNL/Daedalus
```
Note: *Git should be installed.*

## Install
```shell script
mvn install
```
Note: *Maven should be installed.*

## Run
```shell script
cd bin
deadalus [-d] [-f <file>] -g <game> [-m <manager>]
```
Note: *Java should be installed.*

### Parameters

| Parameter | Description| Required |
| --- | --- | --- |
| `-d` | If set, Daedalus will print debug information. | No |
| `-f <file>` | If set, defines which file to use for logging. If not set, defaults to `bin/games/${game}/logs/${time}.log`. | No |
| `-g <game>` | Defines which game there will be played. | Yes (if no `-h`, `-?` or `-l`) |
| `-h` / `-?` | Show help | No |
| `-l <type>` | List available items of certain type.<br>Supported types:<br><ul><li>`games`</li><li>`managers`</li><li>`players:<game>` (where `<game>` is a game)</li></ul> | No |
| `-m <manager>` | If set, defines which manager will be used for the competition. If not set, it defaults to `bin/managers/manager.jar`. | No |
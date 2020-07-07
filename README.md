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
deadalus [-c <config>] [-d] [-h|-?] [-l <list>] [-m <manager>] [-v] [-w <wrapper>]
```
Note: *Java should be installed.*

### Parameters

| Parameter | Description| Required |
| --- | --- | --- |
| `-c <config>` | Defines which config file to use. | No |
| `-d` | If set, Daedalus will print debug information. | No |
| `-h` / `-?` | Show help | No |
| `-l <list>` | List available items of certain type.<br>Supported types:<br><ul><li>`games`</li><li>`logs`</li><li>`managers`</li><li>`players:<game>` (where `<game>` is a game)</li><li>`referees:<game>` (where `<game>` is a game)</li></ul> | No |
| `-m <manager>` | If set, defines which manager will be used for the competition. If not set, it defaults to `bin/managers/manager.jar`. | No |
| `-v` | Show version. | No |
| `-w <wrapper>` | Defines which wrapper file to use. | No |
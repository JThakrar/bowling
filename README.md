
# Bowling

## Overview
This project is to compute scores and print player info from a file
containing rolls by one or more players. Each line in the file should
contain two fields - the player's name and the number of pins fallen
for that roll separated by a tab -
`PLAYER_NAME<tab>PINFALLS` e.g. "John   8"

Valid pinfalls are numbers 0 through 10 and F (foul).

The file should contain the correct number of rolls for 10 frames.

## Pre-requisites
- Scala 2.11.x
- SBT

## How To Build, Deploy and Run

Download the project and change directory to the project directory.
Assuming that Scala and sbt are in your path, build the project as shown below:

```
sbt universal:packageBin
```


Unpack the artifact from the above as shown below which results in a
directory `bowling-1.0` under universal.

```
cd target/universal

unzip bowling-1.0.zip
```


Test using the sample data files provided.

```
bowling-1.0/bin/bowling bowling-1.0/sample_data/Carl.txt

bowling-1.0/bin/bowling bowling-1.0/sample_data/John_And_Jeff.txt
```

Have fun!
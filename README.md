# Tictactoe

## Prerequisites

This is a maven project, simply run

```shell
mvn verify
```

And import it into your favorite IDE. You should then be able to start the project.
It is a typical spring boot project.

## Playing

First one needs to start a new game:

```http request
curl -vvv localhost:9090/game/create
```
It will return the id of the game we will play.

Then someone has to make a move (replace the variables x, y and id for it to work):

```http request
curl -XPUT localhost:9090/game/play -H "Content-type: application/json" -d '{"coordinate": {"x": ${x}, "y": ${y}}, "gameId": "${id}"}'
```

It will return a response, containing the new board as well as an eventual result.

You can also show an ongoing board:

```http request
curl -XPOST localhost:9090/game/show -H "Content-type: application/json" -d '{"gameId": "${id}"}'
```
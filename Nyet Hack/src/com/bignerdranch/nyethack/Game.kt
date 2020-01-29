package com.bignerdranch.nyethack

import java.lang.Exception
import java.lang.IllegalStateException

object Game {
	private val player = Player("Space")
	private var currentRoom: Room = TownSquare()
	private val worldMap = listOf(
		listOf(currentRoom, Room("Tavern"), Room("Back Room")),
		listOf(Room("Long Corridor"), Room("Generic Room"))
	)

	init {
		println("Welcome, adventurer.")
		player.castFireball()
	}

	private fun move(directionInput: String) =
		try {
			val direction = Direction.valueOf(directionInput.toUpperCase())
			val newPosition = direction.updateCoordinate(player.currentPosition)
			if (!newPosition.isInBounds)
				throw IllegalStateException("$direction is out of bounds.")

			val newRoom = worldMap[newPosition.y][newPosition.x]
			player.currentPosition = newPosition
			currentRoom = newRoom
			"OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
		} catch (e: Exception) {
			"Invalid direction: $directionInput"
		}

	private fun printPlayerStatus(){
		println("(Aura: ${player.auraColor(player.isBlessed)}) " +
				"(Blessed: ${if (player.isBlessed) "YES" else "NO"})")
		println("${player.name} ${player.healthPoints}")
	}

	private class GameInput(arg: String?) {
		private val input = arg ?: ""
		val command = input.split(" ")[0]
		val argument = input.split(" ").getOrElse(1) { "" }

		private fun commandNotFound() = "I'm not quite sure what you're trying to do."

		fun processCommand() = when(command.toLowerCase()) {
			"move" -> move(argument)
			else -> commandNotFound()
		}
	}

	fun play() {
		while (true) {
			println(currentRoom.description())
			println(currentRoom.load())
			printPlayerStatus()

			print("> Enter your command: ")
			println(GameInput(readLine()).processCommand())
		}
	}
}

private fun playerUnderFireballCondition(numFireballs: Int) =
	when (numFireballs) {
		in 1 .. 10 -> "Tipsy"
		in 11 .. 20 -> "Sloshed"
		in 21 .. 30 -> "Soused"
		in 31 .. 40 -> "Stewed"
		else -> "t0aSt3d"
	}

fun main() {
	Game.play()
}
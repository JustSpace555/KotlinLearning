package com.bignerdranch.nyethack

import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.system.exitProcess

object Game {
	private val player = Player("Space")
	private var currentRoom: Room = TownSquare()
	private val worldMap = listOf(
		listOf(currentRoom, Room("Tavern"), Room("Back Room")),
		listOf(Room("Long Corridor"), Room("Generic Room"))
	)

	init {
		println("Welcome, adventurer.")
		currentRoom.ifPlayerHere = true
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
			currentRoom.ifPlayerHere = false
			currentRoom = newRoom
			currentRoom.ifPlayerHere = true
			"OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
		} catch (e: Exception) {
			"Invalid direction: $directionInput"
		}

	private fun printPlayerStatus(){
		println("(Aura: ${player.auraColor(player.isBlessed)}) " +
				"(Blessed: ${if (player.isBlessed) "YES" else "NO"})")
		println("${player.name} ${player.healthPoints}")
	}

	private fun getGeolocation(): String =
		buildString {
			worldMap.forEachIndexed { y, _ ->
				worldMap[y].forEachIndexed { x, _ ->
					if (worldMap[y][x].ifPlayerHere)
						append("X ")
					else
						append("O ")
				}
				append('\n')
			}
		}

	private class GameInput(arg: String?) {
		private val input = arg ?: ""
		val command = input.split(" ")[0]
		val argument = input.split(" ").getOrElse(1) { "" }

		private fun commandNotFound() = "I'm not quite sure what you're trying to do."

		fun processCommand(): String = when(command.toLowerCase()) {
			"quit" -> "quit"
			"ring" -> if (currentRoom is TownSquare)
				(currentRoom as TownSquare).ringBell()
			else
				"You are not on town square"
			"move" -> move(argument)
			"fight" -> fight()
			"map" -> getGeolocation()
			else -> commandNotFound()
		}
	}

	private fun slay(monster: Monster) {
		println("${monster.name} did ${monster.attack(player)} damage!")
		println("${player.name} did ${player.attack(monster)} damage!")

		if (player.healthPoints <= 0) {
			println(">>>>> You have been defeated! Thanks for playing. <<<<<")
			exitProcess(0)
		}

		if (monster.healthPoints <= 0) {
			println(">>>>> ${monster.name} has been defeated! <<<<<")
			currentRoom.monster = null
		}
	}

	private fun fight() = currentRoom.monster?.let {
		while (player.healthPoints > 0 && it.healthPoints > 0) {
			slay(it)
			Thread.sleep(1000)
		}
		"Combat complete."
	} ?: "There's nothing here to fight."

	fun play() {
		while (true) {
			println(currentRoom.description())
			println(currentRoom.load())
			printPlayerStatus()

			print("> Enter your command: ")
			val command = GameInput(readLine()).processCommand()
			if (command == "quit") {
				println("Good bye, adventurer!")
				break
			}
			println(command)
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
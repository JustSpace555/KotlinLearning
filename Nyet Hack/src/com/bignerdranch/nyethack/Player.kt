package com.bignerdranch.nyethack

import java.io.File

class Player(_name: String,
			 var healthPoints: Int = 100,
			 val isBlessed: Boolean = true,
			 private val isImmortal: Boolean = false) {

	var name = _name
		get() = "${field.capitalize()} of $homeTown"
		private set(value) {
			field = value.trim()
		}

	val homeTown by lazy { selectHomeTown() }
	var currentPosition = Coordinate(0, 0)

	private fun selectHomeTown() = File("data/towns.txt")
									.readText()
									.split('\n')
									.shuffled()
									.first()

	init {
		require(healthPoints > 0) {"HealthPoints must be greater than zero."}
		require(name.isNotBlank()) {"Player must have a name."}
	}

	constructor(name: String): this(name, isBlessed = true, isImmortal = false) {
		if (name.toLowerCase() == "kar")
			healthPoints = 40
	}

	fun castFireball(numFireballs: Int = 2): Int {
		val divNum = if (numFireballs > 50) 50 else numFireballs
		println("A glass of Fireball springs into existence. (x$divNum)")
		return divNum
	}

	fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean) =
		when (healthPoints) {
			100 -> "is in excellent condition"
			in 90..99 -> "has a few scratches"
			in 75..90 -> if (isBlessed)
				"has some minor wounds, but is healing quite quickly"
			else
				"has some minor wounds"
			in 15..74 -> "looks pretty hurt"
			else -> "is in awful condition"
		}

	fun auraColor(isBlessed: Boolean) =
		if (isBlessed && healthPoints > 50 || isImmortal) "GREEN" else "NONE"
}
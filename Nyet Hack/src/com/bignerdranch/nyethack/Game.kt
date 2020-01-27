package com.bignerdranch.nyethack

private fun printPlayerStatus(player: Player){
	println("(Aura: ${player.auraColor(player.isBlessed)}) " +
			"(Blessed: ${if (player.isBlessed) "YES" else "NO"})")
	println("${player.name} ${player.healthPoints}")
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
	val player = Player("kar", 100, true, false)

	printPlayerStatus(player)
	playerUnderFireballCondition(player.castFireball())
}
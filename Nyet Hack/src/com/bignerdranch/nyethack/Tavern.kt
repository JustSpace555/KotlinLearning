package com.bignerdranch.nyethack

import java.io.File
import com.bignerdranch.nyethack.extensions.random

const val TAVERN_NAME = "Space's Universe"
var patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split('\n')
val patronGold = mutableMapOf<String, Double>()

private fun String.toDragonSpeak() =
	this.replace(Regex("[aeiou]")) {
		when (it.value) {
			"a", "A" -> "4"
			"e", "E" -> "3"
			"i", "I" -> "1"
			"o", "O" -> "0"
			"u", "U" -> "|_|"
			else -> it.value
		}
	}

private fun String.frame(padding: Int, formatChar: String = "*"): String {
	val middle = formatChar.padEnd(padding) + this + formatChar.padStart(padding)
	val end = middle.indices.joinToString(" ") { formatChar }
	return "$end\n$middle\n$end"
}

fun performPurchase(price: Double, patronName: String) {
	val totalPurse = patronGold.getValue(patronName)
	patronGold[patronName] = totalPurse - price
}

private fun placeOrder(patronName: String, menuData: String) {
	val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
	val tavernsMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)
	val (type, name, price) = menuData.split(',')

	println("$patronName speaks with $tavernsMaster about their order.")
	if (patronGold.getValue(patronName) < price.toDouble()) {
		println("$tavernsMaster says: $patronName don't have enough money to buy $name")
		return
	}
	println("$patronName buys a $name ($type) for $price")
	performPurchase(price.toDouble(), patronName)

	val phrase = if (name == "Dragon's Breath")
		"$patronName exclaims: " + "Ah, delicious $name!".toDragonSpeak()
	else
		"$patronName says: Thanks for the $name"
	println(phrase)
}

fun displayPatronBalances() {
	patronGold.forEach { patron, balance ->
		println("$patron, balance: ${"%.2f".format(balance)}")
	}
}

fun main() {
	(0..9).forEach { uniquePatrons += "${patronList.random()} ${lastName.random()}" }
	println(uniquePatrons)
	uniquePatrons.forEach { patronGold[it] = 6.0 }
	var orderCount = 0
	while (orderCount < 10) {
		placeOrder(
			uniquePatrons.random(),
			menuList.random()
		)
		orderCount++
	}
	displayPatronBalances()
	println("Welcome, Space".frame(5))
}
package com.bignerdranch.nyethack

import java.io.File

const val TAVERN_NAME = "Space's Universe"
var patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split('\n')
val patronGold = mutableMapOf<String, Double>()

fun toDragonSpeak(input: String) =
	input.replace(Regex("[aeiou]")) {
		when (it.value) {
			"a", "A" -> "4"
			"e", "E" -> "3"
			"i", "I" -> "1"
			"o", "O" -> "0"
			"u", "U" -> "|_|"
			else -> it.value
		}
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
		"$patronName exclaims: ${toDragonSpeak("Ah, delicious $name!")}"
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
	/*
	if (com.bignerdranch.nyethack.getPatronList.contains("Eli"))
		println("The tavern master says: Eli's in the back playing cards.")
	else
		println("The tavern master says: Eli isn't here.")

	if (com.bignerdranch.nyethack.getPatronList.containsAll(listOf("Sophie, Mordoc")))
		println("The tavern master says: Yea, they're seated by the stew kettle.")
	else
		println("The tavern master says: Nay, they departed hours ago.")
	*/
	(0..9).forEach { uniquePatrons += "${patronList.shuffled().first()} ${lastName.shuffled().first()}" }
	println(uniquePatrons)
	uniquePatrons.forEach { patronGold[it] = 6.0 }
	var orderCount = 0
	while (orderCount < 10) {
		placeOrder(
			uniquePatrons.shuffled().first(),
			menuList.shuffled().first()
		)
		orderCount++
	}
	displayPatronBalances()
}
fun printConstructionCost(numBuildings: Int) {
	val cost = 500
	println("Construction cost: ${cost * numBuildings}")
}

fun configureGreetingFunction(): (String) -> String {
	val structureType = "hospitals"
	var numBuildings = 5
	return { playerName: String ->
		val currentYear = 2018
		numBuildings += 1
		println("Adding $numBuildings $structureType")
		"Welcome to SimVillage, $playerName! (copyright $currentYear)"
	}
}

fun runSimulation() {
	val greetingFunction = configureGreetingFunction()
	println(greetingFunction("Space"))
}

fun main(args: Array<String>) {
	runSimulation()
}
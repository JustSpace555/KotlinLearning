package calculator

import java.lang.Exception
import java.util.*

fun printErrorAndReturnNull(): String? {
    println("Invalid expression")
    return null
}

fun choosePriority(symbol: Char): Int {
    return when(symbol) {
        '^' -> 4
        '*', '/' -> 3
        '+', '-' -> 2
        else -> 1
    }
}

fun validation(map: MutableMap<String, String>, str: String): Boolean {
    var isNumber = false
    var isSymbol = false
    var amountMultiply = 0
    var amountDiv = 0

    if (!map.containsKey(str)) {
        for (i in 0 until str.length) {
            if (!(str[i].isDigit() || str[i] == '+' || str[i] == '-' ||
                            str[i] == '/' || str[i] == '*' ||
                            str[i] == '^' || str[i] == '(' || str[i] == ')'))
                return false
            if (str[i].isDigit())
                isNumber = true
            if (str[i] == '+' || str[i] == '-' || str[i] == '/' ||
                    str[i] == '*' || str[i] == '^' ||
                    str[i] == '(' || str[i] == ')')
            {
                if (str[i] == '-' || str[i] == '+') {
                    if (i != 0)
                        isSymbol = true
                }
                else {
                    isSymbol = true
                    if (str[i] == '*')
                        amountMultiply++
                    if (str[i] == '/')
                        amountDiv++
                }
            }
        }
    }
    if (isNumber && isSymbol || amountDiv > 1 || amountMultiply > 1 ||
            (amountDiv == amountMultiply && amountDiv != 0))
        return false
    return true
}

fun polishNotation(map: MutableMap<String, String>, str: String?): String? {
    if (str == null)
        return null

    var final = ""
    val stack: Stack<Char> = Stack()
    val scan = Scanner(str)
    var temp: String

    while (scan.hasNext()) {
        temp = scan.next()
        if (!validation(map, temp))
            return printErrorAndReturnNull()
        try {
            if (map.containsKey(temp)) {
                final += map[temp].toString() + ' '
                continue
            }
            final += temp.toBigInteger().toString() + " "
        } catch (e: Exception) {
            if (!stack.isEmpty()) {
                if (temp.first() == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(')
                        final += stack.pop() + " "
                    if (stack.isEmpty())
                        return printErrorAndReturnNull()
                    stack.pop()
                } else {
                    while (!stack.isEmpty() && choosePriority(stack.peek()) >= choosePriority(temp.first())
                            && choosePriority(temp.first()) > 1)
                        final += stack.pop() + " "
                    stack.push(temp.first())
                }
            }
            else
                stack.push(temp.first())
        }
    }
    while (!stack.isEmpty())
        final += stack.pop() + " "
    return final
}

fun calcPolishNotation(str: String): String? {
    val stack: Stack<String> = Stack()
    val scan = Scanner(str)
    var temp: String
    var firstNum: String
    var secondNum: String

    while (scan.hasNext()) {
        temp = scan.next()
        if (temp != "-" && temp != "+" && temp != "*" && temp != "/" && temp != "(" && temp != ")" && temp != "^")
            stack.push(temp)
        else {
            secondNum = stack.pop()
            if (!stack.isEmpty()) {
                firstNum = stack.pop()
                when (temp) {
                    "+" -> stack.push((firstNum.toBigInteger() + secondNum.toBigInteger()).toString())
                    "-" -> stack.push((firstNum.toBigInteger() - secondNum.toBigInteger()).toString())
                    "*" -> stack.push((firstNum.toBigInteger() * secondNum.toBigInteger()).toString())
                    "/" -> stack.push((firstNum.toBigInteger() / secondNum.toBigInteger()).toString())
                    "^" -> stack.push((firstNum.toBigInteger().pow(secondNum.toInt())).toString())
                    "(" -> return printErrorAndReturnNull()
                }
            }
            else
                stack.push(temp + secondNum)
        }
    }
    firstNum = stack.pop()
    if (!stack.isEmpty())
        return printErrorAndReturnNull()
    return firstNum
}

fun validateVar(str: String): Boolean {
    var amountEquals = 0
    var isDigit = false
    var isLetter = false

    for (i in str) {
        if (i == '=')
            amountEquals++
        if (i.isDigit() && amountEquals == 0) {
            println("Invalid identifier")
            return false
        }
        if (amountEquals >= 1 && i != '=' && i != ' ') {
            if (i.isDigit())
                isDigit = true
            else
                isLetter = true
        }
        if (amountEquals > 1 || isDigit && isLetter) {
            println("Invalid assignment")
            return false
        }
    }
    return true
}

fun parseVar(map: MutableMap<String, String>, str: String) {
    if (!validateVar(str))
        return

    val key: String = str.substringBefore('=').replace(" ", "")
    val value: String
    try {
        value = str.substringAfter('=').replace(" ", "")
        if (map.containsKey(key))
            map.replace(key, value)
        else
            map[key] = value
    }
    catch (e: Exception) {
        val tempKey: String = str.substringAfter('=').replace(" ", "")
        if (!map.containsKey(tempKey)) {
            println("Unknown variable")
            return
        }
        if (map.containsKey(key))
            map.replace(key, map[tempKey]!!)
        else
            map[key] = map[tempKey]!!
    }
}

fun checkValue(map: MutableMap<String, String>, str: String): Boolean {
    for (i in str)
        if (!i.isLetter())
            return false
    if (map.containsKey(str))
        println(map[str])
    else
        println("Unknown variable")
    return true
}

fun putSpaces(str: String): String {
    var final = ""
    var i = 0
    var temp: Int

    while (i in 0 until str.length) {
        temp = i
        if (str[i] == ' ') {
            i++
            continue
        }
        while (i < str.length && (str[i].isDigit() || str[i].isLetter())) {
            final += str[i]
            i++
        }
        if (i != temp) {
            final += ' '
            continue
        }
        if (str[i] == '+' || str[i] == '-' || str[i] == '/' ||
                str[i] == '*' || str[i] == '^' ||
                str[i] == '(' || str[i] == ')')
        {
            final += str[i] + " "
            i++
        }
    }
    return final
}

fun deleteUselessOperations(str: String): String? {
    val scan = Scanner(str)
    var amountMinusesRepeat = 0
    var prev = ""
    var curr: String
    var final = ""

    while (scan.hasNext()) {
        curr = scan.next()
        if (curr != prev || curr == "(" || curr == ")") {
            final += if (amountMinusesRepeat % 2 == 0)
                "$curr "
            else
                "-$curr "
            amountMinusesRepeat = 0
        }
        else if (curr == prev)
        {
            if (curr == "*" || curr == "/" || curr == "^")
                return printErrorAndReturnNull()
            if (curr == "-")
                amountMinusesRepeat++
        }
        prev = curr
    }
    return final
}

fun main() {
    val scan = Scanner(System.`in`)
    var str: String?
    val variables = mutableMapOf<String, String>()

    while (scan.hasNextLine()) {
        str = scan.nextLine()
        if (str.length > 1 && str.first() == '/' && str != "/exit" && str != "/help") {
            println("Unknown command")
            continue
        }
        if ("/exit" == str) {
            println("Bye!")
            return
        }
        if ("/help" == str) {
            println("The program calculates the sum of numbers")
            continue
        }
        if ("\n" == str || str.isEmpty() || checkValue(variables, str))
            continue
        if (str.contains('='))
            parseVar(variables, str)
        else {
            str = polishNotation(variables, deleteUselessOperations(putSpaces(str)))
            if (str != null) {
                str = calcPolishNotation(str)
                if (str != null)
                    println(str)
            }
        }
    }
}
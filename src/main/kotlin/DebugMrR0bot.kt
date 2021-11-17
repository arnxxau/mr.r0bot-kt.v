fun main() {
    val string = readLine()
    val list = BasicALU().parseInput(string ?: "" )
    println(list)
    for (x in BasicALU().tableConstructor(Pair(list.first, list.second))) println(x)
    BasicALU().printTable(BasicALU().tableConstructor(Pair(list.first, list.second)))
}
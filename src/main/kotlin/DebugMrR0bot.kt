fun main() {
    println("Welcome to Mr R0bot v0.1.0 (kt.v)")
    println("       _______\n" +
            "     _/       \\_\n" +
            "    / |       | \\\n" +
            "   /  |__   __|  \\\n" +
            "  |__/((o| |o))\\__|\n" +
            "  |      | |      |\n" +
            "  |\\     |_|     /|\n" +
            "  | \\           / |\n" +
            "   \\| /  ___  \\ |/\n" +
            "    \\ | / _ \\ | /\n" +
            "     \\_________/\n" +
            "      _|_____|_\n" +
            " ____|_________|____\n" +
            "/                   \\  ")
    println("1 -> To use ALU MODULE")
    println("2 -> To use SISA-I MODULE")

    when (readLine()?.toInt()) {
        1 -> {
            println("You've entered Mr R0bot's ALU MODULE")
            println("Type a Mnemonic in order to display " +
                    "the table in the following format: MODE destinyREG REG@A REG@B")
            val list = BasicALU().parseInput(readLine() ?: "" )
            BasicALU().printTable(BasicALU().tableConstructor(Pair(list.first, list.second)))
        }

        2 -> {
            println("You've entered Mr R0bot's SISA-I MODULE")
            println("Type a hex number in order to display its assembly equivalent")
            println(SISA().hexaParser(readLine() ?: ""))
        }
    }

    //val string = readLine()
    //val list = BasicALU().parseInput(string ?: "" )
    //println(list)
    //for (x in BasicALU().tableConstructor(Pair(list.first, list.second))) println(x)
    //BasicALU().printTable(BasicALU().tableConstructor(Pair(list.first, list.second)))
    //println(BasicALU().getFunctionID(string ?: ""))
    //println(`SISA-I`().getFunctionID(string ?: "" ))
    //println(SISA().hexaParser(string ?: ""))

    //`SISA-I`()
}
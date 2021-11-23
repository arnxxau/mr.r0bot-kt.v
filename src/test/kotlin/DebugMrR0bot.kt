fun main2() {
    println("Welcome to Mr R0bot v0.1.0 (kt.v)")
    println(
        "       _______\n" +
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
                "/                   \\  "
    )
    println("1 -> To use ALU MODULE")
    println("2 -> To use SISA-I MODULE")

    when (readLine()?.toInt()) {
        1 -> {
            println("You've entered Mr R0bot's ALU MODULE")
            println(
                "Type a Mnemonic in order to display " +
                        "the table in the following format: MODE destinyREG REG@A REG@B"
            )
            val list = AluMODULE().intelParse(readLine() ?: "")
            val r = mutableListOf("@A", "@B", "Rb/N", "OP", "F", "In/Alu", "@D", "WrD", "N")
            AluMODULE().printTable(r, AluMODULE().aluTable(list))
        }

        2 -> {
            println("You've entered Mr R0bot's SISA-I MODULE")
            println("Type a hex number in order to display its assembly equivalent")
            println(SisaMODULE().hexaParser(readLine() ?: ""))
        }
    }
}
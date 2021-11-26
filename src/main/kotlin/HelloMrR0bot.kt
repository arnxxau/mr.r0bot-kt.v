fun main() {
    println("Welcome to Mr R0bot v0.2.5 (kt.v)")
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
                "/                   \\  by arnxxau"
    )
    println("1 -> ALU MODULE")
    println("2 -> SISA-I MODULE")
    println("q -> exit")


    do {
        var input = readLine()!!
        when (input) {
            "1" -> {
                println("*You've entered Mr R0bot's ALU MODULE*")
                println(
                    "Type a Mnemonic in order to display " +
                            "the table in the following format: MODE destinyREG, REG@A, REG@B"
                )
                input = readLine()!!
                do {
                    AluMODULE().printAluPhrase(input)
                    input = readLine()!!
                } while (input != "q")
            }
            "2" -> {
                println("*You've entered Mr R0bot's SISA-I MODULE*")
                println("1 -> hex to assembly")
                println("2 -> binary to assembly")
                println("3 -> assembly to hex")
                println("4 -> assembly to control phrase")

                input = readLine()!!
                when (input) {
                    "1" -> {
                        input = readLine()!!
                        do {
                            println(SisaMODULE().hexaParser(input))
                            input = readLine()!!
                        } while (input != "q")
                    }
                    "2" -> {
                        input = readLine()!!
                        do {
                            println(SisaMODULE().binParser(input))
                            input = readLine()!!
                        } while (input != "q")
                    }
                    "3" -> {
                        input = readLine()!!
                        do {
                            val parsed = SisaMODULE().sisaIntelParse(input)
                            println(Utils().encryptAssembly(parsed).toInt(2).toString(16).uppercase())
                            input = readLine()!!
                        } while (input != "q")
                    }
                    "4" -> {
                        input = readLine()!!
                        do {
                            SisaMODULE().printSisaPhrase(input)
                            input = readLine()!!
                        } while (input != "q")
                    }
               }
            }

        }
    } while (input != "q")
}
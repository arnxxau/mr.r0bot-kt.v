open class AluMODULE: Utils() {
    fun intelParse(s: String): MutableList<DataSlot> {
        var isHex = false
        val data = s.split(" ").map {
            if (it.contains("0x") || it.contains("0X")) isHex = true
            it.removePrefix("0x")
                .removePrefix("0X")
                .removeSuffix(",")
                .removeSuffix("I")
        }

        val output = mutableListOf<DataSlot>()
        val id = getID(data[0], DATA.ALU_TABLE, true)
        if (id != null) {
            output.add(DataSlot(id.first, 3, false, Type.F))
            output.add(DataSlot(id.second, 2, false, Type.OP))
        }
        for ((idx, d) in data.withIndex()) {
            if (d.startsWith('R')) {
                val sum = if (verifyParse(idx, output)) 1
                            else 0
                output.add(
                    DataSlot(d.removePrefix("R").toInt(),
                        3, false, selectRegister(idx + sum)))
            } else if (d.toIntOrNull(radix = 16) != null && isHex) {
                output.add(DataSlot(d.toInt(radix = 16),
                    16, false, Type.N, toHex = true))
            } else if (d.toIntOrNull(radix = 10) != null) {
                output.add(DataSlot(d.toInt(radix = 10),
                    16, false, Type.N, toHex = true))
            }
            else if (d.contains('(')) {
                val dSplit = d.split(('('))
                output.add(DataSlot(dSplit[0].toInt(),
                    16, false, Type.N))
                output.add(DataSlot(dSplit[1].substring(1, 2).toInt(),
                        3, false, Type.A))
            }
        }
        return output
    }

    fun aluTable(aluCommand: MutableList<DataSlot>): Array<DataSlot> {
        for (command in aluCommand)
            if (command.type == Type.UNKNOWN) println("Syntax error")
        val aluList = DATA.ALU_CONSTRUCTOR
        for (command in aluCommand) {
            for (c in aluList.indices) {
                if (aluList[c].type == command.type) aluList[c] = command
            }
        }
        //OP & F logic
        aluList[3].discard = aluCommand[0].value == 9 || aluCommand[0].value == 8
        aluList[4].discard = aluCommand[0].value == 9 || aluCommand[0].value == 8
        //A logic
        if (aluCommand[0].value == 9) {
            aluList[0].value = aluList[6].value
            aluList[0].discard = false
            aluList[6].discard = true
        }
        //N logic
        if (!aluList[8].discard or !aluList[1].discard){
            aluList[2].value = if (aluList[8].discard) 1
                            else 0
            aluList[2].discard = false
        }
        //WrD logic
        aluList[7].value = if (aluList[6].discard) 0
                        else 1
        //In/Alu logic
        if (aluList[6].value != 0 && aluCommand[0].value != 9) {
            aluList[5].value = if (aluCommand[0].value == 8) 1
            else 0
            aluList[5].discard = false
        }
        return aluList
    }

    fun printAluPhrase(s: String) {
        val table = aluTable(intelParse(s))
        val binChain = generateBinaryChain(table)
        val topRow = mutableListOf("@A", "@B", "Rb/N", "OP", "F", "In/Alu", "@D", "WrD", "N")
        printTable(topRow, table)
        println("Binary Chain: $binChain")
        println("Hexa Chain: 0x${binChain.toLong(2).toString(16).uppercase()}")
    }
}
open class AluMODULE: Utils() {
    fun intelParse(s: String): MutableList<DataSlot> {
        var isHex = false
        val data = s.split(" ").map {
            if (it.contains("0x")
                || it.contains("0X")) isHex = true

            it.removePrefix("0x")
                .removePrefix("0X")
                .removeSuffix(",")
        }.toMutableList()

        if (data[0] != "MOVEI") data[0] = data[0].removeSuffix("I")
        val output = mutableListOf<DataSlot>()
        val id = getID(data[0], DATA.ALU_TABLE, true)
        if (id != null) {
            output.add(DataSlot(id.first, 3, Type.F, false))
            output.add(DataSlot(id.second, 2, Type.OP, false))
        }
        for ((idx, d) in data.withIndex()) {
            if (d.startsWith('R')) {
                val sum = if (verifyParse(idx, output)) 1
                            else 0
                output.add(
                    DataSlot(d.removePrefix("R").toInt(),
                        3, selectRegister(idx + sum), false))
            } else if (d.toIntOrNull(radix = 16) != null && isHex) {
                output.add(DataSlot(d.toInt(radix = 16),
                    16, Type.N, false, toHex = true))
            } else if (d.toIntOrNull(radix = 10) != null) {
                output.add(DataSlot(d.toInt(radix = 10),
                    16, Type.N, false, toHex = true))
            }
            else if (d.contains('(')) {
                val dSplit = d.split(('('))
                output.add(DataSlot(dSplit[0].toInt(),
                    16, Type.N, false))
                output.add(DataSlot(dSplit[1].substring(1, 2).toInt(),
                        3, Type.A, false))
            }
        }
        return output
    }

    fun aluTable(parsedSlots: MutableList<DataSlot>): Array<DataSlot> {
        for (slot in parsedSlots)
            if (slot.type == Type.UNKNOWN) println("Syntax error")
        val dataSlots = DATA.ALU_CONSTRUCTOR.map {
            it.copy()
        }.toTypedArray()
        for (slot in parsedSlots) {
            for (c in dataSlots.indices) {
                if (dataSlots[c].type == slot.type) dataSlots[c] = slot
            }
        }
        //OP & F logic
        dataSlots[3].discard = parsedSlots[0].value == 9 || parsedSlots[0].value == 8
        dataSlots[4].discard = parsedSlots[0].value == 9 || parsedSlots[0].value == 8
        //A logic
        if (parsedSlots[0].value == 9) {
            dataSlots[0].value = dataSlots[6].value
            dataSlots[0].discard = false
            dataSlots[6].discard = true
        }
        //N logic
        if (!dataSlots[8].discard or !dataSlots[1].discard){
            dataSlots[2].value = if (dataSlots[8].discard) 1
                            else 0
            dataSlots[2].discard = false
        }
        //WrD logic
        dataSlots[7].value = if (dataSlots[6].discard) 0
                        else 1
        //In/Alu logic
        if (!dataSlots[6].discard && parsedSlots[0].value != 9) {
            dataSlots[5].value = if (parsedSlots[0].value == 8) 1
            else 0
            dataSlots[5].discard = false
        }
        return dataSlots
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
import de.vandermeer.asciitable.AsciiTable

class BasicALU {
    data class DataSlot (
        var value: Int,
        var nBit: Int,
        var discard: Boolean,
        var type: Type
            )

    enum class Type {
        A, B, RbN, OP, F, InAlu, D, WrD, N, UNKNOWN, IN, OUT
    }

    private fun selectRegister(idx: Int): Type {
        return when (idx) {
            1 -> Type.D
            2 -> Type.A
            3 -> Type.B
            else -> Type.UNKNOWN
        }
    }

    private fun getFunctionID(s: String): Triple<Pair<Int, Int>, Type, Boolean>{
        var function = -1
        var operation = -1
        var type: Type = Type.UNKNOWN
        var is_n = false
        for (i in 0 until DATA.ALU_TABLE.size) {
            for (j in 0 until DATA.ALU_TABLE[0].size) {
                if (s.startsWith(DATA.ALU_TABLE[i][j])){
                    function = i; operation = 3-j; type = Type.OP
                }
                if (s.endsWith("I")) is_n = true
            }
        }

        if (s == "IN") type = Type.IN
        else if (s == "OUT") type = Type.OUT

        return Triple(Pair(function, operation), type, is_n)
    }

    fun parseInput(s: String): Pair<MutableList<DataSlot>, Boolean> {
        val inputArray = s.split(" ")
        val aluCommand = mutableListOf<DataSlot>()
        var save = false
        for ((idx, i) in inputArray.withIndex()) {
            when {
                i.startsWith("R") -> aluCommand.add(DataSlot(i.removePrefix("R").toInt(),
                                        3, false, selectRegister(idx)))

                i.startsWith("0x")
                        or i.startsWith("0X") -> aluCommand.add(DataSlot(i.removeRange(0, 2).toInt(radix = 16),
                                                    16, false, Type.N))

                i.toIntOrNull() != null -> aluCommand.add(DataSlot(i.toInt(radix = 16),
                                        16, false, Type.N))

                else -> {
                    val p = getFunctionID(i)
                    save = p.third
                    if (p.second == Type.OP) {
                        aluCommand.add(DataSlot(p.first.first, 3, false, Type.F))
                        aluCommand.add(DataSlot(p.first.second, 2, false, Type.OP))
                    } else
                        aluCommand.add(DataSlot(-1, -1, true, p.second))
                }
            }
        }
        return Pair(aluCommand, save)
    }

    fun tableConstructor(aluCommand: Pair<MutableList<DataSlot>, Boolean>): Array<DataSlot> {
        for (command in aluCommand.first)
            if (command.type == Type.UNKNOWN) println("Syntax error")

        val aluList = DATA.ALU_CONSTRUCTOR

        for (command in aluCommand.first) {
            for (c in aluList.indices) {
                if (aluList[c].type == command.type) aluList[c] = command
            }
        }

        //A logic
        if (aluCommand.first[0].type == Type.OUT) {
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
        if (aluList[6].value != 0 && aluCommand.first[0].type != Type.OUT) {
            aluList[5].value = if (aluCommand.first[0].type == Type.IN) 1
            else 0
            aluList[5].discard = false
        }
        return aluList
    }

    fun toBinary(x: Int, len: Int): String {
        return String.format(
            "%" + len + "s",
            Integer.toBinaryString(x)
        ).replace(" ".toRegex(), "0")
    }

    fun printTable(aluCommand: Array<DataSlot>) {
        val table = AsciiTable()
        val stringArray = mutableListOf<String>()
        table.addRule()
        table.addRow("@A", "@B", "Rb/N", "OP", "F", "In/Alu", "@D", "WrD", "N")
        for (c in aluCommand) {
            if (c.discard) {
                var s = ""
                for (i in 0 until c.nBit) s += "X"
                stringArray.add(s)
            }else stringArray.add(toBinary(c.value, c.nBit))
        }
        table.addRule()
        table.addRow(stringArray)
        println(table.render())
    }
}
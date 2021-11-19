open class BasicALU: Utils() {
    open var referenceTable = DATA.ALU_TABLE
    override val dataArray: MutableList<String> =
        mutableListOf("@A", "@B", "Rb/N", "OP", "F", "In/Alu", "@D", "WrD", "N")


    data class DataSlot (
        var value: Int,
        var nBit: Int,
        var discard: Boolean,
        var type: Type
            )

    enum class Type {
        A, B, RbN, OP, F, InAlu, D, WrD, N, UNKNOWN
    }

    fun selectRegister(idx: Int): Type {
        return when (idx) {
            1 -> Type.D
            2 -> Type.A
            3 -> Type.B
            else -> Type.UNKNOWN
        }
    }

    open fun getFunctionID(s: String): Triple<Pair<Int, Int>, Type, Boolean>{
        var function = -1; var operation = -1; var i = 0
        var type = Type.UNKNOWN
        var save = false; var match = false
        while (i < referenceTable.size && !match) {
            for (j in referenceTable[i].indices) {
                if (s.startsWith(referenceTable[i][j])){
                    function = i; operation = j; type = Type.OP; match = true
                }
                if (s.endsWith("I")) save = true
            }
            ++i
        }
        return Triple(Pair(function, operation), type, save)
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

        //OP & F logic
        aluList[3].discard = aluCommand.first[0].value == 9 || aluCommand.first[0].value == 8
        aluList[4].discard = aluCommand.first[0].value == 9 || aluCommand.first[0].value == 8

        //A logic
        if (aluCommand.first[0].value == 9) {
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
        if (aluList[6].value != 0 && aluCommand.first[0].value != 9) {
            aluList[5].value = if (aluCommand.first[0].value == 8) 1
            else 0
            aluList[5].discard = false
        }
        return aluList
    }
}
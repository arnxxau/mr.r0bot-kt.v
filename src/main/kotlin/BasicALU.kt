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
        A, B, RbN, OP, F, InAlu,  ila, D, WrD, WrOut, RdIn, WrMem, Byte, TknBr, N, UNKNOWN, C, E, ADDRIO
    }

    fun selectRegister(idx: Int): Type {
        return when (idx) {
            1 -> Type.D
            2 -> Type.A
            3 -> Type.B
            else -> Type.UNKNOWN
        }
    }

    fun verifyParse(idx: Int, input: MutableList<DataSlot>): Boolean {
        for (data in input) {
            if (data.type == selectRegister(idx)) return true
        }
        return false
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

    fun getID(s: String, toSearch: Array<Array<String>>): Pair<Int, Int>? {
        for (i in toSearch.indices){
            for (j in toSearch[i].indices) {
                if (s.startsWith(toSearch[i][j])) return Pair(i, j)
            }
        }
        return null
    }

    fun intelParse(s: String): MutableList<DataSlot> {
        val data = s.split(" ").map {
            it.removePrefix("0x")
                .removePrefix("0X")
                .removeSuffix(",")
        }
        val output = mutableListOf<DataSlot>()
        val id = getID(data[0], DATA.ALU_TABLE)
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
            } else if (d.toIntOrNull(radix = 16) != null) {
                output.add(DataSlot(d.toInt(radix = 16),
                    16, false, Type.N))
            } else if (d.contains('(')) {
                val dSplit = d.split(('('))
                output.add(DataSlot(dSplit[0].toInt(),
                    16, false, Type.N))
                output.add(DataSlot(dSplit[1].substring(1, 2).toInt(),
                        3, false, Type.A))
            }
        }
        return output
    }

    fun getSisaConstructor(code: Pair<Int, Int>): Array<DataSlot>? {
        return when(code.second) {
            0, 1 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE0
            4, 6 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_B
            2, 3, 5, 7 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_D
            8 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A
            9 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D
            10 -> {
                if (code.first == 0) DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A
                else DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D
            }
            else -> null
        }
    }

    fun sisaIntelParse(s: String): Array<DataSlot> {
        val data = s.split(" ")
        val output = intelParse(s)
        val code = getID(data[0], DATA.SISA_TABLE)
        if (code != null && code.second == 8)
            output[0].type = Type.A
        val constructor = getSisaConstructor(code!!)
        for (slot in constructor!!){
            for (out in output) {
                if (slot.type == Type.E) {
                    slot.value = code.first
                }
                if (slot.type == out.type) {
                    slot.value = out.value
                }
            }
        }
        constructor[0].value = code.second
        return constructor
    }

    fun encryptAssembly(construct: Array<DataSlot>): String {
        var output = ""
        for (slot in construct) {
            output += slot.value.toBinTwosComplement(slot.nBit)
        }
        return output
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
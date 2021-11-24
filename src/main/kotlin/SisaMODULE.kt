class SisaMODULE : AluMODULE() {
    fun getSisaConstructor(code: Pair<Int, Int>): Array<DataSlot>? {
        return when(code.second) {
            0, 1 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE0
            4, 6 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_B
            2, 3, 5, 7 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_D
            8 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A
            9 -> DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D
            10 -> {
                if (code.first == 1) DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A
                else DATA.SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D
            }
            else -> null
        }
    }

    fun sisaIntelParse(s: String): Array<DataSlot> {
        val data = s.split(" ")
        val output = intelParse(s)
        val code = getID(data[0], DATA.SISA_TABLE, true)
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

    fun sisaTable(constructor: Array<DataSlot>): Array<DataSlot> {
        val dataSlots = constructor.toMutableList()
        var c = 0; var e = 0
        for (slot in constructor) {
            if (slot.type == Type.C)
                c = slot.value
            else if (slot.type == Type.E)
                e = slot.value
        }
        val finaleConstruct =  DATA.SISA_CONSTRUCTOR
        if (c == 10) {
            dataSlots[3].discard = true
            finaleConstruct[14].value = dataSlots[3].value
            finaleConstruct[14].discard = false
        }
        // TknBr logic
        val romContent = DATA.SISA_LOGIC_MAP[Pair(e, c)]!!
        var tknbr = 0; val bz = romContent.selectBit(18, 18)!!; val bnz = romContent.selectBit(19, 19)!!
        if (c == 8) {
            println("Introduce the value of R${dataSlots[1].value}")
            val value = intelParse(readLine()!!)[0].value
            println(value)
            tknbr = if (value > 0 && bnz == 1 || value == 0 && bz == 1) 1
                    else 0
        }
        dataSlots.add(DataSlot(tknbr, 1, false, Type.TknBr))
        dataSlots.add(DataSlot(romContent.selectBit(17, 17)!!, 1, false, Type.WrMem))
        dataSlots.add(DataSlot(romContent.selectBit(16, 16)!!, 1, false, Type.RdIn))
        dataSlots.add(DataSlot(romContent.selectBit(15,15)!!, 1, false, Type.WrOut))
        dataSlots.add(DataSlot(romContent.selectBit(14,14)!!, 1, false, Type.WrD))
        val byte = romContent.selectBit(13,13)
        dataSlots.add(DataSlot(byte?: 0, 1, byte == null, Type.Byte))
        val n = romContent.selectBit(12,12)
        dataSlots.add(DataSlot(n?: 0, 1, n == null, Type.RbN))
        val ila = romContent.selectBit(11,10)
        dataSlots.add(DataSlot(ila?: 0, 2, ila == null, Type.ila))
        val op = romContent.selectBit(9,8)
        dataSlots.add(DataSlot(op?: 0, 2, op == null, Type.OP))
        val f = romContent.selectBit(4,2)
        dataSlots.add(DataSlot(f?: 0, 3, f == null, Type.F))

        for (data in dataSlots) {
            for (d in finaleConstruct.indices) {
                if (finaleConstruct[d].type == data.type) {
                    finaleConstruct[d].value = data.value
                    finaleConstruct[d].discard = data.discard
                }
            }
        }
        return finaleConstruct
    }

    fun hexaParser(s: String): String {
        return binParser(intelParse(s)[0].value.toBinary(16))
    }

    fun binParser(binChain: String): String {
        val function = binChain.selectBit(15, 12)!!
        val instChain = mutableListOf<String>()
        when (function) {
            in 0..1 -> {
                instChain.add(DATA.SISA_TABLE[binChain.selectBit(2, 0)!!][function])
                instChain.add(("R${binChain.selectBit(5, 3)}"))
                instChain.add(("R${binChain.selectBit(11, 9)}"))
                instChain.add(("R${binChain.selectBit(8, 6)}"))
            }
            in 2..7 -> {
                instChain.add(DATA.SISA_TABLE[0][function])
                instChain.add(("R${binChain.selectBit(8, 6)}"))
                instChain.add(("R${binChain.selectBit(11, 9)}"))
                instChain.add((binChain.selectBit(5, 0)!!.toBinTwosComplement(6).twsBinaryToDec().toString()))
            }
            in 8..10 -> {
                instChain.add(DATA.SISA_TABLE[binChain.selectBit(8, 8)!!][function])
                instChain.add(("R${binChain.selectBit(11, 9)}"))
                instChain.add(binChain.selectBit(7, 0)!!.toBinTwosComplement(6).twsBinaryToDec().toString())
            }
        }
        return getConstruct(instChain)
    }

    fun printSisaPhrase(s: String) {
        val table = sisaTable(sisaIntelParse(s))

        val binChain = generateBinaryChain(table)
        val topRow = mutableListOf("@A", "@B", "Rb/N", "OP", "F", "-/i/l/a", "@D",
            "WrD", "Wr-Out", "Rd-In", "Wr-Mem", "Byte", "TknBr", "N", "ADDR-IO")
        printTable(topRow, table)
        println("Binary Chain: $binChain")
        println("Hexa Chain: 0x${binChain.toLong(2).toString(16).uppercase()}")
    }

    private fun getConstruct(chain: MutableList<String>): String {
        return when (chain[0]){
            "LD", "LDB" -> chain[0] + " " + chain[1] + ", " + chain[3] + '(' + chain[2] + ')'
            "ST", "STB" -> chain[0] + " " + chain[3] + '(' + chain[2] + ')'  + ", " + chain[1]
            else -> chain.removeFirst() + " " + chain.joinToString(", ")
        }
    }
}
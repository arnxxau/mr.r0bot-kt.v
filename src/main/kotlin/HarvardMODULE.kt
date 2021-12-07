class HarvardMODULE: SisaMODULE() {

    // Constructs the 47 bit control phrase
    fun harvardTable(constructor: Array<DataSlot>): Array<DataSlot> {
        val dataSlots = constructor.toMutableList()
        val opCode = dataSlots[0].value
        val e = if (dataSlots[2].type == Type.E)
                    dataSlots[2].value
                else 0
        // A deep copy is created in order to not overwrite the basic construct
        val finaleConstruct =  DATA.HARVARD_CONSTRUCTOR.map {
            it.copy()
        }.toTypedArray()

        // Some fixes have to be applied when a 'MOVHI' is found
        if (e == 1 && opCode == 9) {
            // REG@D is duplicated into REG@A
            finaleConstruct[0].value = dataSlots[1].value
            finaleConstruct[0].discard = false
        }
        // 'N' has to be displayed as 'ADDRIO' when an IN/OUT order is used
        if (opCode == 10) {
            dataSlots[3].discard = true
            finaleConstruct[14].value = dataSlots[3].value
            finaleConstruct[14].discard = false
        }
        val romContent = DATA.HARVARD_LOGIC_MAP[Pair(e, opCode)]!!

        // Function value logic
        val f = romContent.selectBits(4,2)
        if (dataSlots.last().type != Type.F)
            dataSlots.add(DataSlot(f?: 0, 3, Type.F, f == null))

        // TknBr logic
        var tknBr = 0; val bz = romContent.selectBits(18, 18)!!; val bnz = romContent.selectBits(19, 19)!!
        if (opCode == 8) {
            dataSlots[3].value *= 2
            println("Introduce the value of R${dataSlots[1].value}")
            val value = intelParse(readLine()!!)[0].value
            tknBr = if (value > 0 && bnz == 1
                            || value == 0 && bz == 1) 1
                    else 0
        }

        // General parsing from romContent
        dataSlots.add(DataSlot(tknBr, 1, Type.TknBr, false))
        dataSlots.add(DataSlot(romContent.selectBits(17, 17)!!, 1,  Type.WrMem, false))
        dataSlots.add(DataSlot(romContent.selectBits(16, 16)!!, 1, Type.RdIn, false))
        dataSlots.add(DataSlot(romContent.selectBits(15,15)!!, 1, Type.WrOut, false))
        dataSlots.add(DataSlot(romContent.selectBits(14,14)!!, 1, Type.WrD, false))

        // Parsing with null logic from romContent
        val byte = romContent.selectBits(13,13)
        dataSlots.add(DataSlot(byte?: 0, 1, Type.Byte, byte == null))
        val n = romContent.selectBits(12,12)
        dataSlots.add(DataSlot(n?: 0, 1, Type.RbN, n == null))
        val ila = romContent.selectBits(11,10)
        dataSlots.add(DataSlot(ila?: 0, 2, Type.ila, ila == null))
        val op = romContent.selectBits(9,8)
        dataSlots.add(DataSlot(op?: 0, 2, Type.OP, op == null))

        // Filling the parsed dataSlots into the finale Construct
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

    // Pre -> an assembly SISA string
    fun printHarvardPhrase(s: String) {
        val table = harvardTable(sisaIntelParse(s))
        val binChain = generateBinaryChain(table)
        val topRow = mutableListOf("@A", "@B", "Rb/N", "OP", "F", "-/i/l/a", "@D",
            "WrD", "Wr-Out", "Rd-In", "Wr-Mem", "Byte", "TknBr", "N", "ADDR-IO")
        printTable(topRow, table)
        println("Binary Chain: $binChain")
        println("Hexa Chain: 0x${binChain.toLong(2).toString(16).uppercase()}")
    }
}
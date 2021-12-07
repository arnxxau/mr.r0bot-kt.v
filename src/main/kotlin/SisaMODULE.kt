open class SisaMODULE : AluMODULE() {

    // Returns the SISA constructor depending on the input type
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

    // Extends intelParse() from AluMODULE and returns the every parsed type
    fun sisaIntelParse(s: String): Array<DataSlot> {
        val data = s.split(" ")
        val parsedSlots = intelParse(s)
        val code = getID(data[0], DATA.SISA_TABLE)
        if (code != null && code.second == 8)
            parsedSlots[0].type = Type.A
        val const = getSisaConstructor(code!!)!!.map {
            it.copy()
        }.toTypedArray()

        if (parsedSlots[3].type == Type.E) {
            parsedSlots[3].value = code.first
            parsedSlots[3].discard = false
        }

        for (cSlot in const){
            for (pSLot in parsedSlots) {
                if (cSlot.type == pSLot.type) {
                    cSlot.value = pSLot.value
                    cSlot.discard = false
                }
            }
        }
        const[0].value = code.second
        return const
    }

    // Converts hex machine language into assembly
    fun hexaParser(s: String): String {
        return binParser(intelParse(s)[0].value.toBinary(16))
    }

    // Converts bin machine language into assembly
    fun binParser(binChain: String): String {
        val function = binChain.selectBits(15, 12)!!
        val instChain = mutableListOf<String>()
        when (function) {
            in 0..1 -> {
                instChain.add(DATA.SISA_TABLE[binChain.selectBits(2, 0)!!][function])
                instChain.add(("R${binChain.selectBits(5, 3)}"))
                instChain.add(("R${binChain.selectBits(11, 9)}"))
                instChain.add(("R${binChain.selectBits(8, 6)}"))
            }
            in 2..7 -> {
                instChain.add(DATA.SISA_TABLE[0][function])
                instChain.add(("R${binChain.selectBits(8, 6)}"))
                instChain.add(("R${binChain.selectBits(11, 9)}"))
                instChain.add((binChain.selectBits(5, 0)!!.toBinTwosComplement(6).twsBinaryToDec().toString()))
            }
            in 8..10 -> {
                instChain.add(DATA.SISA_TABLE[binChain.selectBits(8, 8)!!][function])
                instChain.add(("R${binChain.selectBits(11, 9)}"))
                instChain.add(binChain.selectBits(7, 0)!!.toBinTwosComplement(6).twsBinaryToDec().toString())
            }
        }
        return formatAssembly(instChain)
    }

    // Formats the assembly output
    private fun formatAssembly(chain: MutableList<String>): String {
        return when (chain[0]){
            "LD", "LDB" -> chain[0] + " " + chain[1] + ", " + chain[3] + '(' + chain[2] + ')'
            "ST", "STB" -> chain[0] + " " + chain[3] + '(' + chain[2] + ')'  + ", " + chain[1]
            "OUT" -> chain[0]  + ' ' + chain[2] + ", " + chain[1]
            "JALR" -> chain[0]  + ' ' + chain[1] + ", " + chain[2]
            else -> chain.removeFirst() + " " + chain.joinToString(", ")
        }
    }
}
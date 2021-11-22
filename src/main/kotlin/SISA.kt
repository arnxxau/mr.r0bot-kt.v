class SISA : BasicALU() {

    //override var referenceTable: Array<Array<String>> = DATA.SISA_TABLE
    override val dataArray: MutableList<String> =
        mutableListOf("@A", "@B", "Rb/N", "OP", "F", "-/i/l/a", "@D",
            "WrD", "Wr-Out", "Rd-In", "Wr-Mem", "Byte", "TknBr", "N", "ADDR-IO")

    fun hexaParser(s: String): String {
        return binParser(parseInput(s).first[0].value.toBinary(16))
    }

    fun binParser(binaryChain: String): String {
        val function = binaryChain.selectBit(15, 12)
        val instructionChain = mutableListOf<String>()
        when (function) {
            in 0..1 -> {
                instructionChain.add(DATA.SISA_TABLE[binaryChain.selectBit(2, 0)][function])
                instructionChain.add(("R${binaryChain.selectBit(5, 3)}"))
                instructionChain.add(("R${binaryChain.selectBit(11, 9)}"))
                instructionChain.add(("R${binaryChain.selectBit(8, 6)}"))
            }
            in 2..7 -> {
                instructionChain.add(DATA.SISA_TABLE[0][function])
                instructionChain.add(("R${binaryChain.selectBit(8, 6)}"))
                instructionChain.add(("R${binaryChain.selectBit(11, 9)}"))
                instructionChain.add((binaryChain.selectBit(5, 0).toBinTwosComplement(6).twsBinaryToDec().toString()))
            }
            in 8..10 -> {
                instructionChain.add(DATA.SISA_TABLE[binaryChain.selectBit(8, 8)][function])
                instructionChain.add(("R${binaryChain.selectBit(11, 9)}"))
                instructionChain.add(binaryChain.selectBit(7, 0).toBinTwosComplement(6).twsBinaryToDec().toString())
            }
        }
        val code = getID(instructionChain[0], DATA.SISA_TABLE)!!

        return getConstruct(instructionChain)
    }

    fun getConstruct(chain: MutableList<String>): String {
        return when (chain[0]){
            "LD", "LDB" -> chain[0] + " " + chain[1] + ", " + chain[3] + '(' + chain[2] + ')'
            "ST", "STB" -> chain[0] + " " + chain[3] + '(' + chain[2] + ')'  + ", " + chain[1]
            else -> chain.removeFirst() + " " + chain.joinToString(", ")
        }
    }
}
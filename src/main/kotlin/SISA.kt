class SISA : BasicALU() {

    override var referenceTable: Array<Array<String>> = DATA.SISA_TABLE

    fun hexaParser(s: String): String {
        val binaryChain = toBinary(parseInput(s).first[0].value, 16)
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
                instructionChain.add((binaryChain.selectBit(5, 0).toString(radix = 16).uppercase()))
            }
            in 8..10 -> {
                println(function)
                instructionChain.add(DATA.SISA_TABLE[binaryChain.selectBit(8, 8)][function])
                instructionChain.add(("R${binaryChain.selectBit(11, 9)}"))
                instructionChain.add(binaryChain.selectBit(7, 0).toString(radix = 16).uppercase())
            }
        }
        return instructionChain.joinToString(" ")
    }
}
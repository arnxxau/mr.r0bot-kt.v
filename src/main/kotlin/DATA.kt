object DATA {
    val ALU_TABLE: Array<Array<String>> = arrayOf(
        arrayOf("AND", "CMPLT", "MOVE"),
        arrayOf("OR", "CMPLE", "MOVEI"),
        arrayOf("XOR", "---", "MOVHI"),
        arrayOf("NOT", "CMPEQ"),
        arrayOf("ADD", "CMPLTU"),
        arrayOf("SUB", "CMPLEU"),
        arrayOf("SHA"),
        arrayOf("SHL"),
        arrayOf("IN"),
        arrayOf("OUT")
    )

    val SISA_TABLE: Array<Array<String>> = arrayOf(
        arrayOf("AND", "CMPLT", "ADDI", "LD", "ST", "LDB", "STB", "JALR", "BZ", "MOVI", "IN"),
        arrayOf("OR", "CMPLE", "MOVEI", "---", "---", "---", "---", "---", "BNZ", "MOVHI", "OUT"),
        arrayOf("XOR"),
        arrayOf("NOT", "CMPEQ"),
        arrayOf("ADD", "CMPLTU"),
        arrayOf("SUB", "CMPLEU"),
        arrayOf("SHA"),
        arrayOf("SHL")
    )

    val SISA_LOGIC_MAP: Map<Pair<Int, Int>, String> = mapOf(
        Pair(0, 0) to "000001x10000xx0xxx00",
        Pair(0, 1) to "000001x10001xx0xxx00",
        Pair(0, 2) to "000001x0000000110001",
        Pair(0, 3) to "00000100010000110001",
        Pair(0, 4) to "00100000xx00001100xx",
        Pair(0, 5) to "00000110010000110001",
        Pair(0, 6) to "00100010xx00001100xx",
        Pair(0, 7) to "000000xxxxxxxxxxxxxx",
        Pair(0, 8) to "010000xxxx10101000xx",
        Pair(1, 8) to "100000xxxx10101000xx",
        Pair(0, 9) to "000001x0001001100110",
        Pair(1, 9) to "000001x0001001101010",
        Pair(0, 10) to "000101xx10xxxxxxxx10",
        Pair(1, 10) to "000010xxxxxxxxxxxxxx"
    )

    val ALU_CONSTRUCTOR: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 3, Utils.Type.A),
        Utils.DataSlot(0, 3, Utils.Type.B),
        Utils.DataSlot(0, 1, Utils.Type.RbN),
        Utils.DataSlot(0, 3, Utils.Type.OP),
        Utils.DataSlot(0, 2, Utils.Type.F),
        Utils.DataSlot(0, 1, Utils.Type.InAlu),
        Utils.DataSlot(0, 3, Utils.Type.D),
        Utils.DataSlot(0, 1, Utils.Type.WrD, false),
        Utils.DataSlot(0, 16, Utils.Type.N, toHex = true)
    )

    val SISA_CONSTRUCTOR: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 3, Utils.Type.A), // REG@A value
        Utils.DataSlot(0, 3, Utils.Type.B), // REG@B value
        Utils.DataSlot(0, 1, Utils.Type.RbN), // 1 if 'N' is used
        Utils.DataSlot(0, 2, Utils.Type.OP), // ALU operation value
        Utils.DataSlot(0, 3, Utils.Type.F), // ALU function value
        Utils.DataSlot(0, 2, Utils.Type.ila), // 0 to save from REGFILE (ALU), 1 to read RAM, 2 to read from ports
        Utils.DataSlot(0, 3, Utils.Type.D), // REG@D value
        Utils.DataSlot(0, 1, Utils.Type.WrD), // 1 to load permission
        Utils.DataSlot(0, 1, Utils.Type.WrOut), // 1 if an OUT instruction is executed
        Utils.DataSlot(0, 1, Utils.Type.RdIn), // 1 if an IN instruction is executed
        Utils.DataSlot(0, 1, Utils.Type.WrMem), // When ST or STB is executed (store byte)
        Utils.DataSlot(0, 1, Utils.Type.Byte), // 1 when a 'B' instruction is executed
        Utils.DataSlot(0, 1, Utils.Type.TknBr), // 1 when BZ or BZN is executed
        Utils.DataSlot(0, 16, Utils.Type.N, toHex = true), // N value
        Utils.DataSlot(0, 8, Utils.Type.ADDRIO, toHex = true) // Port value
    )

    // From 0 to 1
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE0: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 4, Utils.Type.C),
        Utils.DataSlot(0, 3, Utils.Type.A),
        Utils.DataSlot(0, 3, Utils.Type.B),
        Utils.DataSlot(0, 3, Utils.Type.D),
        Utils.DataSlot(0, 3, Utils.Type.F),
    )

    // From 2 to 7
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_B: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 4, Utils.Type.C),
        Utils.DataSlot(0, 3, Utils.Type.A),
        Utils.DataSlot(0, 3, Utils.Type.B),
        Utils.DataSlot(0, 6, Utils.Type.N),
    )
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_D: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 4, Utils.Type.C),
        Utils.DataSlot(0, 3, Utils.Type.A),
        Utils.DataSlot(0, 3, Utils.Type.D),
        Utils.DataSlot(0, 6, Utils.Type.N),
    )


    // From 8 to 10
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 4, Utils.Type.C),
        Utils.DataSlot(0, 3, Utils.Type.A),
        Utils.DataSlot(0, 1, Utils.Type.E),
        Utils.DataSlot(0, 8, Utils.Type.N),
    )
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D: Array<Utils.DataSlot> = arrayOf(
        Utils.DataSlot(0, 4, Utils.Type.C),
        Utils.DataSlot(0, 3, Utils.Type.D),
        Utils.DataSlot(0, 1, Utils.Type.E),
        Utils.DataSlot(0, 8, Utils.Type.N),
    )
}
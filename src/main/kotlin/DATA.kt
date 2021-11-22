object DATA {

    val ALU_TABLE: Array<Array<String>> = arrayOf(
        arrayOf("AND", "CMPLT", "MOVE"),
        arrayOf("OR", "COMPLE", "MOVEI"),
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

    val SISA_LOGIC_MAP: Map<String, String> = mapOf(
        "00000" to "000001x10000xx0xxx00",
        "00001" to "000001x10001xx0xxx00",
        "00010" to "000001x0000000110001",
        "00011" to "00000100010000110001",
        "00100" to "00100000xx00001100xx",
        "00101" to "00000110010000110001",
        "00110" to "00100010xx00001100xx",
        "00111" to "000000xxxxxxxxxxxxxx",
        "10000" to "010000xxxx10101000xx",
        "10001" to "100000xxxx10101000xx",
        "10010" to "000001x0001001100110",
        "10011" to "000001x0001001101010",
        "10100" to "000101xx10xxxxxxxx10",
        "10101" to "000010xxxxxxxxxxxxxx"
    )

    val ALU_CONSTRUCTOR: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.RbN),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.OP),
        BasicALU.DataSlot(0, 2, true, BasicALU.Type.F),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.InAlu),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.WrD),
        BasicALU.DataSlot(0, 16, true, BasicALU.Type.N)
    )

    val SISA_CONSTRUCTOR: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.RbN),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.OP),
        BasicALU.DataSlot(0, 2, true, BasicALU.Type.F),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.ila),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.WrD),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.WrOut),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.RdIn),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.WrMem),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.Byte),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.TknBr),
        BasicALU.DataSlot(0, 16, true, BasicALU.Type.N),
        BasicALU.DataSlot(0, 1, false, BasicALU.Type.ADDRIO)
    )

    //from 0 to 1
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE0: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.C),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 2, true, BasicALU.Type.F),
    )

    //from 2 to 7
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_B: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.C),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 6, true, BasicALU.Type.N),
    )

    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE1_D: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.C),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 6, true, BasicALU.Type.N),
    )


    //from 8 to 10
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_A: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.C),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.E),
        BasicALU.DataSlot(0, 8, true, BasicALU.Type.N),
    )
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE2_D: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.C),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.E),
        BasicALU.DataSlot(0, 8, true, BasicALU.Type.N),
    )
}
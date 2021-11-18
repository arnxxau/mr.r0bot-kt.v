object DATA {

    val ALU_TABLE: Array<Array<String>> = arrayOf(
        arrayOf("AND", "CMPLT", "MOVE"),
        arrayOf("OR", "COMPLE", "MOVEI"),
        arrayOf("XOR"),
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
        arrayOf("OR", "COMPLE", "MOVEI", "---", "---", "---", "---", "---", "BNZ", "MOVHI", "OUT"),
        arrayOf("XOR"),
        arrayOf("NOT", "CMPEQ"),
        arrayOf("ADD", "CMPLTU"),
        arrayOf("SUB", "CMPLEU"),
        arrayOf("SHA"),
        arrayOf("SHL")
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

    //from 0 to 1
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE0: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.F),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.D),
        BasicALU.DataSlot(0, 2, true, BasicALU.Type.F),
    )

    //from 2 to 7
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE1: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.F),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 6, true, BasicALU.Type.N),
    )

    //from 8 to 10
    val SISA_ASSEMBLY_CONSTRUCTOR_TYPE2: Array<BasicALU.DataSlot> = arrayOf(
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.F),
        BasicALU.DataSlot(0, 3, true, BasicALU.Type.A),
        BasicALU.DataSlot(0, 1, true, BasicALU.Type.B),
        BasicALU.DataSlot(0, 8, true, BasicALU.Type.N),
    )
}
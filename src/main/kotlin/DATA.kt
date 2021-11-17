object DATA {
    val ALU_TABLE: Array<Array<String>> = arrayOf(
        arrayOf("---", "X", "CMPLT", "AND"),
        arrayOf("---", "Y", "COMPLE", "OR"),
        arrayOf("---", "MOVHI", "---", "XOR"),
        arrayOf("---", "---", "CMPEQ", "NOT"),
        arrayOf("---", "---", "CMPLTU", "ADD"),
        arrayOf("---", "---", "CMPLEU", "SUB"),
        arrayOf("---", "---", "---", "SHA"),
        arrayOf("---", "---", "---", "SHL")
    )

    var ALU_CONSTRUCTOR: Array<BasicALU.DataSlot> = arrayOf(
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

}
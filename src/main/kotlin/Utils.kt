import de.vandermeer.asciitable.AsciiTable

open class Utils {
    open val dataArray = mutableListOf<String>()


    fun String.selectBit(startIndex: Int, endIndex: Int): Int {
        return this.substring(this.length - startIndex - 1,
            this.length - endIndex).toInt(radix = 2)
    }

    fun toBinary(x: Int, len: Int): String {
        return String.format(
            "%" + len + "s",
            Integer.toBinaryString(x)
        ).replace(" ".toRegex(), "0")
    }

    open fun printTable(aluCommand: Array<BasicALU.DataSlot>) {
        val table = AsciiTable()
        val stringArray = mutableListOf<String>()
        table.addRule()
        table.addRow(dataArray)
        for (c in aluCommand) {
            if (c.discard) {
                var s = ""
                for (i in 0 until c.nBit) s += "X"
                stringArray.add(s)
            }
            else
                stringArray.add(toBinary(c.value, c.nBit))
        }
        table.addRule()
        table.addRow(stringArray)
        println(table.render())
    }
}
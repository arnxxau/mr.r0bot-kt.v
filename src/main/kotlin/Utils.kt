import de.vandermeer.asciitable.AsciiTable
import kotlin.math.*
open class Utils {
    open val dataArray = mutableListOf<String>()

    // Selects a range of bits and returns its unsigned decimal representation
    fun String.selectBit(startIndex: Int, endIndex: Int): Int {
        return this.substring(this.length - startIndex - 1,
            this.length - endIndex).toInt(radix = 2)
    }

    // Converts a decimal Int into a binary string chain represented in Two's Complement
    fun Int.toBinTwosComplement(len: Int): String {
        var x = this
        val toInsert: String
        val binChain = if (x < 0) {
            toInsert = "1"
            var inv = ""
            ++x; x *= -1
            for (bit in x.toString(radix = 2)) {
                inv += if (bit == '0') '1'
                        else '0'
            }
            inv
        }
        else {
            toInsert = "0"
            x.toString(radix = 2)
        }
        var inserted = ""
        for (i in 0 until len - binChain.length) inserted += toInsert[0]
        return inserted + binChain
    }

    // Converts a decimal Int into a hex string chain represented in Two's Complement
    fun Int.toHexTwosComplement(len: Int): String {
        val hexChain = this.toBinTwosComplement(len*4).toInt(radix = 2).toString(radix = 16).uppercase()
        var insert = ""
        if (this > 0) for (i in 0 until len - hexChain.length) insert += '0'
        return insert + hexChain
    }

    // Converts a decimal string chain into its decimal Int represented in Two's Complement
    fun String.twsBinaryToDec(): Int {
        val n = this.toInt(radix = 2)
        return if (this[0] == '1') (n - 2.0.pow(this.length)).toInt()
        else n
    }

    // Converts a decimal Int into an unsigned binary chain
    fun Int.toBinary(len: Int): String {
        return String.format(
            "%" + len + "s",
            this.toString(radix = 2)
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
                stringArray.add(c.value.toBinTwosComplement(c.nBit))
        }
        table.addRule()
        table.addRow(stringArray)
        println(table.render())
    }
}



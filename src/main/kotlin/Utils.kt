import de.vandermeer.asciitable.AsciiTable
import kotlin.math.*
open class Utils {

    // Selects a range of bits and returns its unsigned decimal representation
    fun String.selectBits(startIndex: Int, endIndex: Int): Int? {
        return this.substring(this.length - startIndex - 1,
            this.length - endIndex).toIntOrNull(radix = 2)
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

    // Prints the control phrase in an ascii table format
    fun printTable(dataArray: MutableList<String>, aluCommand: Array<DataSlot>) {
        val table = AsciiTable()
        val stringArray = mutableListOf<String>()
        table.addRule()
        table.addRow(dataArray)
        for (c in aluCommand) {
            if (c.discard) {
                var s = ""
                val div = if (c.toHex) 4
                            else 1
                for (i in 0 until c.nBit/div) s += "X"
                stringArray.add(s)
            }
            else if (c.toHex) stringArray.add(c.value.toHexTwosComplement(c.nBit/4))
            else stringArray.add(c.value.toBinTwosComplement(c.nBit))
        }
        table.addRule()
        table.addRow(stringArray)
        table.addRule()
        //table.context.width = 130
        println(table.render())
    }

    // Checks if a REG has been already parsed
    fun verifyParse(idx: Int, input: MutableList<DataSlot>): Boolean {
        for (data in input) {
            if (data.type == selectRegister(idx)) return true
        }
        return false
    }

    // Encrypts data from DataSlot to binary format (machine language)
    fun encryptAssembly(construct: Array<DataSlot>): String {
        var output = ""
        for (slot in construct) {
            output += slot.value.toBinTwosComplement(slot.nBit)
        }
        return output
    }

    // Returns a pair representing the row and column of the inputted array of arrays
    fun getID(s: String, toSearch: Array<Array<String>>): Pair<Int, Int>? {
        for (i in toSearch.indices){
            for (j in toSearch[i].indices) {
                if (s == toSearch[i][j]) return Pair(i, j)
            }
        }
        return null
    }

    // Returns the binary chain combining all the inputted dataSlots
    fun generateBinaryChain(slots: Array<DataSlot>): String {
        var binChain = ""
        val discard = 0
        for (slot in slots) {
            binChain += if (slot.discard) discard.toBinary(slot.nBit)
            else if (slot.type == Type.N) slot.value.toBinTwosComplement(slot.nBit)
            else slot.value.toBinary(slot.nBit)
        }
        return binChain
    }

    data class DataSlot (
        var value: Int,
        var nBit: Int,
        var type: Type,
        var discard: Boolean = true,
        var toHex: Boolean = false
    )

    enum class Type {
        A, B, RbN, OP, F, InAlu,  ila, D, WrD, WrOut, RdIn, WrMem, Byte, TknBr, N, UNKNOWN, C, E, ADDRIO
    }

    fun selectRegister(idx: Int): Type {
        return when (idx) {
            1 -> Type.D
            2 -> Type.A
            3 -> Type.B
            else -> Type.UNKNOWN
        }
    }
}



package org.example

class FSM {
    fun output(inputList: List<Boolean>): Boolean {
        var currentstate = true // true = VALID, false = INVALID

        for (i in inputList) {
            currentstate = when (currentstate) {
                true -> if (i) true else false
                false -> false
            }
        }
        return currentstate
    }
}

class AndGate private constructor(val name: String, val inputs: List<Boolean>, val evaluator: FSM) {
    fun getOutput(): Boolean {
        return evaluator.output(inputs)
    }

    override fun toString(): String {
        return "AndGate: $name cu intrarile: $inputs"
    }

    class Builder {
        private var name: String = "Unnamed_AND"
        private val inputs = mutableListOf<Boolean>()

        fun setName(name: String):Builder {
            this.name = name
            return this
        }

        fun addInput(inputValue: Boolean):Builder {
            this.inputs.add(inputValue)
            return this
        }

        fun build(): AndGate {
            val validSizes = listOf(2, 3, 4, 8)

            for (i in validSizes) {

                if (i == inputs.size) {

                    return AndGate(name, inputs, FSM())

                }

            }
            throw IllegalArgumentException("Invalid inputs")
        }

    }
}

fun main(args: Array<String>) {
    val poarta = AndGate.Builder()
        .setName("PoartaAND_Test")
        .addInput(true)
        .addInput(true)
        .addInput(true)
        .build()

    println(poarta)
    println("Out: " + poarta.getOutput())
}
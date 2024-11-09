package store.model.service

class InputService {
    fun parseInput(input: String): List<Pair<String, Int>> {
        val inputList = input.split(" ")
        return inputList.map {
            val (itemName, quantity) = it.trim().removeSurrounding("[", "]").split(",")
            itemName to quantity.toInt()
        }
    }
}
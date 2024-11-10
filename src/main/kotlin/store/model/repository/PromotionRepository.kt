package store.model.repository

import store.model.domain.Promotion
import util.FileLoader
import java.time.LocalDate

class PromotionRepository(filePath: String) {
    private val promotions = mutableListOf<Promotion>()

    init {
        val lines = FileLoader.loadLines(filePath)
        lines.drop(1).forEach { line ->
            val promotion = parsePromotion(line)
            promotions.add(promotion)
        }
    }

    fun findByName(promotionName: String): Promotion? {
        return promotions.find { it.name == promotionName }
    }

    fun getAll(): List<Promotion> {
        return promotions
    }

    private fun parsePromotion(line: String): Promotion {
        val tokens = line.split(",")

        val name = tokens[0].trim()
        val requiredQuantity = tokens[1].trim().toInt()
        val rewardQuantity = tokens[2].trim().toInt()
        val startDate = parseDate(tokens[2].trim())
        val endDate = parseDate(tokens[3].trim())

        return Promotion(name, requiredQuantity, rewardQuantity, startDate, endDate)
    }

    private fun parseDate(dateString: String): LocalDate {
        val year = dateString.substring(0, 4).toInt()
        val month = dateString.substring(4, 6).toInt()
        val day = dateString.substring(6, 8).toInt()
        return LocalDate.of(year, month, day)
    }


}
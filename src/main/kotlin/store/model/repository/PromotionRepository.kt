package store.model.repository

import store.model.domain.Promotion
import store.model.domain.PromotionType
import store.values.ErrorMessages
import util.FileLoader
import java.time.LocalDate

class PromotionRepository(filePath: String) {
    private val promotions = mutableListOf<Promotion>()

    init {
        val lines = FileLoader.loadLines(filePath)
        lines.drop(1)
        lines.forEach { line ->
            val promotion = parsePromotion(line)
            promotions.add(promotion)
        }
    }

    fun findByName(promotionName: String): Promotion {
        val promotion = promotions.find { it.name == promotionName }
        if (promotion == null) {
            throw IllegalArgumentException(ErrorMessages.NON_EXISTING_PROMOTION)
        }
        return promotion
    }

    fun getAll(): List<Promotion> {
        return promotions
    }

    fun setStock(promotionName: String, quantity: Int) {
        val promotion = findByName(promotionName)
        promotion.stock = quantity
    }

    fun getStock(name: String): Int {
        val promotion = findByName(name)
        return promotion.stock
    }

    private fun parsePromotion(line: String): Promotion {
        val tokens = line.split(",")

        val name = tokens[0].trim()
        val type = PromotionType.fromString(tokens[1].trim())
        val startDate = parseDate(tokens[2].trim())
        val endDate = parseDate(tokens[3].trim())
        val promotionStock = tokens[4].trim().toInt()

        return Promotion(name, type, startDate, endDate, promotionStock)
    }

    private fun parseDate(dateString: String): LocalDate {
        val year = dateString.substring(0, 4).toInt()
        val month = dateString.substring(4, 6).toInt()
        val day = dateString.substring(6, 8).toInt()
        return LocalDate.of(year, month, day)
    }


}
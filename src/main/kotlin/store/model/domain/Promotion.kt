package store.model.domain

import java.time.LocalDate

data class Promotion(
    val name: String,
    val requiredQuantity: Int,
    val giveawayQuantity: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
)

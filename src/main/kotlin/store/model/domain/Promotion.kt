package store.model.domain

import java.time.LocalDate

data class Promotion(
    val productName: String,
    val type: PromotionType,
    val startDate: LocalDate,
    val endDate: LocalDate,
    var stock: Int
)

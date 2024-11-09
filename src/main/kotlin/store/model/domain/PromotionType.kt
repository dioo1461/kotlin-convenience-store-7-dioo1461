package store.model.domain

enum class PromotionType(val additionalOfferingQuantity: Int) {
    NONE(0),
    ONE_PLUS_ONE(1),
    TWO_PLUS_ONE(2);

    companion object {
        fun fromString(type: String): PromotionType {
            return when (type) {
                "1+1" -> ONE_PLUS_ONE
                "2+1" -> TWO_PLUS_ONE
                else -> NONE
            }
        }
    }
}
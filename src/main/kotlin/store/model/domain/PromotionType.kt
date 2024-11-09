package store.model.domain

enum class PromotionType(val additionalOfferingQuantity: Int) {
    NONE(0),
    ONE_PLUS_ONE(1),
    TWO_PLUS_ONE(2);
}
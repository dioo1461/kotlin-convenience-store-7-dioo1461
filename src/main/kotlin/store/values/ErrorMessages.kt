package store.values


object ErrorMessages {
    const val INVALID_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."
    const val PRODUCT_NOT_FOUND = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
    const val QUANTITY_EXCEEDS_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."
    const val INVALID_INPUT = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."
    
    const val PROMOTION_NOT_FOUND = "[ERROR] 존재하지 않는 프로모션에 접근했습니다."
    const val NORMAL_STOCK_CANNOT_BE_NEGATIVE = "[ERROR] 상품의 재고 수량은 음수가 될 수 없습니다."
    const val PROMOTION_STOCK_CANNOT_BE_NEGATIVE = "[ERROR] 프로모션 상품의 재고 수량은 음수가 될 수 없습니다."
}

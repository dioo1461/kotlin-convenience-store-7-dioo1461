package store.values

object Messages {
    const val WELCOME = "\n안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"
    const val PROMPT_PURCHASE_ITEM = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
    const val LACK_OF_PROMOTION_STOCK = "\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
    const val ASK_FOR_EXTRA_GIVEAWAYS = "\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
    const val ASK_MEMBERSHIP_DISCOUNT = "\n멤버십 할인을 받으시겠습니까? (Y/N)"
    const val ASK_PURCHASE_AGAIN = "\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"
}
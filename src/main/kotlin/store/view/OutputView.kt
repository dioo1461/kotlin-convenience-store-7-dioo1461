package store.view

import store.model.domain.Product
import store.model.domain.Receipt
import store.values.Messages

object OutputView {
    fun printWelcomeMessage() {
        println(Messages.WELCOME)
    }

    fun printProductList(products: List<Product>) {
        products.forEach { product ->
            val formattedStock = formatStock(product.stock)
            if (product.promotionName != null) {
                val formattedPromotionStock = formatStock(product.promotionStock)
                println("- ${product.name} ${"%,d".format(product.price)}원 $formattedPromotionStock ${product.promotionName}")
            }
            println("- ${product.name} ${"%,d".format(product.price)}원 $formattedStock")
        }
    }

    fun printReceipt(receipt: Receipt) {
        println("\n============== W 편의점 ================")
        println(String.format("%-12s %5s %10s", "상품명", "수량", "금액"))
        receipt.purchasedItems.forEach { item ->
            println(String.format("%-12s %5d %,10d", item.name, item.quantity, item.unitPrice * item.quantity))
        }
        println("============= 증    정 ===============")
        receipt.giftedItems.forEach { gift ->
            if (gift.quantity > 0)
                println(String.format("%-12s %5d", gift.name, gift.quantity))
        }
        println("======================================")
        println(String.format("%-12s %5d %,10d", "총구매액", receipt.totalQuantity, receipt.totalPrice))
        println(String.format("%-12s %10s %,10d", "행사할인", "", -receipt.promotionDiscountAmount))
        println(String.format("%-12s %10s %,10d", "멤버십할인", "", -receipt.membershipDiscountAmount))
        println(String.format("%-12s %10s %,10d", "내실돈", "", receipt.finalPrice))
    }


    private fun formatStock(stock: Int): String {
        return if (stock > 0) "${stock}개" else "재고 없음"
    }

}
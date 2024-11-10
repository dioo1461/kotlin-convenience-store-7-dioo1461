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
            val stockInfo = getStockInfo(product.stock)
            val promotionInfo = product.promotionName ?: ""
            println("${product.name} ${"%,d".format(product.price)}원 $stockInfo $promotionInfo")
        }
    }

    fun printReceipt(receipt: Receipt) {
        println("==============W 편의점================")
        println("상품명\t\t수량\t금액")
        receipt.purchasedItems.forEach { item ->
            println("${item.name}\t\t${item.quantity}\t${item.price * item.quantity}")
        }
        if (receipt.giftedItems.isNotEmpty()) {
            println("=============증\t정===============")
            receipt.giftedItems.forEach { gift ->
                println("${gift.name}\t\t${gift.quantity}")
            }
        }
        println("====================================")
        println("총구매액\t\t${receipt.totalQuantity}\t${receipt.totalPrice}")
        println("행사할인\t\t\t-${receipt.promotionDiscountAmount}")
        println("멤버십할인\t\t\t-${receipt.membershipDiscountAmount}")
        println("내실돈\t\t\t${receipt.finalPrice}")
    }

    private fun getStockInfo(stock: Int): String {
        return if (stock > 0) "${stock}개" else "품절"
    }

}
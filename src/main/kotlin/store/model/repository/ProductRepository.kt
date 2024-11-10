package store.model.repository

import store.model.domain.Product
import store.values.ErrorMessages
import util.FileLoader

class ProductRepository(filePath: String) {
    private val products = mutableListOf<Product>()

    init {
        val lines = FileLoader.loadLines(filePath)
        lines.drop(1)
        lines.forEach { line ->
            parseProduct(line)
        }
    }

    fun findProduct(productName: String): Product? {
        return products.find { it.name == productName }
    }


    fun setStock(productName: String, quantity: Int) {
        val product = findProduct(productName)
        require(product != null) { ErrorMessages.PRODUCT_NOT_FOUND }
        product.stock = quantity
    }

    fun getStock(productName: String): Int {
        val product = findProduct(productName)
        require(product != null) { ErrorMessages.PRODUCT_NOT_FOUND }
        return product.stock
    }

    fun setPromotionStock(productName: String, quantity: Int) {
        val product = findProduct(productName)
        require(product != null) { ErrorMessages.PRODUCT_NOT_FOUND }
        require(product.promotionName != null) { ErrorMessages.PROMOTION_NOT_FOUND }
        product.promotionStock = quantity
    }

    fun getPromotionStock(productName: String): Int {
        val product = findProduct(productName)
        require(product != null) { ErrorMessages.PRODUCT_NOT_FOUND }
        require(product.promotionName != null) { ErrorMessages.PROMOTION_NOT_FOUND }
        return product.promotionStock
    }

    private fun parseProduct(line: String) {
        val tokens = line.split(",")
        val name = tokens[0].trim()
        val price = tokens[1].trim().toInt()
        val count = tokens[2].trim().toInt()
        val promotionName = tokens[3].trim()
        upsertProductList(name, price, count, promotionName)
    }

    private fun upsertProductList(name: String, price: Int, count: Int, promotionName: String) {
        val product = products.find { it.name == name }
        if (promotionName == "null") {
            if (product == null) {
                products.add(Product(name, price, count, null)) // 제품이 없으면 새로 추가
                return
            }
            product.stock = count
            return
        }
        if (product == null) {
            products.add(Product(name, price, count, promotionName)) // 제품이 없으면 새로 추가
            return
        }
        product.stock = count
        product.promotionName = promotionName
    }
}

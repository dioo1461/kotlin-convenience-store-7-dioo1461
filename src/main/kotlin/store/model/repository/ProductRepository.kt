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
            val product = parseProduct(line)
            products.add(product)
        }
    }

    fun findByName(productName: String): Product {
        val product = products.find { it.name == productName }
        if (product == null) {
            throw IllegalArgumentException(ErrorMessages.NON_EXISTING_PRODUCT)
        }
        return product
    }

    fun getAll(): List<Product> {
        return products
    }

    fun setStock(name: String, quantity: Int) {
        val product = findByName(name)
        product.stock = quantity
    }

    fun getStock(name: String): Int {
        val product = findByName(name)
        return product.stock
    }

    private fun parseProduct(line: String): Product {
        val tokens = line.split(",")
        val name = tokens[0].trim()
        val price = tokens[1].trim().toInt()
        val count = tokens[2].trim().toInt()
        val promotion = tokens[3].trim()
        
        if (promotion == "null") {
            return Product(name, price, count)
        }
        return Product(name, price, count, promotion)
    }
}

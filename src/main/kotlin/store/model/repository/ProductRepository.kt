package store.model.repository

import store.model.domain.Product
import store.util.FileLoader

class ProductRepository {
    private val products = mutableListOf<Product>()

    fun loadProducts() {
        val lines = FileLoader.loadLines("src/main/resources/products.md")
        lines.drop(1)
        lines.forEach { line ->
            val product = parseProduct(line)
            products.add(product)
        }
    }

    private fun parseProduct(line: String): Product {
        val tokens = line.split(",")

        val name = tokens[0].trim()
        val price = tokens[1].trim().toInt()
        val count = tokens[2].trim().toInt()

        return Product(name, price, count)
    }
}

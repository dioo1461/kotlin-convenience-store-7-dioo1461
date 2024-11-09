package store.model.repository

import store.model.domain.Product
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

    private fun parseProduct(line: String): Product {
        val tokens = line.split(",")

        val name = tokens[0].trim()
        val price = tokens[1].trim().toInt()
        val count = tokens[2].trim().toInt()

        return Product(name, price, count)
    }
}

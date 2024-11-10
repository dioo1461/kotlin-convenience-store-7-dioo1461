package store.model.domain

data class Product(
    val name: String,
    val price: Int,
    var stock: Int,
    var promotionName: String? = null,
    var promotionStock: Int = 0
)

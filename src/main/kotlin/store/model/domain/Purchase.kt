package store.model.domain

data class Purchase(
    val product: Product,
    var quantity: Int
)
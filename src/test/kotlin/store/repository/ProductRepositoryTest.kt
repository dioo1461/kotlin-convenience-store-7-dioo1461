import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.model.repository.ProductRepository
import java.io.File

class ProductRepositoryTest {

    private lateinit var testFile: File
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        testFile = File.createTempFile("test_products", ".md").apply {
            writeText(
                """
                name,price,quantity,promotion
                콜라,1000,5,탄산2+1
                콜라,1000,10,null
                사이다,1000,5,탄산2+1
                사이다,1000,10,null
                """.trimIndent()
            )
            deleteOnExit()
        }
        productRepository = ProductRepository(testFile.absolutePath) // ProductRepository 초기화
    }

    @Test
    fun `파일에서 제품을 올바르게 불러오는지 테스트`() {
        val products = productRepository.getAllProducts()
        assertEquals(2, products.size)
        assertEquals("콜라", products[0].name)
        assertEquals("사이다", products[1].name)
    }

    @Test
    fun `제품 이름으로 제품을 찾을 수 있는지 테스트`() {
        val product = productRepository.findProduct("콜라")
        assertNotNull(product)
        assertEquals("콜라", product?.name)
        assertEquals(1000, product?.price)
    }

    @Test
    fun `재고를 올바르게 가져오는지 테스트`() {
        val stock = productRepository.getStock("콜라")
        assertEquals(10, stock)
    }

    @Test
    fun `재고를 올바르게 설정하고 가져올 수 있는지 테스트`() {
        productRepository.setStock("콜라", 5)
        val stock = productRepository.getStock("콜라")
        assertEquals(5, stock)
    }

    @Test
    fun `프로모션 재고를 올바르게 가져오는지 테스트`() {
        val promotionStock = productRepository.getPromotionStock("콜라")
        assertEquals(5, promotionStock)
    }

    @Test
    fun `프로모션 재고를 설정하고 올바르게 가져오는지 테스트`() {
        productRepository.setPromotionStock("콜라", 1)
        val promotionStock = productRepository.getPromotionStock("콜라")
        assertEquals(1, promotionStock)
    }

    @Test
    fun `존재하지 않는 제품의 재고를 가져올 때 예외 발생`() {

    }
}

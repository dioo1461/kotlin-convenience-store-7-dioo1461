import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.model.domain.Product
import store.model.repository.ProductRepository
import store.model.service.ProductService
import store.values.ErrorMessages
import java.io.File

class ProductServiceTest {

    private lateinit var testFile: File
    private lateinit var productRepository: ProductRepository
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        testFile = File.createTempFile("mock_products", ".md").apply {
            writeText(
                """
                name,price,quantity,promotion
                콜라,1000,10,탄산2+1
                콜라,1000,10,null
                사이다,1000,1,탄산2+1
                사이다,1000,1,null
                """.trimIndent()
            )
            deleteOnExit()
        }
        productRepository = ProductRepository(testFile.absolutePath)
        productService = ProductService(productRepository)
    }

    @Test
    fun `decreaseStock - 정상적인 재고 감소`() {
        productService.decreaseStock("콜라", 5)
        assertEquals(5, productService.getStock("콜라"))
    }

    @Test
    fun `decreaseStock - 재고가 부족할 때 예외 발생`() {
        val exception = assertThrows<IllegalArgumentException> {
            productService.decreaseStock("콜라", 15)
        }
        assertEquals(ErrorMessages.NORMAL_STOCK_CANNOT_BE_NEGATIVE, exception.message)
    }

    @Test
    fun `decreasePromotionStock - 정상적인 프로모션 재고 감소`() {
        productService.decreasePromotionStock("콜라", 1)
        assertEquals(9, productService.getStock("콜라") - 1)
    }

    @Test
    fun `decreasePromotionStock - 프로모션 재고 부족시 예외 발생`() {
        val exception = assertThrows<IllegalArgumentException> {
            productService.decreasePromotionStock("콜라", 100)
        }
        assertEquals(ErrorMessages.PROMOTION_STOCK_CANNOT_BE_NEGATIVE, exception.message)
    }

    @Test
    fun `findProduct - 존재하는 제품 조회`() {
        val product = productService.findProduct("콜라")
        assertNotNull(product)
        assertEquals("콜라", product?.name)
    }

    @Test
    fun `findProduct - 존재하지 않는 제품 조회시 null 반환`() {
        val product = productService.findProduct("존재하지_않는_제품")
        assertNull(product)
    }

    @Test
    fun `getPromotionName - 제품의 프로모션 이름 가져오기`() {
        val promotionName = productService.getPromotionName("콜라")
        assertEquals("탄산2+1", promotionName)
    }

    @Test
    fun `getPromotionName - 존재하지 않는 제품의 프로모션 이름에 접근할 시 null 반환`() {
        val promotionName = productService.getPromotionName("존재하지_않는_제품")
        assertNull(promotionName)
    }

    @Test
    fun `checkStockAvailability - 충분한 재고가 있을 때 true 반환`() {
        assertTrue(productService.checkStockAvailability("콜라", 5))
    }

    @Test
    fun `checkStockAvailability - 재고가 부족할 때 false 반환`() {
        val isAvailable = productService.checkStockAvailability("콜라", 15)
        assertEquals(false, isAvailable)
    }

    @Test
    fun `checkPromotionStockAvailability - 충분한 프로모션 재고가 있을 때 true 반환`() {
        val isAvailable = productService.checkPromotionStockAvailability("콜라", 1)
        assertTrue(isAvailable)
    }

    @Test
    fun `checkPromotionStockAvailability - 프로모션 재고 부족 시 false 반환`() {
        val isAvailable = productService.checkPromotionStockAvailability("콜라", 15)
        assertEquals(false, isAvailable)
    }
}

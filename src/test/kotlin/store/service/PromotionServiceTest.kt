import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.model.repository.PromotionRepository
import store.model.service.PromotionService
import java.io.File
import java.time.LocalDateTime

class PromotionServiceTest {

    private lateinit var testFile: File
    private lateinit var promotionRepository: PromotionRepository
    private lateinit var promotionService: PromotionService

    @BeforeEach
    fun setUp() {
        testFile = File.createTempFile("test_promotions", ".md").apply {
            writeText(
                """
                name,requiredQuantity,rewardQuantity,startDate,endDate
                유효한_프로모션,2,1,2024-01-01,2024-12-31
                만료된_프로모션,3,1,2023-01-01,2023-12-31
                """.trimIndent()
            )
            deleteOnExit()
        }
        promotionRepository = PromotionRepository(testFile.absolutePath)
        promotionService = PromotionService(promotionRepository)
    }

    @Test
    fun `checkIsNotExpired - 유효한 프로모션에 대해 테스트`() {
        val date = LocalDateTime.of(2024, 6, 15, 0, 0)
        val availability = promotionService.checkIsNotExpired("유효한_프로모션", date)
        assertTrue(availability)
    }

    @Test
    fun `checkIsExpired - 만료된 프로모션에 대해 테스트`() {
        val date = LocalDateTime.of(2024, 6, 1, 0, 0)
        val availability = promotionService.checkIsNotExpired("만료된_프로모션", date)
        assertFalse(availability)
    }

    @Test
    fun `findPromotion - 유효한 프로모션을 올바르게 가져오는지 테스트`() {
        val promotion = promotionService.findPromotion("유효한_프로모션")
        assertNotNull(promotion)
        assertEquals("유효한_프로모션", promotion?.name)
        assertEquals(2, promotion?.requiredQuantity)
        assertEquals(1, promotion?.giveawayQuantity)
    }


    @Test
    fun `findPromotion - 존재하지 않는 프로모션 조회시 null 반환`() {
        val promotion = promotionService.findPromotion("존재하지_않는_프로모션")
        assertNull(promotion)
    }
}

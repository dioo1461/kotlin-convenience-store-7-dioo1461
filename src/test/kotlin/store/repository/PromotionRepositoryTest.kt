import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.model.domain.Promotion
import store.model.repository.PromotionRepository
import java.io.File
import java.time.LocalDate

class PromotionRepositoryTest {

    private lateinit var testFile: File
    private lateinit var promotionRepository: PromotionRepository

    @BeforeEach
    fun setUp() {
        // 임시 파일 생성 및 내용 작성
        testFile = File.createTempFile("test_promotions", ".md").apply {
            writeText(
                """
                name,buy,get,start_date,end_date
                탄산2+1,2,1,20240101,20241231
                반짝할인,1,1,20240101,20240131
                """.trimIndent()
            )
            deleteOnExit() // 테스트 후 임시 파일 삭제
        }
        promotionRepository = PromotionRepository(testFile.absolutePath)
    }

    @Test
    fun `파일에서 프로모션 목록을 불러온다`() {
        val promotions = promotionRepository.getAll()
        assertEquals(2, promotions.size)
        assertEquals("탄산2+1", promotions[0].name)
        assertEquals("반짝할인", promotions[1].name)
    }

    @Test
    fun `findByName으로 프로모션 이름을 검색한다`() {
        val promotion = promotionRepository.findByName("탄산2+1")
        assertNotNull(promotion)
        assertEquals("탄산2+1", promotion?.name)
        assertEquals(2, promotion?.requiredQuantity)
        assertEquals(1, promotion?.giveawayQuantity)
    }

    @Test
    fun `findByName으로 존재하지 않는 프로모션 이름을 검색하면 null을 반환`() {
        val promotion = promotionRepository.findByName("없는프로모션")
        assertNull(promotion)
    }

    @Test
    fun `프로모션 시작일과 종료일을 올바르게 파싱한다`() {
        val promotion = promotionRepository.findByName("탄산2+1")
        assertNotNull(promotion)
        assertEquals(LocalDate.of(2024, 1, 1), promotion?.startDate)
        assertEquals(LocalDate.of(2024, 12, 31), promotion?.endDate)
    }
}

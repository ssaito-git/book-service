package bookservice.webapi.integration.controller.book

import bookservice.webapi.AbstractDbTest
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.nio.charset.StandardCharsets

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DBUnit(caseSensitiveTableNames = true)
class GetBookApiIntegrationTest @Autowired constructor(val mockMvc: MockMvc) : AbstractDbTest() {
    @Test
    @DataSet("datasets/integration/getbookapi/books.yml")
    fun `検索条件なし`() {
        mockMvc.get("/books").andExpectAll {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    @DataSet("datasets/integration/getbookapi/books.yml")
    fun `検索条件に一致する書籍がない`() {
        val result = mockMvc.get("/books?title={title}", "存在しないタイトル").andExpectAll {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val content = result.response.getContentAsString(StandardCharsets.UTF_8)

        val expectedResponseBody = """{"books":[],"hasMore":false}"""

        assertEquals(
            expectedResponseBody,
            content,
        )
    }
}

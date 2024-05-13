package testdata

import org.apache.commons.csv.CSVFormat
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import samplespringboot.db.jooq.tables.references.AUTHORS
import samplespringboot.db.jooq.tables.references.BOOKS
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.Reader
import java.nio.file.Paths
import java.util.UUID

class R__ImportTestData : BaseJavaMigration() {
    override fun migrate(context: Context) {
        val dslContext = DSL.using(context.connection, SQLDialect.POSTGRES)

        try {
            val filepath = this::class.java.classLoader.getResource("testdata/list_person_all_utf8.csv")?.toURI()
                ?.let { Paths.get(it) }

            FileReader(filepath.toString()).use { reader ->
                processCsv(dslContext, reader)
            }
        } catch (ex: FileNotFoundException) {
            println("ファイルが存在しないのでインポートをスキップします。[${ex.message}]")
        }
    }

    private fun processCsv(dslContext: DSLContext, reader: Reader) {
        val csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader(
                "人物ID",
                "著者名",
                "作品ID",
                "作品名",
                "仮名遣い種別",
                "翻訳者名等",
                "入力者名",
                "校正者名",
                "状態",
                "状態の開始日",
                "底本名",
                "出版社名",
                "入力に使用した版",
                "校正に使用した版",
            )
            .setSkipHeaderRecord(true)
            .build()

        csvFormat.parse(reader).use { csvParser ->
            data class Author(val id: UUID, val name: String)

            val authors = mutableMapOf<String, Author>()

            for (record in csvParser) {
                val authorId = record.get("人物ID")
                val authorName = record.get("著者名")
                val bookTitle = record.get("作品名")
                val publisherName = record.get("出版社名")

                val author = authors.computeIfAbsent(authorId) {
                    val id = UUID.randomUUID()
                    dslContext.insertInto(AUTHORS, AUTHORS.ID, AUTHORS.NAME)
                        .values(id, authorName)
                        .execute()
                    Author(id, authorName)
                }

                val bookId = UUID.randomUUID()

                dslContext.insertInto(BOOKS, BOOKS.ID, BOOKS.AUTHOR_ID, BOOKS.TITLE, BOOKS.PUBLISHER_NAME)
                    .values(bookId, author.id, bookTitle, publisherName)
                    .execute()
            }
        }
    }
}

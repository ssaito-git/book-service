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
import java.time.LocalDate
import java.util.UUID

class R__ImportTestData : BaseJavaMigration() {
    override fun migrate(context: Context) {
        val dslContext = DSL.using(context.connection, SQLDialect.POSTGRES)

        try {
            val filepath =
                this::class.java.classLoader.getResource("testdata/list_person_all_extended_utf8.csv")?.toURI()
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
            .setHeader(*CSV_HEADER)
            .setSkipHeaderRecord(true)
            .build()

        csvFormat.parse(reader).use { csvParser ->
            val authors = mutableMapOf<String, UUID>()

            for (record in csvParser) {
                val authorId = record.get("人物ID")
                val authorName = record.get("姓") + record.get("名")
                val authorNameKana = record.get("姓読み") + record.get("名読み")
                val authorBirthDate = runCatching { LocalDate.parse(record.get("生年月日")) }.getOrNull()
                val authorDeathDate = runCatching { LocalDate.parse(record.get("没年月日")) }.getOrNull()
                val bookTitle = record.get("作品名")
                val bookTitleKana = record.get("作品名読み")
                val publisherName = record.get("底本出版社名1")

                val authorIdForDatabase = authors.computeIfAbsent(authorId) {
                    val id = UUID.randomUUID()
                    dslContext.insertInto(
                        AUTHORS,
                        AUTHORS.ID,
                        AUTHORS.NAME,
                        AUTHORS.NAME_KANA,
                        AUTHORS.BIRTH_DATE,
                        AUTHORS.DEATH_DATE,
                    )
                        .values(id, authorName, authorNameKana, authorBirthDate, authorDeathDate)
                        .execute()
                    id
                }

                val bookId = UUID.randomUUID()

                dslContext.insertInto(
                    BOOKS,
                    BOOKS.ID,
                    BOOKS.AUTHOR_ID,
                    BOOKS.TITLE,
                    BOOKS.TITLE_KANA,
                    BOOKS.PUBLISHER_NAME,
                )
                    .values(bookId, authorIdForDatabase, bookTitle, bookTitleKana, publisherName)
                    .execute()
            }
        }
    }

    companion object {
        val CSV_HEADER = arrayOf(
            "作品ID",
            "作品名",
            "作品名読み",
            "ソート用読み",
            "副題",
            "副題読み",
            "原題",
            "初出",
            "分類番号",
            "文字遣い種別",
            "作品著作権フラグ",
            "公開日",
            "最終更新日",
            "図書カードURL",
            "人物ID",
            "姓",
            "名",
            "姓読み",
            "名読み",
            "姓読みソート用",
            "名読みソート用",
            "姓ローマ字",
            "名ローマ字",
            "役割フラグ",
            "生年月日",
            "没年月日",
            "人物著作権フラグ",
            "底本名1",
            "底本出版社名1",
            "底本初版発行年1",
            "入力に使用した版1",
            "校正に使用した版1",
            "底本の親本名1",
            "底本の親本出版社名1",
            "底本の親本初版発行年1",
            "底本名2",
            "底本出版社名2",
            "底本初版発行年2",
            "入力に使用した版2",
            "校正に使用した版2",
            "底本の親本名2",
            "底本の親本出版社名2",
            "底本の親本初版発行年2",
            "入力者",
            "校正者",
            "テキストファイルURL",
            "テキストファイル最終更新日",
            "テキストファイル符号化方式",
            "テキストファイル文字集合",
            "テキストファイル修正回数",
            "XHTML/HTMLファイルURL",
            "XHTML/HTMLファイル最終更新日",
            "XHTML/HTMLファイル符号化方式",
            "XHTML/HTMLファイル文字集合",
            "XHTML/HTMLファイル修正回数",
        )
    }
}

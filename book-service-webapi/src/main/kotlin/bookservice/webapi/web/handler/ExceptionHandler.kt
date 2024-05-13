package bookservice.webapi.web.handler

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * エラーの詳細。
 */
data class ErrorDetail(
    /**
     * 名前
     */
    val name: String,
    /**
     * 理由
     */
    val reason: String,
)

/**
 * コントローラーで発生したバリデーションエラーを [org.springframework.http.ProblemDetail] に変換するハンドラー。
 */
@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * メソッドの引数のバリデーションエラーで発生する例外をハンドリングする。
     * リクエストボディのバリデーションエラーは `handleMethodArgumentNotValid` でハンドリングされるので注意。
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
    ): ProblemDetail {
        val errors = ex.constraintViolations.map {
            ErrorDetail(it.propertyPath.last().toString(), it.message)
        }

        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            detail = "Input parameter error."
            setProperty("errors", errors)
        }
    }

    /**
     * リクエストボディのバリデーションエラーで発生する例外をハンドリングする。
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val errors = ex.bindingResult.allErrors.map {
            when (it) {
                is FieldError -> ErrorDetail(it.field, it.defaultMessage ?: "")
                else -> ErrorDetail("unknown", it.defaultMessage ?: "")
            }
        }

        val problemDetail = ProblemDetail.forStatus(status).apply {
            detail = "Input parameter error."
            setProperty("errors", errors)
        }

        return ResponseEntity(problemDetail, headers, status)
    }

    /**
     * HTTP メッセージが読み取れない際に発生する例外をハンドリングする。
     * 例：JSON の形式が不正。
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val error = when (val cause = ex.cause) {
            is InvalidFormatException -> {
                val name = cause.path.joinToString(".") { it.fieldName }
                ErrorDetail(name, "型が一致しません")
            }
            is MismatchedInputException -> {
                // 変換先のフィールドが non-nullable で入力が null | undefined の場合に例外がスローされる。
                val name = cause.path.joinToString(".") { it.fieldName }
                ErrorDetail(name, "項目が存在しません")
            }
            else -> {
                ErrorDetail("unknown", "メッセージが不明な形式です")
            }
        }

        val problemDetail = ProblemDetail.forStatus(status).apply {
            detail = "Input parameter error."
            setProperty("errors", listOf(error))
        }

        return ResponseEntity(problemDetail, headers, status)
    }
}

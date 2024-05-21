package bookservice.webapi.extension

import bookservice.webapi.service.type.Defined
import bookservice.webapi.service.type.Undefinable
import bookservice.webapi.service.type.Undefined
import org.openapitools.jackson.nullable.JsonNullable

/**
 * [JsonNullable] を [Undefinable] に変換する。
 *
 * @return 変換後の [Undefinable]
 */
fun <T> JsonNullable<T>.toUndefinable(): Undefinable<T> {
    return if (this.isPresent) {
        Defined(this.get())
    } else {
        Undefined()
    }
}

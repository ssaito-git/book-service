package bookservice.webapi.service.type

/**
 * 未定義を表現する。
 *
 * @param T 値の型。
 */
sealed interface Undefinable<T> {
    /**
     * 一つの型に変換する。
     *
     * @param U 変換後の型
     * @param defined 定義されている場合に実行されるブロック
     * @param undefined 未定義の場合に実行されるブロック
     * @return 変換後の値
     */
    fun <U> fold(defined: (T) -> U, undefined: () -> U): U {
        return when (this) {
            is Defined -> defined(this.value)
            is Undefined -> undefined()
        }
    }

    /**
     * 定義された値を変換する。
     *
     * @param U 変換後の型
     * @param transform 変換ブロック
     * @return 変換後の型の [Undefinable]
     */
    fun <U> map(transform: (T) -> U): Undefinable<U> {
        return when (this) {
            is Defined -> Defined(transform(this.value))
            is Undefined -> Undefined()
        }
    }
}

/**
 * 値が定義されている状態を表す。
 *
 * @param T 値の型
 * @property value 値
 */
data class Defined<T>(val value: T) : Undefinable<T>

/**
 * 未定義の状態を表す。
 *
 * @param T 値の方
 */
class Undefined<T> : Undefinable<T>

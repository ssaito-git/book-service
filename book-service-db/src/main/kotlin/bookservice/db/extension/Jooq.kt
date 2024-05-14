package bookservice.db.extension

import org.jooq.Condition

/**
 * Condition に条件を追加する。
 *
 * block の戻り値が null の場合は Condition をそのまま返す。
 */
fun Condition.add(block: (Condition) -> Condition?): Condition = block(this) ?: this

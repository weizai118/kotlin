// !LANGUAGE: +InlineClasses

inline class AsAny<T>(val x: Any?)
inline class AsInt(val x: Int)

object Reference {
    fun <T, R> transform(a: AsAny<T>): AsAny<R> = a as AsAny<R>
    fun <T, R> transformNullable(a: AsAny<T>?): AsAny<R> = a as AsAny<R>
    fun <T, R> transformToNullable(a: AsAny<T>): AsAny<R>? = a as AsAny<R>
    fun <T, R> transformToNullableTarget(a: AsAny<T>): AsAny<R>? = a as AsAny<R>?
    fun <T, R> transformNullableToNullableTarget(a: AsAny<T>?): AsAny<R>? = a as AsAny<R>?
}

object Primitive {
    fun transform(a: AsInt): AsInt = a as AsInt
    fun transformNullable(a: AsInt?): AsInt = a as AsInt
    fun transformToNullable(a: AsInt): AsInt? = a as AsInt
    fun transformToNullableTarget(a: AsInt): AsInt? = a as AsInt?
    fun transformNullableToNullableTarget(a: AsInt?): AsInt? = a as AsInt?
}

fun box(): String {
    val a = AsAny<Int>(42)
    val b1 = Reference.transform<Int, Number>(a)
    val b2 = Reference.transformNullable<Int, Number>(a)
    val b3 = Reference.transformToNullable<Int, Number>(a)
    val b4 = Reference.transformToNullableTarget<Int, Number>(a)
    val b5 = Reference.transformNullableToNullableTarget<Int, Number>(a)
    val b6 = Reference.transformNullableToNullableTarget<Int, Number>(null)

    val c = AsInt(42)
    val d1 = Primitive.transform(c)
    val d2 = Primitive.transformNullable(c)
    val d3 = Primitive.transformToNullable(c)
    val d4 = Primitive.transformToNullableTarget(c)
    val d5 = Primitive.transformNullableToNullableTarget(c)
    val d6 = Primitive.transformNullableToNullableTarget(null)

    return "OK"
}
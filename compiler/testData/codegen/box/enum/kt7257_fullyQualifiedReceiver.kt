// IGNORE_BACKEND: JVM_IR
// IGNORE_BACKEND: JS_IR
enum class X {
    B {
        override val value2 = "K"
        override val value = "O" + X.B.value2
    };

    abstract val value2: String
    abstract val value: String
}

fun box() = X.B.value
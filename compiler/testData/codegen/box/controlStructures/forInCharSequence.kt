// IGNORE_BACKEND: JS_IR
// WITH_RUNTIME

fun box(): String {
    var s = ""
    for (c in StringBuilder("OK")) {
        s += c
    }
    return s
}
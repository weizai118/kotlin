// LANGUAGE_VERSION: 1.3
// WITH_RUNTIME
// WITH_COROUTINES
import helpers.*
// TREAT_AS_ONE_FILE
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*
suspend fun suspendHere(): String = suspendCoroutineUninterceptedOrReturn { x ->
    x.resume("OK")
}

fun builder(c: suspend () -> Unit) {
    c.startCoroutine(EmptyContinuation)
}

val nonConstOne = 1

fun box(): String {
    var result = "fail 1"
    builder {
        // Initialize var with Int value
        for (i in 1..nonConstOne) {
            if ("".length > 0) continue
        }

        // This variable should take the same slot as 'i' had
        var s: String

        // We should not spill 's' to continuation field because it's not initialized
        // More precisely it contains a value of wrong type (it conflicts with contents of local var table),
        // so an attempt of spilling may lead to problems on Android
        if (suspendHere() == "OK") {
            s = "OK"
        }
        else {
            s = "fail 2"
        }

        result = s
    }

    return result
}

// 1 LOCALVARIABLE i I L.* 2
// 1 LOCALVARIABLE s Ljava/lang/String; L.* 2
// 0 PUTFIELD VarValueConflictsWithTableKt\$box\$1.I\$0 : I
/* 2 loads in cycle */
/* 1 load in EmptyContinuation.<init>(Lkotlin/coroutines/CoroutineContext;ILkotlin/jvm/internal/DefaultConstructorMarker;)V */
// 3 ILOAD 2

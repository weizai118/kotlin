import org.gradle.api.Project

enum class Bunch(val platformVersion: String = "") {
    IJ_172("172") { override fun bunches() = self() },
    IJ_173("173") { override fun bunches() = self() },
    IJ_181("181") { override fun bunches() = self() },
    IJ_182("182") { override fun bunches() = self() },

    AS_31("AS31") { override fun bunches() = self() },
    AS_32("AS32") { override fun bunches() = self() },

    `181+`() { override fun bunches() = listOf(*IJ_18x.flatten(), AS_32) },

    IJ() { override fun bunches() = listOf(IJ_172, IJ_173, IJ_181, IJ_182) },
    IJ_17x() { override fun bunches() = listOf(IJ_172, IJ_173) },
    IJ_18x() { override fun bunches() = listOf(IJ_181, IJ_182) },

    AS() { override fun bunches() = listOf(AS_31, AS_32) };

    abstract fun bunches(): List<Bunch>

    protected fun flatten() = bunches().toTypedArray()
    protected fun self(): List<Bunch> = listOf(this)
}

fun Array<out Bunch>.testBunches(againstPlatformVersion: String): Boolean {
    return this.flatMap { it.bunches() }
        .onEach { require(it.bunches().singleOrNull() == it) }
        .any { it.platformVersion == againstPlatformVersion }
}

inline fun Project.bunched(vararg bunches: Bunch, block: () -> Unit): Any? {
    val platformVersion = rootProject.extensions.extraProperties["versions.intellijSdk"].toString()

    if (bunches.testBunches(platformVersion)) {
        block()
        return ""
    }

    return null
}
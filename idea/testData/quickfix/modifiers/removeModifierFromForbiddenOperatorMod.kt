// "Remove 'operator' modifier" "true"
// COMPILER_ARGUMENTS: -XXLanguage:+ProhibitOperatorModAsDeclaration

object A {
    operator<caret> fun mod(x: Int) {}
}
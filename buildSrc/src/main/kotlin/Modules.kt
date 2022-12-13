object Modules {

    const val app = ":app"

    object Common {
        private const val prefix = ":common"

        const val core = "$prefix:core"
        const val coreUi = "$prefix:core-ui"
        const val design = "$prefix:design"
    }
}

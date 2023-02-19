object Modules {

    const val app = ":app"

    object Common {
        private const val prefix = ":common"

        const val core = "$prefix:core"
        const val coreAndroid = "$prefix:core-android"
        const val api = "$prefix:api"
        const val design = "$prefix:design"
        const val navigation = "$prefix:navigation"
        const val ui = "$prefix:ui"
    }

    object Featues {
        private const val prefix = ":features"

        const val home = "$prefix:home"
        const val auth = "$prefix:auth"
        const val about = "$prefix:about"
        const val encryption = "$prefix:encryption"
        const val category = "$prefix:category"

        object Recipe {
            private const val prefix = "${Featues.prefix}:recipe"

            const val info = "$prefix:info"
            const val share = "$prefix:share"
            const val control = "$prefix:control"
            const val input = "$prefix:input"
        }

        object RecipeBook {
            private const val prefix = "${Featues.prefix}:recipe-book"

            const val dashboard = "$prefix:dashboard"
            const val search = "$prefix:search"
            const val favourite = "$prefix:favourite"
            const val category = "$prefix:category"
        }

        object ShoppingList {
            private const val prefix = "${Featues.prefix}:shopping-list"

            const val control = "$prefix:control"
            const val purchaseInput = "$prefix:purchase-input"
        }
    }
}

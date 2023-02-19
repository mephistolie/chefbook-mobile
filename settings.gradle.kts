rootProject.name = "chefbook"

include(":app")

include(":common:core")
include(":common:core-android")
include(":common:api")
include(":common:design")
include(":common:navigation")
include(":common:ui")

include(":features:auth")
include(":features:home")
include(":features:category")
include(":features:about")
include(":features:encryption")

include(":features:recipe:info")
include(":features:recipe:input")
include(":features:recipe:control")
include(":features:recipe:share")

include(":features:recipe-book:search")
include(":features:recipe-book:favourite")
include(":features:recipe-book:category")
include(":features:recipe-book:dashboard")

include(":features:shopping-list:purchase-input")
include(":features:shopping-list:control")

package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppIconUseCase

internal actual class SetAppIconUseCaseImpl : SetAppIconUseCase {

  actual override suspend operator fun invoke(icon: AppIcon) = Unit
}

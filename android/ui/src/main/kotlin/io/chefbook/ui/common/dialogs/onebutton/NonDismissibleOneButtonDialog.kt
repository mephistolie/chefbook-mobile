package io.chefbook.ui.common.dialogs.onebutton

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import io.chefbook.navigation.params.dialogs.OneButtonDialogParams
import io.chefbook.navigation.results.dialogs.OneButtonDialogResult
import io.chefbook.navigation.styles.NonDismissibleDialog

@Destination("one_button_dialog_non_dismissible", style = NonDismissibleDialog::class)
@Composable
fun NonDismissibleOneButtonDialog(
  params: OneButtonDialogParams,
  request: String? = null,
  resultBackNavigator: ResultBackNavigator<OneButtonDialogResult>,
) {
  OneButtonDialogContent(
    title = stringResource(params.titleId),
    description = params.descriptionId?.let { stringResource(id = it) },
    buttonIcon = ImageVector.vectorResource(params.buttonIconId),
    buttonText = stringResource(id = params.buttonTextId),

    onClick = { resultBackNavigator.navigateBack(OneButtonDialogResult(request)) },
  )
}

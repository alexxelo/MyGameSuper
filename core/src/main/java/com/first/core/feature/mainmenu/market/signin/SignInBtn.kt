package com.first.core.feature.mainmenu.market.signin

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.first.auth.ForActivityResult
import com.first.core.R


@Composable
fun launchSignIn(intent: Intent, onResult: (ForActivityResult) -> Unit): ManagedActivityResultLauncher<Unit, ForActivityResult> {
  return rememberLauncherForActivityResult(
    contract = object : ActivityResultContract<Unit, ForActivityResult>() {
      override fun createIntent(context: Context, input: Unit): Intent {
        return intent
      }

      override fun parseResult(resultCode: Int, intent: Intent?): ForActivityResult {
        return ForActivityResult(
          resultCode = resultCode,
          intent = intent,
        )
      }
    },
    onResult = onResult
  )
}

@Composable
fun SignInBtn(
  modifier: Modifier = Modifier,
  vm: SignInBtnVM,
  onClick: () -> Unit = {},
) {
  val isSignedIn by vm.isSignedIn.observeAsState()
  val isSignedInSafe = isSignedIn ?: return

  val signInProcess = launchSignIn(
    intent = vm.signInIntent,
    onResult = vm::onSignInResult,
  )
  SignInBtn(
    modifier = modifier,
    isSignedIn = isSignedInSafe,
    onClick = {
      onClick()
      if (isSignedInSafe) {
        vm.onSignOutClick()
      } else {
        signInProcess.launch(Unit)
      }
    },
  )
}

@Composable
fun SignInBtn(
  modifier: Modifier = Modifier,
  isSignedIn: Boolean = false,
  onClick: () -> Unit = {},
) {
  Button(
    modifier = modifier,
    onClick = { onClick() }
  ) {
    SignInImage(
      modifier = Modifier.size(26.dp),
      isSignedIn = isSignedIn,
    )
    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
    Text(
      text = if (isSignedIn) {
        stringResource(id = com.first.auth.R.string.sign_out)
      } else {
        stringResource(id = com.first.auth.R.string.sign_in)
      },
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
    )
  }

}

@Composable
private fun SignInImage(
  modifier: Modifier = Modifier,
  isSignedIn: Boolean = false,
  heightPx: Float = 0f,
  painter: Painter = painterResource(id = R.drawable.ic_google_play_games)
) {
  val alpha = if (isSignedIn) 1f else 0.50f
  val alphaAnimated by animateFloatAsState(targetValue = alpha)

  val offsetY = if (isSignedIn) 0f else heightPx / 2f
  val offsetYAnimated by animateFloatAsState(targetValue = offsetY)

  Image(
    painter = painter,
    contentDescription = null, //decorative
    modifier = modifier
      .graphicsLayer {
        translationY = offsetYAnimated
      }
      .alpha(alpha = alphaAnimated)
  )
}

@Preview
@Composable
fun SignInBtnPreview(modifier: Modifier = Modifier) {
  SignInBtn(modifier = modifier)
}
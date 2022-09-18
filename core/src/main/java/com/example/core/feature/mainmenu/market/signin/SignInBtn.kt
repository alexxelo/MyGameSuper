package com.example.core.feature.mainmenu.market.signin

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth.ForActivityResult
import com.example.core.R
import com.example.core.feature.mainmenu.MainMenuTokens
import com.ilyin.ui_core_compose.colors.MdColors


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBtn(
  modifier: Modifier = Modifier,
  isSignedIn: Boolean = false,
  onClick: () -> Unit = {},
) {
  Card(
    modifier = modifier.semantics(true) {},
    onClick = onClick,
    border = MainMenuTokens.cardBorder(),
  ) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
      val heightPx = with(LocalDensity.current) { maxHeight.toPx() }
      Bg(
        modifier = Modifier.fillMaxSize(),
        isSignedIn = isSignedIn,
      )
      SignInImage(
        modifier = Modifier
          .fillMaxSize(0.75f)
          .align(Alignment.Center),
        heightPx = heightPx,
        isSignedIn = isSignedIn,
      )
      Text(
        text = if (isSignedIn) {
          stringResource(id = com.example.auth.R.string.sign_in)
        } else {
          stringResource(id = com.example.auth.R.string.sign_out)
        },
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
          .padding(16.dp),
      )
    }
  }
}

@Composable
private fun Bg(
  modifier: Modifier = Modifier,
  isSignedIn: Boolean = false,
  signedInBgColor: Color = MainMenuTokens.computeBtnColor(MdColors.lime),
  signedOutBgColor: Color = signedInBgColor.copy(alpha = 0.5f).compositeOver(Color.White),
) {
  val colorAnimated by animateColorAsState(targetValue = if (isSignedIn) signedInBgColor else signedOutBgColor)
  Spacer(
    modifier = modifier.background(
      color = colorAnimated
    )
  )
}

@Composable
private fun SignInImage(
  modifier: Modifier = Modifier,
  isSignedIn: Boolean = false,
  heightPx: Float = 0f,
  painter: Painter = painterResource(id = com.ilyin.localization.R.drawable.ic_flag_arabic)
    //painterResource(id = R.drawable.ic_google_play_games),
) {
  val alpha = if (isSignedIn) 1f else 0.50f
  val alphaAnimated by animateFloatAsState(targetValue = alpha)

  val offsetY = if (isSignedIn) 0f else heightPx / 4f
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
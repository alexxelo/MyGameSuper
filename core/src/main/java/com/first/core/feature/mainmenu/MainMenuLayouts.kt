package com.first.core.feature.mainmenu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.ui_core_compose.isScreenWide

private val cardSpacing = 16.dp

@Composable
fun MainMenuLayouts(
  modifier: Modifier = Modifier,
  background: @Composable (modifier: Modifier) -> Unit = {},
  marketCard: @Composable (modifier: Modifier) -> Unit = {},
  //rateView: @Composable (modifier: Modifier) -> Unit = {},
  play: @Composable (modifier: Modifier) -> Unit = {},
  settings: @Composable (modifier: Modifier) -> Unit = {},
  //knowledge: @Composable (modifier: Modifier) -> Unit = {},
) {
  if (isScreenWide()) {
    MainMenuLayoutWide(
      modifier = modifier,
      background = background,
      marketCard = marketCard,
      //rateView = rateView,
      play = play,
      settings = settings,
      //knowledge = knowledge,
    )
  } else {
    MainMenuLayoutNormal(
      modifier = modifier,
      background = background,
      marketCard = marketCard,
      //rateView = rateView,
      play = play,
      settings = settings,
      //knowledge = knowledge,
    )
  }
}

@Composable
private fun MainMenuLayoutWide(
  modifier: Modifier = Modifier,
  background: @Composable (modifier: Modifier) -> Unit = {},
  marketCard: @Composable (modifier: Modifier) -> Unit = {},
  //rateView: @Composable (modifier: Modifier) -> Unit = {},
  play: @Composable (modifier: Modifier) -> Unit = {},
  settings: @Composable (modifier: Modifier) -> Unit = {},
  //knowledge: @Composable (modifier: Modifier) -> Unit = {},
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    background(Modifier.fillMaxSize())
    Column(
      modifier = Modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(cardSpacing),
    ) {
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(cardSpacing),
      ) {
        Column(
          modifier = Modifier
            .padding(start = cardSpacing, top = cardSpacing, bottom = cardSpacing)
            .fillMaxHeight()
            .weight(1f),
          verticalArrangement = Arrangement.spacedBy(cardSpacing),
        ) {
          marketCard(
            modifier = Modifier
              .weight(1f)
              .aspectRatio(1f),
          )
          Box(
            modifier = Modifier
              .weight(1f)
              .width(IntrinsicSize.Min)
          ) {
            /*rateView(
              modifier = Modifier
                .fillMaxHeight(1f)
                .width(IntrinsicSize.Min),
            )*/
          }
        }

        Card1(
          modifier = Modifier
            .fillMaxHeight(1f)
            .aspectRatio(1f)
            .padding(cardSpacing),
          play = play,
          settings = settings,
          //knowledge = knowledge,
        )
      }
    }
  }
}

@Composable
private fun MainMenuLayoutNormal(
  modifier: Modifier = Modifier,
  background: @Composable (modifier: Modifier) -> Unit = {},
  marketCard: @Composable (modifier: Modifier) -> Unit = {},
  //rateView: @Composable (modifier: Modifier) -> Unit = {},
  play: @Composable (modifier: Modifier) -> Unit = {},
  settings: @Composable (modifier: Modifier) -> Unit = {},
  //knowledge: @Composable (modifier: Modifier) -> Unit = {},
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    background(Modifier.fillMaxSize())
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(cardSpacing),
    ) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(2f),
        horizontalArrangement = Arrangement.spacedBy(cardSpacing),
        contentPadding = PaddingValues(top = cardSpacing, start = cardSpacing, end = cardSpacing)
      ) {
        item {
          marketCard(
            modifier = Modifier
              .fillMaxHeight()
              .aspectRatio(1f),
          )
        }
        item {
          /*rateView(
            modifier = Modifier
              .width(IntrinsicSize.Min)
              .fillMaxHeight(),
          )*/
        }
      }
      Spacer(modifier = Modifier.weight(1f))
      Card1(
        modifier = Modifier
          .fillMaxWidth()
          .height(IntrinsicSize.Min)
          .padding(cardSpacing),
        play = play,
        settings = settings,
        //knowledge = knowledge,
      )
//      Disclaimer(modifier = Modifier.padding(bottom = cardSpacing))
    }
  }
}

@Composable
fun Disclaimer(modifier: Modifier) {
  Text(
    text = "Game is in beta. Development is in progress.",
    modifier = modifier,
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.labelSmall
  )
}

@Composable
private fun Card1(
  modifier: Modifier = Modifier,
  play: @Composable (modifier: Modifier) -> Unit = {},
  settings: @Composable (modifier: Modifier) -> Unit = {},
  knowledge: @Composable (modifier: Modifier) -> Unit = {},
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(cardSpacing)
  ) {
    Row(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(cardSpacing),
    ) {
      settings(
        modifier = Modifier
          .weight(1f)
          .aspectRatio(1f)
      )
      knowledge(
        modifier = Modifier
          .weight(1f)
          .aspectRatio(1f)
      )
    }
    play(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .aspectRatio(2f)
    )
  }
}

@Preview(device = Devices.PIXEL_3, widthDp = 392, heightDp = 785, showBackground = true)
@Preview(device = Devices.PIXEL_C, widthDp = 785, heightDp = 392, showBackground = true)
@Composable
fun MainMenuLayoutsPreview(modifier: Modifier = Modifier) {
  //MainMenuViewPreview(modifier = modifier)
}
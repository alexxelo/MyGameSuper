package com.example.core.feature.tipshop.free.ads


import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.advertising.watchvideo.VideoAdState
import com.example.core.feature.tipshop.free.ads.TipsForAdsVM.Companion.TIPS_FOR_AD

@Composable
fun TipsForAds(
  modifier: Modifier = Modifier,
  vm: TipsForAdsVM,
  activity: Activity,
) {


  val adState by vm.adState.observeAsState()
  val adStateSafe = adState ?: return

  TipsForAds(
    modifier = modifier,
    adState = adStateSafe,
    onWatchClick = { vm.watch(activity) },
    onReloadClick = { vm.requestReload() }
  )

}

@Composable
fun TipsForAds(
  modifier: Modifier = Modifier,
  adState: VideoAdState,
  onWatchClick: () -> Unit = {},
  onReloadClick: () -> Unit = {},
) {

  Column(modifier = modifier) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(text = stringResource(id = com.example.core.R.string.tips_amount, TIPS_FOR_AD))
      Spacer(modifier = Modifier.weight(1f))
      Btn(
        modifier = modifier,
        adState = adState,
        onWatchClick = onWatchClick,
        onReloadClick = onReloadClick
      )

      Text(
        text = stringResource(com.example.core.R.string.tips_for_ads_description),
//        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }

}

@Composable
fun Btn(
  modifier: Modifier,
  adState: VideoAdState,
  onWatchClick: () -> Unit,
  onReloadClick: () -> Unit
) {
  val noClick: () -> Unit = {}
  val onClick = when (adState) {
    VideoAdState.AD_LOADING -> noClick
    VideoAdState.AD_LOADED -> onWatchClick
    VideoAdState.NO_AD -> onReloadClick
  }

  Button(
    onClick = onClick,
    enabled = adState != VideoAdState.AD_LOADING,
    modifier = modifier.semantics(true) {},
  ) {
    when (adState) {
      VideoAdState.AD_LOADING -> CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
      VideoAdState.AD_LOADED -> Icon(
        imageVector = Icons.Default.Videocam,
        contentDescription = null, // decorative
      )
      VideoAdState.NO_AD -> Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null, // decorative
      )
    }

    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = when (adState) {
        VideoAdState.AD_LOADING -> "Loading"//stringResource(id = com.example.advertising.R.string.ad_state_loading)
        VideoAdState.AD_LOADED -> "watch"//stringResource(id = com.example.advertising.R.string.watch)
        VideoAdState.NO_AD -> "no ad"//stringResource(id = com.example.advertising.R.string.ad_state_no_ad)
      }
    )
  }
}


@Preview
@Composable
fun TipsForAdsPreviewLoaded(modifier: Modifier = Modifier) {
  TipsForAds(modifier = modifier, adState = VideoAdState.AD_LOADED)
}

@Preview
@Composable
fun TipsForAdsPreviewLoading(modifier: Modifier = Modifier) {
  TipsForAds(modifier = modifier, adState = VideoAdState.AD_LOADING)
}

@Preview
@Composable
fun TipsForAdsPreviewNoAd(modifier: Modifier = Modifier) {
  TipsForAds(modifier = modifier, adState = VideoAdState.NO_AD)
}

package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.House

@Composable
fun HouseThumbnail(
  imageNameOrUrl: String,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  // Map known local drawable names
  val drawableRes = when (imageNameOrUrl) {
    "house_flat_1" -> R.drawable.house_flat_1
    "house_room_2" -> R.drawable.house_room_2
    "house_luxury_3" -> R.drawable.house_luxury_3
    else -> null
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant),
    contentAlignment = Alignment.Center
  ) {
    if (drawableRes != null) {
      Image(
        painter = painterResource(id = drawableRes),
        contentDescription = "বাসার ছবি",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else if (imageNameOrUrl.startsWith("http://") || imageNameOrUrl.startsWith("https://")) {
      AsyncImage(
        model = ImageRequest.Builder(context)
          .data(imageNameOrUrl)
          .crossfade(true)
          .build(),
        contentDescription = "বাসার ছবি",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        error = painterResource(id = R.drawable.house_flat_1),
        placeholder = painterResource(id = R.drawable.house_flat_1)
      )
    } else {
      Image(
        painter = painterResource(id = R.drawable.house_flat_1),
        contentDescription = "বাসার ছবি",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

@Composable
fun HouseCard(
  house: House,
  onCardClick: () -> Unit,
  onCallClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    onClick = onCardClick,
    modifier = modifier
      .fillMaxWidth()
      .testTag("house_card_${house.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp,
      pressedElevation = 4.dp
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Leading Image
      HouseThumbnail(
        imageNameOrUrl = house.image,
        modifier = Modifier
          .size(76.dp)
          .clip(RoundedCornerShape(12.dp))
      )

      Spacer(modifier = Modifier.width(14.dp))

      // Middle Details
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(end = 6.dp),
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = house.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "এলাকা: ${house.area}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = "ভাড়া: ${house.rent}",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        )

        if (house.bedrooms.isNotBlank() || house.houseType.isNotBlank()) {
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            ) {
              Text(
                text = "${house.bedrooms} • ${house.houseType}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }

      // Trailing Call Action Button
      FilledTonalButton(
        onClick = onCallClick,
        modifier = Modifier
          .size(44.dp)
          .testTag("call_button_${house.id}"),
        shape = CircleShape,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Call,
          contentDescription = "কল করুন: ${house.phone}",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun SearchBarComponent(
  query: String,
  onQueryChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    value = query,
    onValueChange = onQueryChange,
    modifier = modifier
      .fillMaxWidth()
      .testTag("search_text_field"),
    placeholder = {
      Text(
        text = "এলাকা দিয়ে সার্চ করুন (যেমন: চৌরাস্তা, বোর্ড বাজার)...",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "সার্চ করুন",
        tint = MaterialTheme.colorScheme.primary
      )
    },
    trailingIcon = {
      if (query.isNotEmpty()) {
        IconButton(onClick = { onQueryChange("") }) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "মুছুন",
            tint = MaterialTheme.colorScheme.outline
          )
        }
      }
    },
    singleLine = true,
    shape = RoundedCornerShape(14.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = MaterialTheme.colorScheme.primary,
      unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
      focusedContainerColor = MaterialTheme.colorScheme.surface,
      unfocusedContainerColor = MaterialTheme.colorScheme.surface
    )
  )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AreaFilterChips(
  areas: List<String>,
  selectedArea: String,
  onAreaSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  FlowRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    areas.forEach { area ->
      val isSelected = selectedArea == area
      FilterChip(
        selected = isSelected,
        onClick = { onAreaSelected(area) },
        label = {
          Text(
            text = area,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = RoundedCornerShape(10.dp)
      )
    }
  }
}

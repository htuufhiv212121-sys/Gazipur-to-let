package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.House

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailBottomSheet(
  house: House,
  sheetState: SheetState,
  onDismiss: () -> Unit,
  onCall: (String) -> Unit,
  onShare: () -> Unit,
  onDelete: () -> Unit
) {
  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    containerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .padding(bottom = 24.dp)
        .navigationBarsPadding()
    ) {
      // Header with close button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = "গাজীপুর টু-লেট",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "বন্ধ করুন")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // House Photo Preview
      HouseThumbnail(
        imageNameOrUrl = house.image,
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .clip(RoundedCornerShape(18.dp))
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Title & Rent
      Text(
        text = house.title,
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        ),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = house.area,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = house.rent,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Specs Badges Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.weight(1f)
        ) {
          Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Bed, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = house.bedrooms, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.weight(1.3f)
        ) {
          Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.House, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = house.houseType, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
          }
        }
      }

      if (house.description.isNotBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "বাসার বিবরণ:",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = house.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Button(
          onClick = { onCall(house.phone) },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("detail_call_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "কল করুন (${house.phone})", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = {
            try {
              val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:${house.phone}")
                putExtra("sms_body", "আসসালামু আলাইকুম, আমি '${house.title}' বাসাটি ভাড়া নেওয়ার ব্যাপারে কথা বলতে চাই।")
              }
              context.startActivity(smsIntent)
            } catch (e: Exception) {
              Toast.makeText(context, "এসএমএস অ্যাপ পাওয়া যায়নি", Toast.LENGTH_SHORT).show()
            }
          },
          modifier = Modifier
            .height(48.dp)
            .testTag("detail_sms_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Message, contentDescription = "মেসেজ পাঠান", tint = MaterialTheme.colorScheme.primary)
        }

        OutlinedButton(
          onClick = onShare,
          modifier = Modifier
            .height(48.dp)
            .testTag("detail_share_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Share, contentDescription = "শেয়ার করুন", tint = MaterialTheme.colorScheme.primary)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Delete option for user added house
      OutlinedButton(
        onClick = {
          onDelete()
          onDismiss()
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("delete_house_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
      ) {
        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("বিজ্ঞাপন মুছে ফেলুন", style = MaterialTheme.typography.bodySmall)
      }
    }
  }
}

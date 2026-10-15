package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostHouseBottomSheet(
  sheetState: SheetState,
  onDismiss: () -> Unit,
  onPostHouse: (title: String, area: String, rent: String, phone: String, houseType: String, bedrooms: String, description: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var area by remember { mutableStateOf("") }
  var rent by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }

  val houseTypes = listOf("ফ্যামিলি ফ্ল্যাট", "ব্যাচেলর ঘর", "সাবলেট", "বিলাসবহুল বাসা")
  var selectedType by remember { mutableStateOf(houseTypes[0]) }

  val bedroomOptions = listOf("১ রুম", "২ রুম", "৩ রুম", "৪+ রুম")
  var selectedBedrooms by remember { mutableStateOf(bedroomOptions[1]) }

  var isError by remember { mutableStateOf(false) }

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
        .padding(horizontal = 24.dp)
        .padding(bottom = 24.dp)
        .navigationBarsPadding()
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AddHome,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "নতুন বাসা পোস্ট করুন",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "বন্ধ করুন")
        }
      }

      Text(
        text = "গাজীপুর এলাকার বাড়িভাড়া বা টু-লেট বিজ্ঞাপন দিন",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Title
      OutlinedTextField(
        value = title,
        onValueChange = {
          title = it
          if (it.isNotBlank()) isError = false
        },
        label = { Text("বাসার শিরোনাম *") },
        placeholder = { Text("যেমন: ২ রুমের পরিচ্ছন্ন ফ্ল্যাট ভাড়া") },
        leadingIcon = {
          Icon(Icons.Default.Title, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_title_input"),
        singleLine = true,
        isError = isError && title.isBlank()
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Area
      OutlinedTextField(
        value = area,
        onValueChange = {
          area = it
          if (it.isNotBlank()) isError = false
        },
        label = { Text("এলাকার নাম *") },
        placeholder = { Text("যেমন: চৌরাস্তা / বোর্ড বাজার / শিববাড়ী, গাজীপুর") },
        leadingIcon = {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_area_input"),
        singleLine = true,
        isError = isError && area.isBlank()
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Rent & Phone Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedTextField(
          value = rent,
          onValueChange = {
            rent = it
            if (it.isNotBlank()) isError = false
          },
          label = { Text("মাসিক ভাড়া *") },
          placeholder = { Text("যেমন: ৮,৫০০ টাকা") },
          leadingIcon = {
            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          },
          modifier = Modifier
            .weight(1f)
            .testTag("post_rent_input"),
          singleLine = true,
          isError = isError && rent.isBlank()
        )

        OutlinedTextField(
          value = phone,
          onValueChange = {
            phone = it
            if (it.isNotBlank()) isError = false
          },
          label = { Text("ফোন নম্বর *") },
          placeholder = { Text("017XXXXXXXX") },
          leadingIcon = {
            Icon(Icons.Default.Call, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier
            .weight(1f)
            .testTag("post_phone_input"),
          singleLine = true,
          isError = isError && phone.isBlank()
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Room Type Selection
      Text(
        text = "বাসার ধরন:",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        houseTypes.forEach { type ->
          FilterChip(
            selected = selectedType == type,
            onClick = { selectedType = type },
            label = { Text(type) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bedrooms Selection
      Text(
        text = "রুমের সংখ্যা:",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        bedroomOptions.forEach { bed ->
          FilterChip(
            selected = selectedBedrooms == bed,
            onClick = { selectedBedrooms = bed },
            label = { Text(bed) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Description
      OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text("অতিরিক্ত বিবরণ (ঐচ্ছিক)") },
        placeholder = { Text("গ্যাস, বিদ্যুৎ, পানির সুবিধা বা শর্তসমূহ...") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_description_input"),
        minLines = 2,
        maxLines = 4
      )

      if (isError) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "অনুগ্রহ করে শিরোনাম, এলাকা, ভাড়া এবং ফোন নম্বর পূরণ করুন।",
          color = MaterialTheme.colorScheme.error,
          style = MaterialTheme.typography.bodySmall
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          if (title.isBlank() || area.isBlank() || rent.isBlank() || phone.isBlank()) {
            isError = true
          } else {
            val formattedRent = if (rent.contains("টাকা")) rent else "$rent টাকা/মাস"
            onPostHouse(
              title,
              area,
              formattedRent,
              phone,
              selectedType,
              selectedBedrooms,
              description
            )
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("submit_post_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        )
      ) {
        Text(
          text = "বিজ্ঞাপন প্রকাশ করুন",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
    }
  }
}

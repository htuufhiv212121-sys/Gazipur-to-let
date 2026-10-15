package com.example.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.House
import com.example.ui.components.AreaFilterChips
import com.example.ui.components.HouseCard
import com.example.ui.components.HouseDetailBottomSheet
import com.example.ui.components.PostHouseBottomSheet
import com.example.ui.components.SearchBarComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: HouseViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val houses by viewModel.houses.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedArea by viewModel.selectedArea.collectAsStateWithLifecycle()

  var showPostSheet by remember { mutableStateOf(false) }
  val postSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var selectedHouseForDetail by remember { mutableStateOf<House?>(null) }
  val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val gazipurAreas = listOf(
    "সব",
    "চৌরাস্তা",
    "বোর্ড বাজার",
    "শিববাড়ী",
    "জয়দেবপুর",
    "কোনাবাড়ী",
    "চান্দনা"
  )

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Apartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "গাজীপুর টু-লেট (To-Let)",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary
              )
              Text(
                text = "গাজীপুর সিটি • বাসা ও ফ্ল্যাট ভাড়া",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
          titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Text(
              text = "${houses.size} টি বাসা",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }
        }
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showPostSheet = true },
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = {
          Text(
            text = "বাসা পোস্ট করুন",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
          )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
        modifier = Modifier.testTag("post_house_fab")
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      // সার্চ বার
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        SearchBarComponent(
          query = searchQuery,
          onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // এলাকা ফিল্টার চিপস
        AreaFilterChips(
          areas = gazipurAreas,
          selectedArea = selectedArea,
          onAreaSelected = { viewModel.onAreaSelected(it) }
        )
      }

      // বাসার তালিকা
      if (houses.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
              )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = "কোন বাসা পাওয়া যায়নি",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (searchQuery.isNotBlank() || selectedArea != "সব")
                "ভিন্ন কোনো এলাকা বা শব্দ দিয়ে খুঁজুন অথবা নতুন বাসা পোস্ট করুন।"
              else
                "বাসা যোগ করতে নিচের 'বাসা পোস্ট করুন' বোতামে চাপুন।",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .testTag("house_list_view"),
          contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 88.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(items = houses, key = { it.id }) { house ->
            HouseCard(
              house = house,
              onCardClick = { selectedHouseForDetail = house },
              onCallClick = {
                scope.launch {
                  snackbarHostState.showSnackbar("কল করা হচ্ছে: ${house.phone}")
                }
                viewModel.callHouse(context, house.phone)
              }
            )
          }
        }
      }
    }
  }

  // নতুন বাসা পোস্ট করার বটম শিট
  if (showPostSheet) {
    PostHouseBottomSheet(
      sheetState = postSheetState,
      onDismiss = { showPostSheet = false },
      onPostHouse = { title, area, rent, phone, houseType, bedrooms, description ->
        viewModel.addNewHouse(title, area, rent, phone, houseType, bedrooms, description)
        showPostSheet = false
        scope.launch {
          snackbarHostState.showSnackbar("বাসার বিজ্ঞাপন সফলভাবে পোস্ট করা হয়েছে!")
        }
      }
    )
  }

  // বাসার বিস্তারিত তথ্যের বটম শিট
  selectedHouseForDetail?.let { house ->
    HouseDetailBottomSheet(
      house = house,
      sheetState = detailSheetState,
      onDismiss = { selectedHouseForDetail = null },
      onCall = { phone ->
        scope.launch {
          snackbarHostState.showSnackbar("কল করা হচ্ছে: $phone")
        }
        viewModel.callHouse(context, phone)
      },
      onShare = { viewModel.shareHouse(context, house) },
      onDelete = {
        viewModel.deleteHouse(house)
        selectedHouseForDetail = null
        scope.launch {
          snackbarHostState.showSnackbar("বিজ্ঞাপনটি মুছে ফেলা হয়েছে")
        }
      }
    )
  }
}

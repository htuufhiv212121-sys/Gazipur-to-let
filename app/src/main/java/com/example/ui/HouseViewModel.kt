package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.House
import com.example.data.repository.HouseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HouseViewModel(
  application: Application,
  private val repository: HouseRepository
) : AndroidViewModel(application) {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedArea = MutableStateFlow("সব")
  val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

  init {
    viewModelScope.launch {
      repository.checkAndSeedInitialData()
    }
  }

  val houses: StateFlow<List<House>> = combine(
    repository.allHouses,
    _searchQuery,
    _selectedArea
  ) { houseList, query, areaFilter ->
    houseList.filter { house ->
      val matchesQuery = query.isBlank() ||
        house.title.contains(query, ignoreCase = true) ||
        house.area.contains(query, ignoreCase = true) ||
        house.rent.contains(query, ignoreCase = true)

      val matchesArea = areaFilter == "সব" ||
        house.area.contains(areaFilter, ignoreCase = true)

      matchesQuery && matchesArea
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun onAreaSelected(area: String) {
    _selectedArea.value = area
  }

  fun addNewHouse(
    title: String,
    area: String,
    rent: String,
    phone: String,
    houseType: String = "ফ্ল্যাট",
    bedrooms: String = "২ রুম",
    description: String = ""
  ) {
    viewModelScope.launch {
      // Choose an image placeholder or local asset
      val sampleImages = listOf("house_flat_1", "house_room_2", "house_luxury_3")
      val randomImage = sampleImages.random()

      val newHouse = House(
        title = title.trim(),
        area = area.trim(),
        rent = rent.trim(),
        phone = phone.trim(),
        image = randomImage,
        houseType = houseType,
        bedrooms = bedrooms,
        description = description.trim()
      )
      repository.insertHouse(newHouse)
    }
  }

  fun deleteHouse(house: House) {
    viewModelScope.launch {
      repository.deleteHouse(house)
    }
  }

  fun callHouse(context: Context, phone: String) {
    try {
      val cleanPhone = phone.replace("-", "").replace(" ", "").trim()
      val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$cleanPhone")
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "ফোন ডায়াল করা যায়নি: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
  }

  fun shareHouse(context: Context, house: House) {
    try {
      val shareText = """
        🏠 গাজীপুর টু-লেট (Gazipur To-Let)
        -------------------------------
        📌 বাসা: ${house.title}
        📍 এলাকা: ${house.area}
        💰 ভাড়া: ${house.rent}
        🛏️ রুম: ${house.bedrooms} (${house.houseType})
        📞 যোগাযোগ: ${house.phone}
        ${if (house.description.isNotBlank()) "📝 বিবরণ: ${house.description}" else ""}
      """.trimIndent()

      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, house.title)
        putExtra(Intent.EXTRA_TEXT, shareText)
      }
      context.startActivity(Intent.createChooser(intent, "বাসার তথ্য শেয়ার করুন"))
    } catch (e: Exception) {
      Toast.makeText(context, "শেয়ার করা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
    }
  }

  class Factory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val database = AppDatabase.getDatabase(application)
      val repository = HouseRepository(database.houseDao())
      return HouseViewModel(application, repository) as T
    }
  }
}

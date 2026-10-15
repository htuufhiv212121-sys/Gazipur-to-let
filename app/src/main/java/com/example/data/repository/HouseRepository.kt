package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.HouseDao
import com.example.data.model.House
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HouseRepository(private val houseDao: HouseDao) {
  val allHouses: Flow<List<House>> = houseDao.getAllHouses()

  suspend fun checkAndSeedInitialData() {
    withContext(Dispatchers.IO) {
      if (houseDao.getCount() == 0) {
        AppDatabase.seedDefaultHouses(houseDao)
      }
    }
  }

  suspend fun insertHouse(house: House): Long {
    return withContext(Dispatchers.IO) {
      houseDao.insertHouse(house)
    }
  }

  suspend fun deleteHouse(house: House) {
    withContext(Dispatchers.IO) {
      houseDao.deleteHouse(house)
    }
  }
}

package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.House
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
  @Query("SELECT * FROM houses ORDER BY timestamp DESC")
  fun getAllHouses(): Flow<List<House>>

  @Query("SELECT * FROM houses WHERE area LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' ORDER BY timestamp DESC")
  fun searchHouses(query: String): Flow<List<House>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHouse(house: House): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(houses: List<House>)

  @Delete
  suspend fun deleteHouse(house: House)

  @Query("SELECT COUNT(*) FROM houses")
  suspend fun getCount(): Int
}

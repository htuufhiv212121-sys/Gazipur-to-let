package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "houses")
data class House(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val area: String,
  val rent: String,
  val phone: String,
  val image: String,
  val houseType: String = "ফ্ল্যাট",
  val bedrooms: String = "২",
  val description: String = "",
  val timestamp: Long = System.currentTimeMillis()
)

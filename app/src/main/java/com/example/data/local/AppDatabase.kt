package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.House
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [House::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun houseDao(): HouseDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "gazipur_houses.db"
        )
        .addCallback(object : Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            scope.launch {
              INSTANCE?.houseDao()?.let { dao ->
                seedDefaultHouses(dao)
              }
            }
          }
        })
        .build()
        INSTANCE = instance
        instance
      }
    }

    suspend fun seedDefaultHouses(dao: HouseDao) {
      val defaultHouses = listOf(
        House(
          title = "২ রুমের ফ্ল্যাট ভাড়া",
          area = "চৌরাস্তা, গাজীপুর",
          rent = "৮,০০০ টাকা/মাস",
          phone = "01700000000",
          image = "house_flat_1",
          houseType = "ফ্যামিলি ফ্ল্যাট",
          bedrooms = "২ রুম",
          description = "খোলামেলা মনোরম পরিবেশ, সার্বক্ষণিক পানির সুব্যবস্থা, তিতাস গ্যাস সংযোগ ও আলাদা বারান্দা রয়েছে।"
        ),
        House(
          title = "১ রুমের ব্যাচেলর/ফ্যামিলি ঘর",
          area = "বোর্ড বাজার, গাজীপুর",
          rent = "৪,৫০০ টাকা/মাস",
          phone = "01800000000",
          image = "house_room_2",
          houseType = "ব্যাচেলর/ফ্যামিলি",
          bedrooms = "১ রুম",
          description = "আইইউটি ও জাতীয় বিশ্ববিদ্যালয়ের কাছাকাছি। নিরিবিলি পরিবেশ, ছাত্র ও চাকুরিজীবীদের জন্য উপযুক্ত।"
        ),
        House(
          title = "৩ রুমের বিলাসবহুল বাসা",
          area = "শিববাড়ী, গাজীপুর",
          rent = "১৫,০০০ টাকা/মাস",
          phone = "01900000000",
          image = "house_luxury_3",
          houseType = "বিলাসবহুল ফ্ল্যাট",
          bedrooms = "৩ রুম",
          description = "লিফট, জেনারেটর ব্যাকআপ, সিসিটিভি ক্যামেরা ও সার্বক্ষণিক সিকিউরিটি গার্ড সুবিধা সম্বলিত নতুন ফ্ল্যাট।"
        )
      )
      dao.insertAll(defaultHouses)
    }
  }
}

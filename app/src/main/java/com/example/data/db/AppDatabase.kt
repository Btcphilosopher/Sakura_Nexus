package com.example.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// --- Entities ---

@Entity(tableName = "wallet_accounts")
data class WalletAccount(
    @PrimaryKey val id: Int = 1,
    val balance: Double = 50000.0, // Default 50,000 Yen
    val points: Int = 1200,
    val userName: String = "サクラ 太郎",
    val tier: String = "GOLD"
)

@Entity(tableName = "wallet_transactions")
data class WalletTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String, // "CHARGE" or "PAYMENT"
    val category: String, // "TRANSPORT", "MART", "HEALTH", "LIVING", "DRIVE", "OTHER"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "jr_bookings")
data class JRBooking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trainName: String,
    val fromStation: String,
    val toStation: String,
    val date: String,
    val seat: String,
    val ticketType: String, // "ORDINARY" or "GREEN"
    val qrCodeData: String,
    val price: Double,
    val status: String = "ACTIVE", // "ACTIVE" or "CANCELLED"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mart_cart_items")
data class MartCartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
    val category: String,
    val imageUrl: String = "",
    val isPurchased: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "health_activities")
data class HealthActivity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val steps: Int,
    val heartRate: Int,
    val sleepMinutes: Int,
    val calorieScore: Int,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
)

// --- DAOs ---

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_accounts WHERE id = 1 LIMIT 1")
    fun getAccount(): Flow<WalletAccount?>

    @Query("SELECT * FROM wallet_accounts WHERE id = 1 LIMIT 1")
    suspend fun getAccountSync(): WalletAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAccount(account: WalletAccount)

    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getTransactions(): Flow<List<WalletTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransaction)
}

@Dao
interface JRBookingDao {
    @Query("SELECT * FROM jr_bookings ORDER BY timestamp DESC")
    fun getBookings(): Flow<List<JRBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: JRBooking)

    @Query("UPDATE jr_bookings SET status = 'CANCELLED' WHERE id = :id")
    suspend fun cancelBooking(id: Int)
}

@Dao
interface MartDao {
    @Query("SELECT * FROM mart_cart_items ORDER BY timestamp DESC")
    fun getCartItems(): Flow<List<MartCartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: MartCartItem)

    @Query("DELETE FROM mart_cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Int)

    @Query("UPDATE mart_cart_items SET isPurchased = 1 WHERE isPurchased = 0")
    suspend fun checkoutCart()

    @Query("DELETE FROM mart_cart_items WHERE isPurchased = 0")
    suspend fun clearCart()
}

@Dao
interface HealthDao {
    @Query("SELECT * FROM health_activities ORDER BY timestamp DESC")
    fun getHealthActivities(): Flow<List<HealthActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthActivity(activity: HealthActivity)
}

// --- AppDatabase ---

@Database(
    entities = [
        WalletAccount::class,
        WalletTransaction::class,
        JRBooking::class,
        MartCartItem::class,
        HealthActivity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun jrBookingDao(): JRBookingDao
    abstract fun martDao(): MartDao
    abstract fun healthDao(): HealthDao
}

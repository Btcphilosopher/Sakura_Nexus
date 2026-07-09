package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.db.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SakuraRepository private constructor(private val db: AppDatabase) {

    private val walletDao = db.walletDao()
    private val jrBookingDao = db.jrBookingDao()
    private val martDao = db.martDao()
    private val healthDao = db.healthDao()

    // --- Wallet ---
    val walletAccount: Flow<WalletAccount?> = walletDao.getAccount()
    val walletTransactions: Flow<List<WalletTransaction>> = walletDao.getTransactions()

    suspend fun getWalletAccountSync(): WalletAccount {
        val account = walletDao.getAccountSync()
        return if (account != null) {
            account
        } else {
            val newAccount = WalletAccount()
            walletDao.insertOrUpdateAccount(newAccount)
            newAccount
        }
    }

    suspend fun chargeWallet(amount: Double) {
        val current = getWalletAccountSync()
        val updated = current.copy(balance = current.balance + amount, points = current.points + (amount * 0.01).toInt())
        walletDao.insertOrUpdateAccount(updated)
        walletDao.insertTransaction(
            WalletTransaction(
                title = "ウォレットチャージ",
                amount = amount,
                type = "CHARGE",
                category = "OTHER"
            )
        )
    }

    suspend fun payWithWallet(amount: Double, title: String, category: String): Boolean {
        val current = getWalletAccountSync()
        if (current.balance < amount) return false
        val updated = current.copy(balance = current.balance - amount, points = current.points + (amount * 0.005).toInt())
        walletDao.insertOrUpdateAccount(updated)
        walletDao.insertTransaction(
            WalletTransaction(
                title = title,
                amount = amount,
                type = "PAYMENT",
                category = category
            )
        )
        return true
    }

    // --- JR Next ---
    val jrBookings: Flow<List<JRBooking>> = jrBookingDao.getBookings()

    suspend fun bookTicket(
        trainName: String,
        from: String,
        to: String,
        date: String,
        seat: String,
        isGreen: Boolean,
        price: Double
    ): Boolean {
        val success = payWithWallet(price, "JR Next ${trainName} (${from} ➔ ${to})", "TRANSPORT")
        if (!success) return false
        
        val booking = JRBooking(
            trainName = trainName,
            fromStation = from,
            toStation = to,
            date = date,
            seat = seat,
            ticketType = if (isGreen) "GREEN" else "ORDINARY",
            qrCodeData = "SAKURA_NEXUS_JR_${System.currentTimeMillis()}_$seat",
            price = price
        )
        jrBookingDao.insertBooking(booking)
        return true
    }

    suspend fun cancelBooking(id: Int, refundAmount: Double) {
        jrBookingDao.cancelBooking(id)
        val current = getWalletAccountSync()
        walletDao.insertOrUpdateAccount(current.copy(balance = current.balance + refundAmount))
        walletDao.insertTransaction(
            WalletTransaction(
                title = "JRきっぷ払戻し（返金）",
                amount = refundAmount,
                type = "CHARGE",
                category = "TRANSPORT"
            )
        )
    }

    // --- SakuraMart ---
    val cartItems: Flow<List<MartCartItem>> = martDao.getCartItems()

    suspend fun addToCart(name: String, price: Double, category: String) {
        martDao.insertCartItem(
            MartCartItem(
                name = name,
                price = price,
                category = category
            )
        )
    }

    suspend fun removeFromCart(id: Int) {
        martDao.deleteCartItem(id)
    }

    suspend fun checkoutCart(items: List<MartCartItem>): Boolean {
        val unpurchased = items.filter { !it.isPurchased }
        val total = unpurchased.sumOf { it.price * it.quantity }
        if (total <= 0.0) return false
        
        val success = payWithWallet(total, "SakuraMart お買い物 (${unpurchased.size}点)", "MART")
        if (!success) return false
        
        martDao.checkoutCart()
        return true
    }

    suspend fun clearCart() {
        martDao.clearCart()
    }

    // --- Health ---
    val healthActivities: Flow<List<HealthActivity>> = healthDao.getHealthActivities()

    suspend fun recordHealthActivity(steps: Int, heartRate: Int, sleepMinutes: Int, calorieScore: Int) {
        val baseScore = (steps / 100) + (sleepMinutes / 6)
        val score = baseScore.coerceIn(40, 100)
        val today = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN).format(Date())
        healthDao.insertHealthActivity(
            HealthActivity(
                date = today,
                steps = steps,
                heartRate = heartRate,
                sleepMinutes = sleepMinutes,
                calorieScore = calorieScore,
                score = score
            )
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: SakuraRepository? = null

        fun getInstance(context: Context): SakuraRepository {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sakura_nexus_db"
                ).fallbackToDestructiveMigration().build()
                val repo = SakuraRepository(db)
                INSTANCE = repo
                repo
            }
        }
    }
}

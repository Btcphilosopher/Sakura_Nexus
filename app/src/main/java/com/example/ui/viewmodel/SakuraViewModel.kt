package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.db.HealthActivity
import com.example.data.db.JRBooking
import com.example.data.db.MartCartItem
import com.example.data.db.WalletAccount
import com.example.data.db.WalletTransaction
import com.example.data.repository.SakuraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    JR_NEXT,
    SAKURA_MART,
    SAKURA_HEALTH,
    SAKURA_LIVING,
    SAKURA_DRIVE,
    WALLET,
    MY_ACCOUNT
}

enum class Language {
    JP, EN, CN, KR
}

data class ChatMessage(
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class SakuraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SakuraRepository.getInstance(application)

    // --- Tab and Navigation ---
    private val _currentTab = MutableStateFlow(AppTab.WALLET) // Default to Wallet / Dashboard
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // --- Language / Localization ---
    private val _currentLanguage = MutableStateFlow(Language.JP)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    // --- Wallet ---
    val walletAccount: StateFlow<WalletAccount> = repository.walletAccount
        .combine(MutableStateFlow(WalletAccount())) { acc, default -> acc ?: default }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WalletAccount())

    val walletTransactions: StateFlow<List<WalletTransaction>> = repository.walletTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun chargeWallet(amount: Double) {
        viewModelScope.launch {
            repository.chargeWallet(amount)
        }
    }

    fun payWithWallet(amount: Double, title: String, category: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.payWithWallet(amount, title, category)
            onComplete(success)
        }
    }

    // --- JR Next ---
    val jrBookings: StateFlow<List<JRBooking>> = repository.jrBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _shinkansenSearchFrom = MutableStateFlow("東京")
    val shinkansenSearchFrom = _shinkansenSearchFrom.asStateFlow()

    private val _shinkansenSearchTo = MutableStateFlow("新大阪")
    val shinkansenSearchTo = _shinkansenSearchTo.asStateFlow()

    fun setShinkansenRoute(from: String, to: String) {
        _shinkansenSearchFrom.value = from
        _shinkansenSearchTo.value = to
    }

    fun bookTicket(
        trainName: String,
        from: String,
        to: String,
        date: String,
        seat: String,
        isGreen: Boolean,
        price: Double,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.bookTicket(trainName, from, to, date, seat, isGreen, price)
            onComplete(success)
        }
    }

    fun cancelTicket(id: Int, refundAmount: Double) {
        viewModelScope.launch {
            repository.cancelBooking(id, refundAmount)
        }
    }

    // --- SakuraMart ---
    val cartItems: StateFlow<List<MartCartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addToCart(name: String, price: Double, category: String) {
        viewModelScope.launch {
            repository.addToCart(name, price, category)
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            repository.removeFromCart(id)
        }
    }

    fun checkoutCart(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val items = cartItems.value
            val success = repository.checkoutCart(items)
            onComplete(success)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // --- Sakura Health ---
    val healthActivities: StateFlow<List<HealthActivity>> = repository.healthActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun recordDailyHealth(steps: Int, heartRate: Int, sleepMinutes: Int, calorieScore: Int) {
        viewModelScope.launch {
            repository.recordHealthActivity(steps, heartRate, sleepMinutes, calorieScore)
        }
    }

    // --- AI Concierge ---
    private val _aiChatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                "ai",
                "こんにちは！Sakura Nexus のおもてなしAIコンシェルジュ「さくら」です。交通案内、お買い物、医療、不動産、モビリティなど、日本の暮らしに関するあらゆることを日本語・英語・中国語・韓国語でサポートいたします。何でもお聞きください！"
            )
        )
    )
    val aiChatMessages: StateFlow<List<ChatMessage>> = _aiChatMessages.asStateFlow()

    private val _aiIsLoading = MutableStateFlow(false)
    val aiIsLoading: StateFlow<Boolean> = _aiIsLoading.asStateFlow()

    fun sendToAIConcierge(text: String) {
        if (text.trim().isEmpty()) return
        
        val userMsg = ChatMessage("user", text)
        _aiChatMessages.value = _aiChatMessages.value + userMsg
        _aiIsLoading.value = true

        viewModelScope.launch {
            val systemInstruction = """
                You are "さくら" (Sakura), the supreme digital concierge of "Sakura Nexus" (サクラ・ネクサス), a state-level next-generation super-app of Japan.
                You provide graceful, exceptionally polite, and deeply helpful Japanese-style hospitality (Omotenashi - おもてなし).
                Your knowledge spans the 7 core pillars of the Sakura Nexus super app:
                1. JR Next (train booking, Shinkansen, Suica integration, IC card, national transit).
                2. SakuraMart (premium smart-convenience store, mobile order, freshly brewed matcha/coffee, grocery delivery).
                3. Sakura Health (digital hospitals, tele-health, vaccine tracker, health scores, pharmacy syncing).
                4. Sakura Living (virtual 3D property tours, digital contracts, smart home control, rental).
                5. Sakura Drive (autonomous taxi dispatch, EV charging reserving, carsharing, smart parking).
                6. Sakura Wallet (national ID-linked secure digital currency, loyalty points, transport ticketing).
                7. Smart City Infrastructures (Tokyo smart city initiative, digital government Integration).

                Maintain a polite, serene, and sophisticated persona. Format your answers elegantly, using bullet points for clarity.
                Answer in the language of the user's prompt (Japanese, English, Chinese, or Korean). If they write in Japanese, use honorific form (です・ます調).
            """.trimIndent()

            val aiResponse = GeminiClient.getResponse(text, systemInstruction)
            _aiChatMessages.value = _aiChatMessages.value + ChatMessage("ai", aiResponse)
            _aiIsLoading.value = false
        }
    }

    fun clearAIChat() {
        _aiChatMessages.value = listOf(
            ChatMessage(
                "ai",
                "コンシェルジュ履歴をリセットしました。新たなご質問をお待ちしております。"
            )
        )
    }

    // --- Mock Data Initializers for Rich UX ---
    init {
        // Insert default activities and records if database is empty on first launch
        viewModelScope.launch {
            // Initialize wallet if empty
            repository.getWalletAccountSync()
            
            // Record a few health history entries to make the dashboard look gorgeous!
            repository.recordHealthActivity(7200, 72, 420, 2100)
            repository.recordHealthActivity(9400, 68, 480, 2400)
            repository.recordHealthActivity(5100, 75, 390, 1800)
        }
    }
}

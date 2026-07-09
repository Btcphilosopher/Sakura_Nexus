package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AIChatScreen
import com.example.ui.screens.JRNextScreen
import com.example.ui.screens.MyAccountScreen
import com.example.ui.screens.SakuraDriveScreen
import com.example.ui.screens.SakuraHealthScreen
import com.example.ui.screens.SakuraLivingScreen
import com.example.ui.screens.SakuraMartScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.Loc
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OffWhite
import com.example.ui.theme.SakuraPink
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.SakuraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: SakuraViewModel = viewModel()
                var isChatOpen by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    SuperAppMainContent(
                        viewModel = viewModel,
                        onOpenChat = { isChatOpen = true }
                    )

                    // Overlay AI Concierge Chat screen with spring animation
                    AnimatedVisibility(
                        visible = isChatOpen,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it })
                    ) {
                        AIChatScreen(
                            viewModel = viewModel,
                            onBack = { isChatOpen = false },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAppMainContent(
    viewModel: SakuraViewModel,
    onOpenChat: () -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val lang by viewModel.currentLanguage.collectAsState()
    val wallet by viewModel.walletAccount.collectAsState()
    val cart by viewModel.cartItems.collectAsState()
    val unpurchasedCart = cart.filter { !it.isPurchased }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Small Custom Generated Sakura Logo
                        Image(
                            painter = painterResource(id = R.drawable.sakura_logo),
                            contentDescription = "Sakura Nexus Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, LuxuryGold, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Sakura Nexus",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CrimsonRed)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = lang.name,
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = Loc.get("app_subtitle", lang),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                // Main Super App Bottom Tab items
                val tabs = listOf(
                    Triple(AppTab.JR_NEXT, Icons.Default.Train, Loc.get("jr_next", lang)),
                    Triple(AppTab.SAKURA_MART, Icons.Default.ShoppingCart, "SakuraMart"),
                    Triple(AppTab.SAKURA_HEALTH, Icons.Default.LocalHospital, "医療"),
                    Triple(AppTab.WALLET, Icons.Default.AccountBalanceWallet, "ウォレット"),
                    Triple(AppTab.SAKURA_LIVING, Icons.Default.Home, "不動産"),
                    Triple(AppTab.SAKURA_DRIVE, Icons.Default.DirectionsCar, "ドライブ"),
                    Triple(AppTab.MY_ACCOUNT, Icons.Default.Person, "マイページ")
                )

                tabs.forEach { (tab, icon, label) ->
                    val isSelected = currentTab == tab
                    val hasBadge = tab == AppTab.SAKURA_MART && unpurchasedCart.isNotEmpty()

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        modifier = Modifier.testTag("tab_${tab.name.lowercase()}"),
                        icon = {
                            if (hasBadge) {
                                BadgedBox(badge = { Badge { Text("${unpurchasedCart.size}") } }) {
                                    Icon(imageVector = icon, contentDescription = label)
                                }
                            } else {
                                Icon(imageVector = icon, contentDescription = label)
                            }
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CrimsonRed,
                            selectedTextColor = CrimsonRed,
                            indicatorColor = SakuraPinkLight,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main content rendering based on active tab
            when (currentTab) {
                AppTab.JR_NEXT -> JRNextScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                AppTab.SAKURA_MART -> SakuraMartScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                AppTab.SAKURA_HEALTH -> SakuraHealthScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                AppTab.SAKURA_LIVING -> SakuraLivingScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                AppTab.SAKURA_DRIVE -> SakuraDriveScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                AppTab.WALLET -> WalletScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToChat = onOpenChat
                )
                AppTab.MY_ACCOUNT -> MyAccountScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

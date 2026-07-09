package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalConvenienceStore
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.MartCartItem
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.SakuraPink
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.SakuraViewModel

data class FoodProduct(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val emoji: String,
    val description: String
)

@Composable
fun SakuraMartScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: スマホ注文 (Catalog), 1: 注文カート (Cart), 2: セルフスキャン (Barcode)
    
    val cart by viewModel.cartItems.collectAsState()
    val unpurchasedCart = cart.filter { !it.isPurchased }
    
    val products = listOf(
        FoodProduct("1", "特選 桜鮭おにぎり", 180.0, "おにぎり", "🍙", "最高級の桜鮭をふんだんに使用した極上おにぎり。"),
        FoodProduct("2", "三陸産極上寿司折", 1280.0, "寿司", "🍣", "新鮮な本マグロと真鯛、イクラを揃えたプレミアム寿司折。"),
        FoodProduct("3", "特選十勝牛ビビンバ弁当", 750.0, "お弁当", "🍱", "十勝和牛を甘辛く仕立て、新鮮野菜ナムルを載せた極み弁当。"),
        FoodProduct("4", "京都宇治抹茶ラテ", 380.0, "ドリンク", "🍵", "石臼挽きの宇治抹茶を100%使用した濃厚本格抹茶ラテ。"),
        FoodProduct("5", "サクラブレンド珈琲", 250.0, "コーヒー", "☕", "厳選された豆を自社焙煎した薫り高いドリップコーヒー。"),
        FoodProduct("6", "健康酵素サラダボウル", 480.0, "健康食品", "🥗", "20種類の無農薬野菜とスーパーフードのヘルシーサラダ。")
    )

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("モバイル注文", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = {
                    BadgedBox(badge = {
                        if (unpurchasedCart.isNotEmpty()) {
                            Badge { Text("${unpurchasedCart.size}") }
                        }
                    }) {
                        Text("カート", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text("セルフスキャン", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            when (activeSubTab) {
                0 -> {
                    // Mobile order catalogue header
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SakuraPinkLight)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(SakuraPink)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeliveryDining,
                                        contentDescription = "Delivery",
                                        tint = CrimsonRed,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "宅配・店舗受取サービス",
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "近隣のSakuraMart店舗から20分でお届け",
                                        color = CrimsonRed.copy(alpha = 0.8f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Grid of products (rendered item-by-item in lazy column for speed and safety)
                    item {
                        Text(
                            text = "プレミアム未来型フード商品",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    products.chunked(2).forEach { pair ->
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                pair.forEach { prod ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        ProductCard(prod) {
                                            viewModel.addToCart(prod.name, prod.price, prod.category)
                                            Toast.makeText(context, "${prod.name}をカートに追加しました。", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                if (pair.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Cart Details
                    if (unpurchasedCart.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Empty",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "お買い物カートは空です。",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "モバイル注文タブからお好きな未来の逸品を追加してください。",
                                    textAlign = TextAlign.Center,
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp).padding(horizontal = 24.dp)
                                )
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "カート内の商品",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(unpurchasedCart) { item ->
                            CartItemRow(item) {
                                viewModel.removeFromCart(item.id)
                            }
                        }

                        // Order totals and pay button
                        item {
                            val total = unpurchasedCart.sumOf { it.price * it.quantity }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("小計", color = Color.Gray, fontSize = 13.sp)
                                        Text("¥ ${String.format("%,.0f", total)}", fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("消費税 (10%含)", color = Color.Gray, fontSize = 13.sp)
                                        Text("¥ ${String.format("%,.0f", total * 0.1)}", fontWeight = FontWeight.Bold)
                                    }
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("合計金額", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(
                                            "¥ ${String.format("%,.0f", total)}",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 24.sp,
                                            color = CrimsonRed
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            viewModel.checkoutCart { success ->
                                                if (success) {
                                                    Toast.makeText(context, "お買い上げありがとうございました！", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "ウォレット残高が不足しています。", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("checkout_cart_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Payment,
                                            contentDescription = "Pay",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Sakura Payで支払う", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Barcode self checkout simulator
                    item {
                        SelfScanSimulator()
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ProductCard(prod: FoodProduct, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(SakuraPinkLight, Color.White)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(prod.emoji, fontSize = 48.sp)
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = prod.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = prod.description,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¥ ${String.format("%,.0f", prod.price)}",
                        fontWeight = FontWeight.Bold,
                        color = CrimsonRed,
                        fontSize = 14.sp
                    )

                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CrimsonRed)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: MartCartItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SakuraPinkLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🛍️", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("数量: ${item.quantity} • ¥ ${String.format("%,.0f", item.price)}", fontSize = 11.sp, color = Color.Gray)
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun SelfScanSimulator() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = "Self Scan",
                tint = CrimsonRed,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "店舗セルフチェックアウトQR",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "店内のスキャナーに提示するだけでお支払いが完了します。",
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp).padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Barcode graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(80.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "My Barcode",
                    tint = Color.Black,
                    modifier = Modifier.size(72.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "SAKURA_MEMBER_638A_94",
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

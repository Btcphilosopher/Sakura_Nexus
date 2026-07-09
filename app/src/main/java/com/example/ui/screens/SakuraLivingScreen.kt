package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.SakuraPink
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.SakuraViewModel

data class Property(
    val id: String,
    val name: String,
    val location: String,
    val price: Double,
    val type: String, // "BUY" or "RENT"
    val rentPrice: Double = 0.0,
    val size: String,
    val emoji: String
)

@Composable
fun SakuraLivingScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: 物件検索 (Search), 1: 住宅ローン計算 (Mortgage), 2: スマートマイホーム (Smart Home)
    
    val properties = listOf(
        Property("1", "渋谷スカイグランデ 36F", "東京都渋谷区宇田川町", 185000000.0, "BUY", 0.0, "3LDK / 85.5 ㎡", "🏢"),
        Property("2", "洛北嵐山クラシック邸宅", "京都府京都市右京区嵯峨野", 95000000.0, "BUY", 0.0, "5LDK / 164.2 ㎡", "🏯"),
        Property("3", "六本木サイバータワーハイツ", "東京都港区六本木", 0.0, "RENT", 420000.0, "1LDK / 48.0 ㎡", "🏙️")
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
                text = { Text("物件検索 / 内覧", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("ローンシミュレータ", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text("スマートホーム", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
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
                    // Property catalogue
                    item {
                        Text(
                            text = "AI推薦 プレミアム物件",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(properties) { prop ->
                        PropertyCardItem(prop) {
                            Toast.makeText(context, "3Dバーチャル内覧データをダウンロード中...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                1 -> {
                    // Mortgage calculator
                    item {
                        MortgageCalculatorCard()
                    }
                }

                2 -> {
                    // Smart Home IoT controls
                    item {
                        SmartHomeControls()
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun PropertyCardItem(prop: Property, onVRClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SakuraPinkLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(prop.emoji, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(prop.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Loc", tint = Color.Gray, modifier = Modifier.size(12.dp))
                            Text(" ${prop.location}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (prop.type == "BUY") CrimsonRed.copy(alpha = 0.1f) else Color(0xFF1E88E5).copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (prop.type == "BUY") "分譲" else "賃貸",
                        color = if (prop.type == "BUY") CrimsonRed else Color(0xFF1E88E5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("物件価格", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        text = if (prop.type == "BUY") "¥ ${String.format("%,.0f", prop.price)}" else "月額 ¥ ${String.format("%,.0f", prop.rentPrice)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = CrimsonRed
                    )
                }

                Text(prop.size, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onVRClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo, contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.ViewInAr, contentDescription = "VR", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("3Dバーチャル内覧", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SakuraPinkLight, contentColor = CrimsonRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isExpanded) "閉じる" else "詳細分析", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("AI地域・人口トレンド分析", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = "本物件は、東京都市計画「Sakura Smart City」構想の特定開発区に隣接しており、過去3年間の資産価値は年平均4.2%上昇しています。周辺の公共EVシャトル網、24時間デジタルヘルスケアハブの整備率が極めて高く、高い流動性と投資利回りが期待されます。",
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorCard() {
    var housePrice by remember { mutableStateOf(80000000f) } // Default 80 Million
    var loanTerm by remember { mutableStateOf(35f) } // Default 35 years
    var rate by remember { mutableStateOf(1.2f) } // Default 1.2%

    // Calculate monthly repayments
    // Approximate formula: monthly payment = [Principal * r * (1 + r)^n] / [(1 + r)^n - 1]
    val principal = housePrice * 0.9 // Assume 10% downpayment
    val r = (rate / 100) / 12
    val n = loanTerm * 12
    val monthlyPayment = if (r > 0) {
        (principal * r * Math.pow((1 + r).toDouble(), n.toDouble())) / (Math.pow((1 + r).toDouble(), n.toDouble()) - 1)
    } else {
        principal / n
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mortgage_calc_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Calculate, contentDescription = "Calc", tint = CrimsonRed)
                Spacer(modifier = Modifier.width(6.dp))
                Text("国家保障型スマート住宅ローン金利計算", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
            }

            Text(
                text = "銀行ローン自動一括審査・デジタル仮契約対応",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // House Price
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("借入希望金額 (Principal)", fontSize = 11.sp)
                    Text("¥ ${String.format("%,d", housePrice.toInt())}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                }
                Slider(
                    value = housePrice,
                    onValueChange = { housePrice = it },
                    valueRange = 10000000f..300000000f,
                    modifier = Modifier.testTag("loan_amount_slider")
                )
            }

            // Term
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("返済期間 (Loan Term)", fontSize = 11.sp)
                    Text("${loanTerm.toInt()} 年", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                }
                Slider(
                    value = loanTerm,
                    onValueChange = { loanTerm = it },
                    valueRange = 5f..40f
                )
            }

            // Interest Rate
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("適用想定金利 (Interest Rate)", fontSize = 11.sp)
                    Text("${String.format("%.2f", rate)} % (固定・優遇)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                }
                Slider(
                    value = rate,
                    onValueChange = { rate = it },
                    valueRange = 0.1f..3.5f
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Result
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("毎月の返済目安額", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = "月額 ¥ ${String.format("%,d", monthlyPayment.toInt())}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonRed
                    )
                }

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White)
                ) {
                    Text("仮審査を開始する", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SmartHomeControls() {
    var lightState by remember { mutableStateOf(false) }
    var airconState by remember { mutableStateOf(true) }
    var securityState by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "スマートマイホーム自動コントロール",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "国家スマートインフラ直結、自宅のIoT機器を遠隔操作",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Lights control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (lightState) SakuraPinkLight else Color.LightGray.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "Light",
                            tint = if (lightState) CrimsonRed else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("リビング照明", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(if (lightState) "点灯中 (100%)" else "消灯", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Switch(checked = lightState, onCheckedChange = { lightState = it })
            }

            // Aircon control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (airconState) SakuraPinkLight else Color.LightGray.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = "AC",
                            tint = if (airconState) CrimsonRed else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("AI自動エアコン", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(if (airconState) "冷房稼働中 (設定 24.5℃)" else "停止中", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Switch(checked = airconState, onCheckedChange = { airconState = it })
            }

            // Security system
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (securityState) Color(0xFFE8F5E9) else Color.LightGray.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            tint = if (securityState) Color(0xFF2E7D32) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("生体防犯セキュリティシステム", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(if (securityState) "セキュリティ防犯作動中" else "解除中（未警戒）", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                Switch(checked = securityState, onCheckedChange = { securityState = it })
            }
        }
    }
}

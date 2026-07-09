package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Power
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

data class ShareVehicle(
    val id: String,
    val model: String,
    val type: String, // "EV" or "PHEV"
    val range: String,
    val distance: String,
    val pricePerHour: Double,
    val emoji: String
)

@Composable
fun SakuraDriveScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: 自動配車タクシー (Taxi), 1: カーシェア (Car Share), 2: 充電・駐車場 (EV Charging/Parking)
    
    val shareVehicles = listOf(
        ShareVehicle("1", "Sakura Volt 2", "EV", "350 km", "徒歩 2分 (150m)", 1200.0, "🚗"),
        ShareVehicle("2", "Nexus Touring SUV", "EV", "480 km", "徒歩 5分 (400m)", 1800.0, "🚙"),
        ShareVehicle("3", "京エコ ハイブリッド 3", "PHEV", "600 km", "徒歩 8分 (650m)", 950.0, "🚘")
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
                text = { Text("自動運転配車", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("カーシェア", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text("充電・パーキング", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
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
                    // Autonomous Ride Hailing Simulator
                    item {
                        TaxiDispatchSimulator()
                    }
                }

                1 -> {
                    // Car Share lookup
                    item {
                        Text(
                            text = "近隣のNexusシェアステーション",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(shareVehicles) { car ->
                        CarShareItemCard(car) {
                            Toast.makeText(context, "${car.model}のドアロックを解除しました！ご乗車を開始してください。", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                2 -> {
                    // EV Charging Stations & Parking Lots Finder
                    item {
                        EVChargingStationsFinder()
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun TaxiDispatchSimulator() {
    val context = LocalContext.current
    var isDispatched by remember { mutableStateOf(false) }
    var etaMinutes by remember { mutableStateOf(4) }
    var destination by remember { mutableStateOf("東京ビッグサイト (スマートシティ総合会場)") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("taxi_dispatch_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocalTaxi, contentDescription = "Taxi", tint = CrimsonRed)
                Spacer(modifier = Modifier.width(6.dp))
                Text("完全自動運転・自動配車シャトル「Sakura Taxi」", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }

            Text(
                text = "国家最高水準の自動安全航法 (Level 4/5) を採用した無人EVタクシー",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Destination Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SakuraPinkLight)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Current", tint = CrimsonRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("現在地: 渋谷ソラスタホテル", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = "Dest", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("目的地: $destination", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isDispatched) {
                // Fair calculations
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("推定所要時間 • 料金", fontSize = 11.sp, color = Color.Gray)
                        Text("18分 • ¥ 3,850", fontSize = 20.sp, fontWeight = FontWeight.Black, color = CrimsonRed)
                    }

                    Button(
                        onClick = { isDispatched = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White),
                        modifier = Modifier.testTag("dispatch_taxi_button")
                    ) {
                        Text("無人EVタクシーを配車", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Dispatched Live Map view / simulation
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Graphic simulation of map
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Map, contentDescription = "Map", tint = Color.Gray, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("リアルタイムGPS航法同期中...", fontSize = 11.sp, color = Color.Gray)
                            Text("自動運転EV車 [品川500 さ 39-24] が向かっています", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "到着まであと 約 ${etaMinutes} 分",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF2E7D32)
                        )

                        Button(
                            onClick = {
                                isDispatched = false
                                Toast.makeText(context, "配車予約をキャンセルしました。", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f), contentColor = Color.Red),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("キャンセル", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarShareItemCard(car: ShareVehicle, onUnlock: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SakuraPinkLight),
                contentAlignment = Alignment.Center
            ) {
                Text(car.emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(car.model, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                Text(car.distance, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ElectricCar, contentDescription = "EV Range", tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                    Text(" バッテリー残航続距離: ${car.range}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("¥ ${String.format("%,.0f", car.pricePerHour)}/時間", fontWeight = FontWeight.Black, fontSize = 13.sp, color = CrimsonRed)
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onUnlock,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo, contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("解錠", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EVChargingStationsFinder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.EvStation, contentDescription = "Charging", tint = Color(0xFF00ACC1))
                Spacer(modifier = Modifier.width(6.dp))
                Text("近隣のEV超急速充電ステーション", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            listOf(
                "品川スマートシティ 超急速充電ハブ" to "利用可能: 6台 / 空き: 4台",
                "渋谷ストリーム 地下EVステーション" to "利用可能: 4台 / 空き: 1台",
                "六本木ヒルズ スーパーチャージャー" to "利用可能: 8台 / 空き: 0台 (満車)"
            ).forEach { (station, status) ->
                val isFull = status.contains("満車")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(station, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(status, fontSize = 11.sp, color = if (isFull) Color.Red else Color(0xFF2E7D32))
                    }

                    Button(
                        onClick = {},
                        enabled = !isFull,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00ACC1), contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("予約", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

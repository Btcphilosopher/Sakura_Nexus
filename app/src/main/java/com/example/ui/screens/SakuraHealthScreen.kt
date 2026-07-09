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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.SakuraPink
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.SakuraViewModel

data class MedicalClinic(
    val id: String,
    val name: String,
    val specialty: String,
    val distance: String,
    val rating: Double,
    val emoji: String
)

@Composable
fun SakuraHealthScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: 病院予約・診療 (Clinic), 1: 電子カルテ・健康管理 (EHR)
    
    val healthActivities by viewModel.healthActivities.collectAsState()
    val clinics = listOf(
        MedicalClinic("1", "桜台総合中央病院", "内科・小児科・心臓血管外科", "1.2 km", 4.9, "🏥"),
        MedicalClinic("2", "日本橋先端医療スマートハブ", "皮膚科・人間ドック・美容皮膚科", "2.5 km", 4.8, "🔬"),
        MedicalClinic("3", "京都伝統和漢クリニック", "漢方・ウェルネス・鍼灸", "4.2 km", 4.7, "🎋")
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
                text = { Text("病院検索・オンライン診療", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("電子カルテ・健康管理", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
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
                    // Videocall Consultation card banner
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(CrimsonRed, Color(0xFFC62828))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "24時間即時オンライン診療",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color.White.copy(alpha = 0.2f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("緊急受診可", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Text(
                                        text = "厚生労働省認定医師がビデオ診断。処方箋を最寄りの薬局へ即時送信します。",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    Button(
                                        onClick = {
                                            Toast.makeText(context, "オンライン医師と接続しています...", Toast.LENGTH_LONG).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = CrimsonRed),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("今すぐオンライン受診する", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Clinics list
                    item {
                        Text(
                            text = "スマート連携 登録医療機関",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(clinics) { clinic ->
                        ClinicItemCard(clinic) {
                            Toast.makeText(context, "${clinic.name}の診察枠を予約しました。", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                1 -> {
                    // Electronic Health Records
                    item {
                        EHRDashboard(healthActivities) { steps, hr, sleep, calories ->
                            viewModel.recordDailyHealth(steps, hr, sleep, calories)
                            Toast.makeText(context, "健康ログを送信しました。点数が更新されました！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ClinicItemCard(clinic: MedicalClinic, onReserve: () -> Unit) {
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
                Text(clinic.emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(clinic.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                Text(clinic.specialty, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = LuxuryGold, modifier = Modifier.size(14.dp))
                    Text(" ${clinic.rating} • ${clinic.distance}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }

            Button(
                onClick = onReserve,
                colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("予約", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EHRDashboard(
    activities: List<com.example.data.db.HealthActivity>,
    onRecordActivity: (steps: Int, hr: Int, sleep: Int, calories: Int) -> Unit
) {
    // Interactive inputs to simulate sensor telemetry
    var simulatedSteps by remember { mutableStateOf(8500f) }
    var simulatedHeartRate by remember { mutableStateOf(72f) }
    var simulatedSleep by remember { mutableStateOf(420f) } // in minutes (7 hrs)

    val latest = activities.firstOrNull()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // EHR Ring / Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "電子カルテ連携ヘルスケアスコア",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.MonitorHeart,
                        contentDescription = "Heart",
                        tint = CrimsonRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Score graphic
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(SakuraPinkLight, SakuraPink)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${latest?.score ?: 85}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = CrimsonRed
                            )
                            Text("健康スコア", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "最新同期日時: ${latest?.date ?: "今日"}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "歩数: ${String.format("%,d", latest?.steps ?: 8500)} 歩",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "平均心拍: ${latest?.heartRate ?: 72} bpm",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "睡眠時間: ${(latest?.sleepMinutes ?: 420) / 60}時間 ${(latest?.sleepMinutes ?: 420) % 60}分",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Interactive Telemetry Simulator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "スマートセンサー連携シミュレータ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "ウェアラブル・健康器具からのリアルタイム生体情報をシミュレートします。",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Steps slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("歩数 (Steps)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${simulatedSteps.toInt()} 歩", fontSize = 12.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = simulatedSteps,
                        onValueChange = { simulatedSteps = it },
                        valueRange = 0f..20000f,
                        modifier = Modifier.testTag("steps_slider")
                    )
                }

                // Heart Rate slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("心拍数 (Heart Rate)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${simulatedHeartRate.toInt()} bpm", fontSize = 12.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = simulatedHeartRate,
                        onValueChange = { simulatedHeartRate = it },
                        valueRange = 50f..140f
                    )
                }

                // Sleep slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("睡眠時間 (Sleep)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${(simulatedSleep.toInt() / 60)}時間 ${(simulatedSleep.toInt() % 60)}分", fontSize = 12.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = simulatedSleep,
                        onValueChange = { simulatedSleep = it },
                        valueRange = 120f..600f
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onRecordActivity(
                            simulatedSteps.toInt(),
                            simulatedHeartRate.toInt(),
                            simulatedSleep.toInt(),
                            (simulatedSteps * 0.25).toInt() + 1500
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("record_health_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White)
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("センサー情報を国籍電子カルテに同期", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

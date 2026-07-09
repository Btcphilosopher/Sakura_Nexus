package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import com.example.data.db.JRBooking
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepIndigo
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.SakuraPink
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.SakuraViewModel

@Composable
fun JRNextScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: 新幹線予約 (Shinkansen), 1: デジタルIC (Mobile IC), 2: 駅ナビ (Station Navigator)
    
    val bookings by viewModel.jrBookings.collectAsState()
    val fromStation by viewModel.shinkansenSearchFrom.collectAsState()
    val toStation by viewModel.shinkansenSearchTo.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // High-fidelity tab bar
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("新幹線予約", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("デジタルIC", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text("駅ナビ / MAP", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
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
                    // Shinkansen Ticket Reservation UI
                    item {
                        ShinkansenSearchCard(
                            from = fromStation,
                            to = toStation,
                            onSwap = { viewModel.setShinkansenRoute(toStation, fromStation) },
                            onBook = { isGreen, trainName, price ->
                                viewModel.bookTicket(
                                    trainName = trainName,
                                    from = fromStation,
                                    to = toStation,
                                    date = "2026/08/15",
                                    seat = "${(1..15).random()}-${('A'..'E').random()}",
                                    isGreen = isGreen,
                                    price = price
                                ) { success ->
                                    if (success) {
                                        Toast.makeText(context, "新幹線予約が完了しました！", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "残高不足です。ウォレットにチャージしてください。", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        )
                    }

                    // Active Bookings / QR Passes list
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookOnline,
                                contentDescription = "Active Bookings",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "購入済みのチケット",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (bookings.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsRailway,
                                        contentDescription = "No tickets",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "予約された新幹線きっぷはまだありません。\n上記の検索パネルからご予約いただけます。",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(bookings) { booking ->
                            ShinkansenTicketCard(booking) {
                                viewModel.cancelTicket(booking.id, booking.price)
                                Toast.makeText(context, "きっぷの払戻しを行いました。", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                1 -> {
                    // Mobile IC card integration page
                    item {
                        MobileICCard()
                    }
                }

                2 -> {
                    // Station guides and maps
                    item {
                        StationGuide()
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ShinkansenSearchCard(
    from: String,
    to: String,
    onSwap: () -> Unit,
    onBook: (Boolean, String, Double) -> Unit
) {
    var isGreenSeat by remember { mutableStateOf(false) }
    val trainOption = if (isGreenSeat) "のぞみ（グリーン車）" else "のぞみ（普通指定席）"
    val basePrice = if (isGreenSeat) 19590.0 else 14720.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("shinkansen_search_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "スマートEX型 新幹線かんたん予約",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "タッチ決済 & QR乗車対応のデジタルチケット",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stations selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // From station
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("出発駅", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = from,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Swap Icon button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { onSwap() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap Route",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // To station
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("到着駅", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = to,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Class selection Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ordinary Seat
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isGreenSeat) SakuraPinkLight else Color.Transparent)
                        .clickable { isGreenSeat = false }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = !isGreenSeat, onClick = { isGreenSeat = false })
                    Column {
                        Text("普通席", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("¥14,720", fontSize = 11.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                }

                // Green seat
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isGreenSeat) SakuraPinkLight else Color.Transparent)
                        .clickable { isGreenSeat = true }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = isGreenSeat, onClick = { isGreenSeat = true })
                    Column {
                        Text("グリーン車", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("¥19,590", fontSize = 11.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Purchase Button
            Button(
                onClick = { onBook(isGreenSeat, trainOption, basePrice) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("book_ticket_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "Pay",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${trainOption}を予約する • ¥${String.format("%,.0f", basePrice)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun ShinkansenTicketCard(booking: JRBooking, onCancel: () -> Unit) {
    val isGreen = booking.ticketType == "GREEN"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Train,
                        contentDescription = "Train Icon",
                        tint = CrimsonRed
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = booking.trainName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = if (isGreen) "GREEN CLASS" else "ORDINARY CLASS",
                    color = if (isGreen) LuxuryGold else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // From / To layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("FROM", fontSize = 10.sp, color = Color.Gray)
                    Text(booking.fromStation, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "To",
                    tint = Color.LightGray,
                    modifier = Modifier.size(28.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text("TO", fontSize = 10.sp, color = Color.Gray)
                    Text(booking.toStation, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details and QR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("DATE: ${booking.date}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("SEAT: ${booking.seat}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                    Text("STATUS: ${booking.status}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (booking.status == "ACTIVE") Color(0xFF2E7D32) else Color.Red)
                }

                // Interactive QR Ticket Trigger
                if (booking.status == "ACTIVE") {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { /* Simulate ticket expansion */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Ticket QR Code",
                            tint = Color.Black,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            }

            if (booking.status == "ACTIVE") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("予約キャンセル（払戻しする）", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MobileICCard() {
    var suicaBalance by remember { mutableStateOf(3420) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2E7D32), Color(0xFF81C784))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mobile Suica Nexus",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "PASSED",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("残高", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(
                    text = "¥ ${String.format("%,d", suicaBalance)}",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("スマホタッチ乗車機能：有効", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                    Button(
                        onClick = { suicaBalance += 1000 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("1,000円チャージ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StationGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "リアルタイム運行情報",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Station status
            listOf(
                "東海道新幹線" to "平常運転",
                "山陽新幹線" to "平常運転",
                "東北新幹線" to "平常運転",
                "北陸新幹線" to "遅延 5分（強風のため）",
                "山手線" to "平常運転"
            ).forEach { (line, status) ->
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
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (status.startsWith("平常")) Color(0xFF4CAF50) else Color(0xFFFF9800))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(line, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    Text(
                        status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (status.startsWith("平常")) Color(0xFF4CAF50) else Color(0xFFFF9800)
                    )
                }
            }
        }
    }
}

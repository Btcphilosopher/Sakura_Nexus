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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CloudCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.SakuraPinkDark
import com.example.ui.theme.SakuraPinkLight
import com.example.ui.viewmodel.Language
import com.example.ui.viewmodel.SakuraViewModel

@Composable
fun MyAccountScreen(viewModel: SakuraViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(0) } // 0: アカウント情報・設定 (Settings), 1: 国家デジタル設計書 (Infrastructure Blueprint)
    val lang by viewModel.currentLanguage.collectAsState()
    val wallet by viewModel.walletAccount.collectAsState()

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
                text = { Text("プロフィール・設定", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("国家デジタル設計図", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
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
                    // Profile Header card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                // Avatar with cherry blossom background
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SakuraPink, SakuraPinkDark)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = wallet.userName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(LuxuryGold.copy(alpha = 0.15f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CardMembership,
                                            contentDescription = "Gold",
                                            tint = LuxuryGold,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "ゴールド特別国民会員 (${wallet.tier})",
                                            color = LuxuryGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("サクラ残高", fontSize = 11.sp, color = Color.Gray)
                                        Text("¥ ${String.format("%,.0f", wallet.balance)}", fontWeight = FontWeight.Bold, color = CrimsonRed)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("付与ポイント", fontSize = 11.sp, color = Color.Gray)
                                        Text("${wallet.points} P", fontWeight = FontWeight.Bold, color = LuxuryGold)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("セキュリティ", fontSize = 11.sp, color = Color.Gray)
                                        Text("最高レベル", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                    }
                                }
                            }
                        }
                    }

                    // Multi-language settings
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Language, contentDescription = "Lang", tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("多言語対応おもてなし設定 (Language)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Text("アプリ全体の言語をいつでも変更可能です。", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Language.JP to "日本語",
                                        Language.EN to "English",
                                        Language.CN to "简体中文",
                                        Language.KR to "한국어"
                                    ).forEach { (langCode, label) ->
                                        val isSelected = lang == langCode
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.setLanguage(langCode)
                                                Toast.makeText(context, "${label}に設定しました。", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("lang_${langCode.name}"),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) SakuraPinkLight else Color.Transparent,
                                                contentColor = if (isSelected) CrimsonRed else Color.Gray
                                            ),
                                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed) else null
                                        ) {
                                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Reset button
                    item {
                        Button(
                            onClick = {
                                viewModel.clearAIChat()
                                Toast.makeText(context, "AIコンシェルジュ履歴がクリアされました。", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("コンシェルジュ会話履歴を初期化", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                1 -> {
                    // Digital National Infrastructure Master Design Spec! (CONCEPTUAL ENTERPRISE INFORMATION)
                    item {
                        Text(
                            text = "サクラ・ネクサス 国家デジタルインフラ構想",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    item {
                        BlueprintSection("1. UI/UX設計仕様", Icons.Default.Palette) {
                            Text(
                                "「和×未来×静かな高級感」を体現した独自デザイン。桜色（#FFB7C5）は生命力と品格を表し、深い藍色（#0F1E36）で国家基盤としての信頼性を表現。余白の美を重視したグリッド、Material 3仕様のタッチターゲット（48dp保証）、エッジトゥエッジ（全画面没入）が標準搭載されています。日本の「おもてなし精神」を多言語ローカライズ（日・英・中・韓）にて、AI音声・テキストを用いて提供します。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("2. アプリ構成画面一覧", Icons.Default.Dashboard) {
                            Text(
                                "• JR Next: 全国新幹線・私鉄・バス統合ナビ。スマホICタッチ乗車・QRきっぷ発行。\n" +
                                "• SakuraMart: オンラインコンビニ。需要予測、バーコードセルフ決済、無人受取。\n" +
                                "• Sakura Health: 全国共通電子カルテ(EHR)、遠隔オンライン診療、バイタル自動同期。\n" +
                                "• Sakura Living: 3Dバーチャル内覧、ブロックチェーン電子不動産契約、スマート住宅管理。\n" +
                                "• Sakura Drive: Level 4自動運転タクシー配車、EV充電スポット、IoT駐車場予約。\n" +
                                "• Wallet: マイナンバーカード紐付けセキュア決済、共通ポイント、身元確認決済。\n" +
                                "• AI Concierge: ライフスタイル支援マルチモーダルAI、常時言語切り替え。",
                                fontSize = 11.sp, lineHeight = 16.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("3. データベース設計（Room/PostgreSQL）", Icons.Default.Storage) {
                            Text(
                                "• wallet_accounts: ID、残高、ポイント、所有者情報。地方自治体給付金給付スキーム連携。\n" +
                                "• wallet_transactions: 決済・入出金ログ。カテゴリー分類、安全検知シグナル格納。\n" +
                                "• jr_bookings: 新幹線予約情報、座席位置、有効期限、電子暗号化QRデータ。\n" +
                                "• mart_cart_items: カート一時保存、数量、カテゴリー、店番。\n" +
                                "• health_activities: 歩数、心拍、睡眠、カロリー、総合健康評価スコア。個人情報匿名化（Anonymized IP）を適用。\n" +
                                "• PostgreSQL/Spanner（サーバー側）: マイクロサービス別にSharded/Distributed DBを構築、ゼロトラストアクセスに完全対応。",
                                fontSize = 11.sp, lineHeight = 16.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("4. API設計・連携インターフェース", Icons.Default.Api) {
                            Text(
                                "• POST /api/v1/wallet/charge : ウォレット入金（クレジットカード/銀行口座連携/暗号鍵検証）\n" +
                                "• POST /api/v1/jr/book : 新幹線チケット予約（座席競合トランザクション保護/二重決済防止）\n" +
                                "• GET /api/v1/health/telemetry : センサー端末生体情報同期（暗号化ストリーミング転送）\n" +
                                "• POST /api/v1/ai/consult : AIおもてなしコンシェルジュ（Gemini 3.5 Flash 高速API直結、コンテキストセッション保持）",
                                fontSize = 11.sp, lineHeight = 16.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                    }

                    item {
                        BlueprintSection("5. システムアーキテクチャ（超高可用性）", Icons.Default.CloudCircle) {
                            Text(
                                "• クラウドインフラ: Multi-Region Active-Active構成。Spanner/Redis併用によるミリ秒台の同期。\n" +
                                "• API Gateway: 流量制限、認証/認可（OAuth 2.1）、DDoS防御、分散トラッキング。\n" +
                                "• マイクロサービス: Kubernetesクラスタ上に展開された独立稼働サービス群。交通、小売、医療が障害発生時にも縮退運転を維持可能とする障害隔離（Circuit Breaker）を設計。\n" +
                                "• セキュリティ: TLS 1.3、エンドツーエンドデータの準同型暗号化。ゼロトラストアクセスネットワーク。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("6. 企業・行政向け管理ポータル (Enterprise Portal)", Icons.Default.Analytics) {
                            Text(
                                "• 交通事業者: 列車編成、運行ダイヤ実時間分析、料金動的価格最適化(Dynamic Pricing)。\n" +
                                "• 小売企業: リアルタイム店舗棚在庫監視、AI需要予測に基づく自動発注、CRM分析。\n" +
                                "• 医療機関: 医師シフト管理、病床満空状況、保険適応自動計算。\n" +
                                "• 行政機関: 地域人口移動シミュレーション、災害緊急アナウンス、生活支援給付自動管理ポータル。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("7. ブランド＆ロゴ設計", Icons.Default.Palette) {
                            Text(
                                "「Nexus＝繋がり・絆」。日本伝統のサクラの花弁が「人」「生活」「国家」を結びつける結び目を象り、ゴールドの未来軌道がそれを取り囲むロゴマーク。伝統的な漆器に見られる和紙の風合いと最先端デジタルマトリクスグリッドをブレンド。高級ホテルや格式ある公共機関でも溶け込む洗練された「スマートおもてなし」をシンボライズしています。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("8. MVP開発ロードマップ", Icons.Default.Route) {
                            Text(
                                "• Phase 1 (1〜3ヶ月): Wallet、AI Concierge コアエンジン、マイナンバー認証基盤の開発。\n" +
                                "• Phase 2 (4〜6ヶ月): JR Next 新幹線スマートEX連携、SakuraMartモバイル事前注文。実証実験展開。\n" +
                                "• Phase 3 (7〜12ヶ月): Sakura Health 電子カルテ＆緊急オンライン受診連携、スマート不動産β版搭載。\n" +
                                "• Phase 4 (13ヶ月〜): Smart City 東京都/大阪スマート交通区完全自動運転タクシー「Sakura Drive」実運用開始。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }

                    item {
                        BlueprintSection("9. 将来のスマートシティ社会基盤構想", Icons.Default.Engineering) {
                            Text(
                                "「Sakura Nexus」は単なるモバイルアプリを超え、日本の少子高齢化、物流の維持、地方過疎化を解決するための「21世紀型デジタル国家共通インフラ」です。完全キャッシュレス決済とデジタルアイデンティティが、マイナンバーを通じて公共交通、救急、スマート物流グリッドと直結し、あらゆる国民および外国人観光客に平等で、静かな高級感溢れる暮らしをおもてなしいたします。",
                                fontSize = 11.sp, lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun BlueprintSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = title, tint = CrimsonRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Text(if (isExpanded) "閉じる ▲" else "詳細を見る ▼", fontSize = 11.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
                    content()
                }
            }
        }
    }
}



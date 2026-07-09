package com.example.ui.theme

import com.example.ui.viewmodel.Language

object Loc {
    fun get(key: String, lang: Language): String {
        return translations[key]?.get(lang) ?: key
    }

    private val translations = mapOf(
        "app_subtitle" to mapOf(
            Language.JP to "次世代デジタル国家インフラ",
            Language.EN to "Next-Gen Digital National Infrastructure",
            Language.CN to "次世代数字化国家基础设施",
            Language.KR to "차세대 디지털 국가 인프라"
        ),
        "wallet_balance" to mapOf(
            Language.JP to "ウォレット残高",
            Language.EN to "Wallet Balance",
            Language.CN to "钱包余额",
            Language.KR to "지갑 잔액"
        ),
        "points" to mapOf(
            Language.JP to "サクラポイント",
            Language.EN to "Sakura Points",
            Language.CN to "樱花积分",
            Language.KR to "사쿠라 포인트"
        ),
        "charge" to mapOf(
            Language.JP to "チャージ",
            Language.EN to "Charge",
            Language.CN to "充值",
            Language.KR to "충전"
        ),
        "jr_next" to mapOf(
            Language.JP to "JR Next 鉄道",
            Language.EN to "JR Next Rail",
            Language.CN to "JR Next 铁路",
            Language.KR to "JR Next 철도"
        ),
        "sakura_mart" to mapOf(
            Language.JP to "SakuraMart",
            Language.EN to "SakuraMart",
            Language.CN to "樱花超市",
            Language.KR to "사쿠라마트"
        ),
        "sakura_health" to mapOf(
            Language.JP to "医療ケア",
            Language.EN to "Sakura Health",
            Language.CN to "医疗健康",
            Language.KR to "의료 헬스"
        ),
        "sakura_living" to mapOf(
            Language.JP to "スマート不動産",
            Language.EN to "Sakura Living",
            Language.CN to "智能房产",
            Language.KR to "스마트 부동산"
        ),
        "sakura_drive" to mapOf(
            Language.JP to "モビリティ",
            Language.EN to "Sakura Drive",
            Language.CN to "智能出行",
            Language.KR to "스마트 드라이브"
        ),
        "wallet" to mapOf(
            Language.JP to "ウォレット",
            Language.EN to "Wallet",
            Language.CN to "数字钱包",
            Language.KR to "디지털 지갑"
        ),
        "my_account" to mapOf(
            Language.JP to "アカウント",
            Language.EN to "My Account",
            Language.CN to "个人中心",
            Language.KR to "내 계정"
        ),
        "concierge" to mapOf(
            Language.JP to "AIコンシェルジュ",
            Language.EN to "AI Concierge",
            Language.CN to "AI生活秘书",
            Language.KR to "AI 컨시어지"
        ),
        "quick_charge" to mapOf(
            Language.JP to "クイックチャージ",
            Language.EN to "Quick Charge",
            Language.CN to "快捷充值",
            Language.KR to "간편 충전"
        ),
        "recent_tx" to mapOf(
            Language.JP to "最近の利用履歴",
            Language.EN to "Recent Transactions",
            Language.CN to "近期交易记录",
            Language.KR to "최근 이용 내역"
        ),
        "currency_symbol" to mapOf(
            Language.JP to "円",
            Language.EN to "JPY",
            Language.CN to "元",
            Language.KR to "원"
        )
    )
}

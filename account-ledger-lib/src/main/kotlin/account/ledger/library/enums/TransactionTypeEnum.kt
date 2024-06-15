package account.ledger.library.enums

enum class TransactionTypeEnum(val value: BajajDiscountTypeEnum?) {

    NORMAL(value = null),

    TWO_WAY(value = null),

    VIA(value = null),
    CYCLIC_VIA(value = null),

    SPECIAL(value = null),

    BAJAJ_COINS_FLAT(value = BajajDiscountTypeEnum.Flat),
    BAJAJ_COINS_FLAT_WITHOUT_SOURCE(value = BajajDiscountTypeEnum.Flat),
    BAJAJ_COINS_FLAT_WITHOUT_BALANCE_CHECK(value = BajajDiscountTypeEnum.Flat),

    BAJAJ_CASHBACK_FLAT(value = BajajDiscountTypeEnum.Flat),
    BAJAJ_CASHBACK_FLAT_WITHOUT_SOURCE(value = BajajDiscountTypeEnum.Flat),
    BAJAJ_CASHBACK_FLAT_WITHOUT_BALANCE_CHECK(value = BajajDiscountTypeEnum.Flat),

    BAJAJ_COINS_UP_TO(value = BajajDiscountTypeEnum.UpTo),
    BAJAJ_COINS_UP_TO_WITHOUT_SOURCE(value = BajajDiscountTypeEnum.UpTo),
    BAJAJ_COINS_UP_TO_WITHOUT_BALANCE_CHECK(value = BajajDiscountTypeEnum.UpTo),

    BAJAJ_CASHBACK_UP_TO(value = BajajDiscountTypeEnum.UpTo),
    BAJAJ_CASHBACK_UP_TO_WITHOUT_SOURCE(value = BajajDiscountTypeEnum.UpTo),
    BAJAJ_CASHBACK_UP_TO_WITHOUT_BALANCE_CHECK(value = BajajDiscountTypeEnum.UpTo)
}

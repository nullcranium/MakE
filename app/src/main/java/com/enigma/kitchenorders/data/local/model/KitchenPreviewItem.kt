package com.enigma.kitchenorders.data.local.model

data class KitchenPreviewItem(
    val orderId: Long,
    val menuName: String,
    val quantity: Int,
    val notes: String?,
    val customerName: String,
    val menuId: Int
)

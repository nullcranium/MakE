package com.enigma.kitchenorders.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerName: String,
    val createdAt: Long,
    val deliveryDate: Long,
    val status: String = "ACTIVE" // ACTIVE or CANCELLED
)

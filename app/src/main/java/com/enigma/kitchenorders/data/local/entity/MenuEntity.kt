package com.enigma.kitchenorders.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menus")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val currentPrice: Double,
    val isActive: Boolean = true
)

package com.enigma.kitchenorders.domain.repository

import com.enigma.kitchenorders.data.local.entity.MenuEntity
import com.enigma.kitchenorders.data.local.entity.OrderEntity
import com.enigma.kitchenorders.data.local.entity.OrderItemEntity
import com.enigma.kitchenorders.data.local.model.KitchenPreviewItem
import kotlinx.coroutines.flow.Flow

interface KitchenRepository {
    // Menu
    fun getAllMenus(): Flow<List<MenuEntity>>
    fun getActiveMenus(): Flow<List<MenuEntity>>
    suspend fun saveMenu(menu: MenuEntity)
    suspend fun deleteMenu(menu: MenuEntity)
    
    // Order
    suspend fun createOrder(customerName: String, deliveryDate: Long, items: List<OrderItemEntity>)
    fun getActiveOrders(deliveryDate: Long): Flow<List<OrderEntity>>
    fun getKitchenPreview(deliveryDate: Long): Flow<List<KitchenPreviewItem>>
    suspend fun cancelOrder(orderId: Long)
}

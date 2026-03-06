package com.enigma.kitchenorders.data.repository

import com.enigma.kitchenorders.data.local.AppDatabase
import com.enigma.kitchenorders.data.local.entity.MenuEntity
import com.enigma.kitchenorders.data.local.entity.OrderEntity
import com.enigma.kitchenorders.data.local.entity.OrderItemEntity
import com.enigma.kitchenorders.data.local.model.KitchenPreviewItem
import com.enigma.kitchenorders.domain.repository.KitchenRepository
import kotlinx.coroutines.flow.Flow

class KitchenRepositoryImpl(
    private val db: AppDatabase
) : KitchenRepository {
    
    override fun getAllMenus(): Flow<List<MenuEntity>> = db.menuDao().getAllMenus()
    
    override fun getActiveMenus(): Flow<List<MenuEntity>> = db.menuDao().getAllActiveMenus()
    
    override suspend fun saveMenu(menu: MenuEntity) = db.menuDao().insertMenu(menu)
    
    override suspend fun deleteMenu(menu: MenuEntity) = db.menuDao().deleteMenu(menu)
    
    override suspend fun createOrder(customerName: String, deliveryDate: Long, items: List<OrderItemEntity>) {
        val order = OrderEntity(
            customerName = customerName,
            createdAt = System.currentTimeMillis(),
            deliveryDate = deliveryDate
        )
        db.orderDao().placeOrder(order, items)
    }
    
    override fun getActiveOrders(deliveryDate: Long): Flow<List<OrderEntity>> = 
        db.orderDao().getActiveOrdersByDate(deliveryDate)
        
    override fun getKitchenPreview(deliveryDate: Long): Flow<List<KitchenPreviewItem>> =
        db.orderDao().getKitchenPreviewItems(deliveryDate)
        
    override suspend fun cancelOrder(orderId: Long) = db.orderDao().cancelOrder(orderId)
}

package com.enigma.kitchenorders.data.local.dao

import androidx.room.*
import com.enigma.kitchenorders.data.local.entity.OrderEntity
import com.enigma.kitchenorders.data.local.entity.OrderItemEntity
import com.enigma.kitchenorders.data.local.model.KitchenPreviewItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun placeOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        val orderId = insertOrder(order)
        val itemsWithId = items.map { it.copy(orderId = orderId) }
        insertOrderItems(itemsWithId)
    }

    @Query("SELECT * FROM orders WHERE deliveryDate = :deliveryDate AND status = 'ACTIVE' ORDER BY createdAt DESC")
    fun getActiveOrdersByDate(deliveryDate: Long): Flow<List<OrderEntity>>

    @Query("""
        SELECT 
            o.id as orderId,
            oi.menuNameSnapshot as menuName,
            oi.quantity,
            oi.notes,
            o.customerName,
            oi.menuId
        FROM order_items oi
        INNER JOIN orders o ON oi.orderId = o.id
        WHERE o.deliveryDate = :deliveryDate AND o.status = 'ACTIVE'
    """)
    fun getKitchenPreviewItems(deliveryDate: Long): Flow<List<KitchenPreviewItem>>
    
    @Query("UPDATE orders SET status = 'CANCELLED' WHERE id = :orderId")
    suspend fun cancelOrder(orderId: Long)
}

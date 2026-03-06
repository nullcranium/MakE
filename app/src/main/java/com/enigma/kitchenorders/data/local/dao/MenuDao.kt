package com.enigma.kitchenorders.data.local.dao

import androidx.room.*
import com.enigma.kitchenorders.data.local.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Query("SELECT * FROM menus WHERE isActive = 1")
    fun getAllActiveMenus(): Flow<List<MenuEntity>>

    @Query("SELECT * FROM menus")
    fun getAllMenus(): Flow<List<MenuEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity)

    @Update
    suspend fun updateMenu(menu: MenuEntity)
    
    @Query("SELECT * FROM menus WHERE id = :id")
    suspend fun getMenuById(id: Int): MenuEntity?
    
    @Delete
    suspend fun deleteMenu(menu: MenuEntity)
}

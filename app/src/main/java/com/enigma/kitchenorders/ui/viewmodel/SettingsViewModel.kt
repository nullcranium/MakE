package com.enigma.kitchenorders.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enigma.kitchenorders.data.local.entity.MenuEntity
import com.enigma.kitchenorders.domain.repository.KitchenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: KitchenRepository
) : ViewModel() {
    
    val allMenus: StateFlow<List<MenuEntity>> = repository.getAllMenus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun saveMenu(menu: MenuEntity) {
        viewModelScope.launch {
            repository.saveMenu(menu)
        }
    }
    
    fun deleteMenu(menu: MenuEntity) {
        viewModelScope.launch {
            repository.deleteMenu(menu)
        }
    }
}

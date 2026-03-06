package com.enigma.kitchenorders.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.enigma.kitchenorders.data.local.entity.MenuEntity
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    businessName: String,
    menus: List<MenuEntity>,
    onBusinessNameChange: (String) -> Unit,
    onSaveMenu: (MenuEntity) -> Unit,
    onDeleteMenu: (MenuEntity) -> Unit,
    onNavigateBack: () -> Unit
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }
    
    var editingBusinessName by remember { mutableStateOf(businessName) }
    var showAddMenuDialog by remember { mutableStateOf(false) }
    var editingMenu by remember { mutableStateOf<MenuEntity?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var menuToDelete by remember { mutableStateOf<MenuEntity?>(null) }
    
    // For add/edit dialog
    var menuName by remember { mutableStateOf("") }
    var menuPrice by remember { mutableStateOf("") }
    var menuActive by remember { mutableStateOf(true) }
    
    LaunchedEffect(businessName) {
        editingBusinessName = businessName
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingMenu = null
                    menuName = ""
                    menuPrice = ""
                    menuActive = true
                    showAddMenuDialog = true
                },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Tambah Menu") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Business Name Section
            item {
                Text(
                    "Informasi Bisnis",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                OutlinedTextField(
                    value = editingBusinessName,
                    onValueChange = { editingBusinessName = it },
                    label = { Text("Nama Bisnis") },
                    leadingIcon = { Icon(Icons.Default.Store, null) },
                    trailingIcon = {
                        if (editingBusinessName != businessName) {
                            IconButton(onClick = { onBusinessNameChange(editingBusinessName) }) {
                                Icon(Icons.Default.Check, "Simpan", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            item { Divider() }
            
            // Menu List Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Daftar Menu",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${menus.size} menu",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (menus.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Restaurant,
                                null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Belum ada menu",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Tap tombol + untuk menambah",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            items(menus) { menu ->
                ElevatedCard(
                    onClick = {
                        editingMenu = menu
                        menuName = menu.name
                        menuPrice = menu.currentPrice.toLong().toString()
                        menuActive = menu.isActive
                        showAddMenuDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                menu.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                currencyFormat.format(menu.currentPrice),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (!menu.isActive) {
                            AssistChip(
                                onClick = {},
                                label = { Text("Nonaktif") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    labelColor = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
    
    // Add/Edit Menu Dialog
    if (showAddMenuDialog) {
        AlertDialog(
            onDismissRequest = { showAddMenuDialog = false },
            title = { 
                Text(if (editingMenu == null) "Tambah Menu" else "Edit Menu")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = menuName,
                        onValueChange = { menuName = it },
                        label = { Text("Nama Menu") },
                        placeholder = { Text("cth: Nasi Urap Ayam Bakar") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = menuPrice,
                        onValueChange = { menuPrice = it.filter { c -> c.isDigit() } },
                        label = { Text("Harga (Rp)") },
                        placeholder = { Text("cth: 15000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Menu aktif")
                        Switch(
                            checked = menuActive,
                            onCheckedChange = { menuActive = it }
                        )
                    }
                    
                    // Delete button (only show when editing existing menu)
                    if (editingMenu != null) {
                        Divider()
                        TextButton(
                            onClick = {
                                menuToDelete = editingMenu
                                showDeleteConfirmation = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hapus Menu Ini")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = menuPrice.toDoubleOrNull() ?: 0.0
                        val menu = MenuEntity(
                            id = editingMenu?.id ?: 0,
                            name = menuName.trim(),
                            currentPrice = price,
                            isActive = menuActive
                        )
                        onSaveMenu(menu)
                        showAddMenuDialog = false
                    },
                    enabled = menuName.isNotBlank() && menuPrice.isNotBlank()
                ) {
                    Text("SIMPAN")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMenuDialog = false }) {
                    Text("BATAL")
                }
            }
        )
    }
    
    // Delete Confirmation Dialog
    if (showDeleteConfirmation && menuToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteConfirmation = false
                menuToDelete = null
            },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Hapus Menu?") },
            text = {
                Text("Menu \"${menuToDelete?.name}\" akan dihapus secara permanen. Tindakan ini tidak dapat dibatalkan.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        menuToDelete?.let { onDeleteMenu(it) }
                        showDeleteConfirmation = false
                        showAddMenuDialog = false
                        menuToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("HAPUS")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDeleteConfirmation = false
                        menuToDelete = null
                    }
                ) {
                    Text("BATAL")
                }
            }
        )
    }
}

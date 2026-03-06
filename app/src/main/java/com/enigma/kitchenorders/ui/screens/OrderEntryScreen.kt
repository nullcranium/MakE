package com.enigma.kitchenorders.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.enigma.kitchenorders.data.local.entity.MenuEntity
import com.enigma.kitchenorders.data.local.entity.OrderItemEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderEntryScreen(
    menus: List<MenuEntity>,
    cart: List<OrderItemEntity>,
    customerName: String,
    deliveryDate: Long,
    onCustomerNameChange: (String) -> Unit,
    onDeliveryDateChange: (Long) -> Unit,
    onAddItem: (MenuEntity, Int, String?) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onSubmitOrder: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID")) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")) }
    
    // Dialog states
    var showAddItemDialog by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf<MenuEntity?>(null) }
    var itemQuantity by remember { mutableStateOf("1") }
    var itemNotes by remember { mutableStateOf("") }
    var menuDropdownExpanded by remember { mutableStateOf(false) }
    
    // Calculate totals
    val totalItems = cart.size
    val totalQuantity = cart.sumOf { it.quantity }
    val totalPrice = cart.sumOf { it.priceSnapshot * it.quantity }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catat Pesanan") },
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
        bottomBar = {
            // Summary Card
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$totalItems item ($totalQuantity porsi)")
                        Text(
                            currencyFormat.format(totalPrice),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSubmitOrder,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = customerName.isNotBlank() && cart.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Save, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SIMPAN PESANAN")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // Delivery Date Picker
            item {
                OutlinedCard(
                    onClick = {
                        val cal = Calendar.getInstance().apply { timeInMillis = deliveryDate }
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val newCal = Calendar.getInstance().apply {
                                    set(year, month, day, 0, 0, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                onDeliveryDateChange(newCal.timeInMillis)
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Tanggal Pengiriman",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                dateFormat.format(Date(deliveryDate)),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            // Customer Name
            item {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = onCustomerNameChange,
                    label = { Text("Nama Pelanggan") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            // Add Item Button
            item {
                OutlinedButton(
                    onClick = { showAddItemDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = menus.isNotEmpty()
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (menus.isEmpty()) "Belum ada menu" else "Tambah Item")
                }
            }
            
            // Cart Items
            if (cart.isNotEmpty()) {
                item {
                    Text(
                        "Daftar Pesanan",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            itemsIndexed(cart) { index, item ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.menuNameSnapshot,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "${item.quantity}x ${currencyFormat.format(item.priceSnapshot)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!item.notes.isNullOrBlank()) {
                                Text(
                                    "Catatan: ${item.notes}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        Text(
                            currencyFormat.format(item.priceSnapshot * item.quantity),
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { onRemoveItem(index) }) {
                            Icon(
                                Icons.Default.Delete,
                                "Hapus",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
    
    // Add Item Dialog
    if (showAddItemDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddItemDialog = false
                selectedMenu = null
                itemQuantity = "1"
                itemNotes = ""
            },
            title = { Text("Tambah Item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Menu Dropdown
                    ExposedDropdownMenuBox(
                        expanded = menuDropdownExpanded,
                        onExpandedChange = { menuDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedMenu?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pilih Menu") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(menuDropdownExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = menuDropdownExpanded,
                            onDismissRequest = { menuDropdownExpanded = false }
                        ) {
                            menus.forEach { menu ->
                                DropdownMenuItem(
                                    text = { 
                                        Text("${menu.name} - ${currencyFormat.format(menu.currentPrice)}")
                                    },
                                    onClick = {
                                        selectedMenu = menu
                                        menuDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Quantity
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it.filter { c -> c.isDigit() } },
                        label = { Text("Jumlah") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    // Notes
                    OutlinedTextField(
                        value = itemNotes,
                        onValueChange = { itemNotes = it },
                        label = { Text("Catatan (opsional)") },
                        placeholder = { Text("cth: tanpa nasi, pedas extra") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedMenu?.let { menu ->
                            val qty = itemQuantity.toIntOrNull() ?: 1
                            onAddItem(menu, qty, itemNotes.ifBlank { null })
                        }
                        showAddItemDialog = false
                        selectedMenu = null
                        itemQuantity = "1"
                        itemNotes = ""
                    },
                    enabled = selectedMenu != null && (itemQuantity.toIntOrNull() ?: 0) > 0
                ) {
                    Text("TAMBAH")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showAddItemDialog = false 
                    selectedMenu = null
                    itemQuantity = "1"
                    itemNotes = ""
                }) {
                    Text("BATAL")
                }
            }
        )
    }
}

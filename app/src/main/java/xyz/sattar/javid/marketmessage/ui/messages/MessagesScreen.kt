package xyz.sattar.javid.marketmessage.ui.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import xyz.sattar.javid.marketmessage.domain.model.Customer
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.home.RecentMessageItem
import xyz.sattar.javid.marketmessage.ui.messages.components.MessagesContactItem
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel = hiltViewModel(),
    onCreateMessageClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val customerStats = uiState.customerStats?.collectAsLazyPagingItems()
    val recentMessages = uiState.recentMessages?.collectAsLazyPagingItems()

    // State for Edit BottomSheet
    var editingCustomer by remember { mutableStateOf<Customer?>(null) }
    var editedName by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    // State for Delete Dialog
    var deletingCustomer by remember { mutableStateOf<Customer?>(null) }

    HandleEvents(viewModel, onCreateMessageClick)

    Scaffold(
        topBar = {
            AppToolbar(title = "پیام‌ها")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.sendIntent(MessagesIntent.CreateNewMessage) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "ایجاد پیام جدید")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(0) }
                    },
                    text = { Text("مخاطبین") }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    },
                    text = { Text("پیام‌های ارسالی") }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        if (customerStats != null) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(count = customerStats.itemCount) { index ->
                                    val item = customerStats[index]
                                    if (item != null) {
                                        MessagesContactItem(
                                            stats = item,
                                            onEditClick = {
                                                editingCustomer = item.customer
                                                editedName = item.customer.editFullName ?: item.customer.fullName ?: ""
                                            },
                                            onDeleteClick = {
                                                deletingCustomer = item.customer
                                            }
                                        )
                                    }
                                }
                                
                                customerStats.apply {
                                    when {
                                        loadState.refresh is LoadState.Loading -> {
                                            item { 
                                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                    CircularProgressIndicator() 
                                                }
                                            }
                                        }
                                        loadState.append is LoadState.Loading -> {
                                            item { 
                                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                    CircularProgressIndicator() 
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    1 -> {
                        if (recentMessages != null) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(count = recentMessages.itemCount) { index ->
                                    val item = recentMessages[index]
                                    if (item != null) {
                                        RecentMessageItem(message = item)
                                    }
                                }

                                recentMessages.apply {
                                    when {
                                        loadState.refresh is LoadState.Loading -> {
                                            item { 
                                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                    CircularProgressIndicator() 
                                                }
                                            }
                                        }
                                        loadState.append is LoadState.Loading -> {
                                            item { 
                                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                    CircularProgressIndicator() 
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Bottom Sheet
    if (editingCustomer != null) {
        ModalBottomSheet(
            onDismissRequest = { editingCustomer = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "ویرایش نام مخاطب", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("نام") },
                    modifier = Modifier.fillMaxWidth()
                )
                AppButton(
                    text = "ذخیره",
                    onClick = {
                        editingCustomer?.let { customer ->
                            viewModel.sendIntent(MessagesIntent.UpdateCustomerName(customer, editedName))
                        }
                        editingCustomer = null
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Delete Confirmation Dialog
    if (deletingCustomer != null) {
        AlertDialog(
            onDismissRequest = { deletingCustomer = null },
            title = { Text(text = "حذف مخاطب") },
            text = { Text("آیا مطمئن هستید؟ با حذف این مخاطب، تمام سوابق و پیام‌های ارسالی برای او حذف خواهد شد و قابل بازگشت نیست.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deletingCustomer?.let { customer ->
                            viewModel.sendIntent(MessagesIntent.DeleteCustomer(customer))
                        }
                        deletingCustomer = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingCustomer = null }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
private fun HandleEvents(
    viewModel: MessagesViewModel,
    onCreateMessageClick: () -> Unit
) {
    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is MessagesEvent.NavigateToCreateMessage -> onCreateMessageClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    MarketMessageTheme {
        // MessagesScreen() // Requires ViewModel injection setup for preview
    }
}

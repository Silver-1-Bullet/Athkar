package com.istighfar.counter.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import com.istighfar.counter.data.CounterRepository
import com.istighfar.counter.widget.IstighfarWidget
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { CounterRepository(context) }

    val todayCount by repository.todayCountFlow.collectAsState(initial = 0)
    val lifetimeCount by repository.lifetimeCountFlow.collectAsState(initial = 0)
    val selectedDhikr by repository.selectedDhikrFlow.collectAsState(initial = CounterRepository.DHIKR_LIST[0])
    val autoReset by repository.autoResetEnabledFlow.collectAsState(initial = true)

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Title
        Text(
            text = "أذكاري",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Text(
            text = "سبّح واستغفر",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Circular counter card
        CircularCounterCard(
            count = todayCount,
            dhikr = selectedDhikr,
            onTap = {
                scope.launch {
                    repository.incrementCount()
                    IstighfarWidget().updateAll(context)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "اليوم", value = todayCount.toString())
            StatCard(label = "الإجمالي", value = lifetimeCount.toString())
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dhikr selector
        DhikrSelector(
            selected = selectedDhikr,
            onSelect = { dhikr ->
                scope.launch {
                    repository.setSelectedDhikr(dhikr)
                    IstighfarWidget().updateAll(context)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Settings
        SettingsCard(
            autoReset = autoReset,
            onAutoResetChange = { enabled ->
                scope.launch { repository.setAutoReset(enabled) }
            },
            onManualReset = {
                scope.launch {
                    repository.resetTodayCount()
                    IstighfarWidget().updateAll(context)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CircularCounterCard(
    count: Int,
    dhikr: String,
    onTap: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .size(260.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onTap()
                    }
                )
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = count.toString(),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = dhikr,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun DhikrSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "اختر الذكر",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(CounterRepository.DHIKR_LIST) { dhikr ->
                    FilterChip(
                        selected = dhikr == selected,
                        onClick = { onSelect(dhikr) },
                        label = { Text(text = dhikr, fontSize = 14.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    autoReset: Boolean,
    onAutoResetChange: (Boolean) -> Unit,
    onManualReset: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "الإعدادات",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "إعادة التعيين التلقائي عند منتصف الليل",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = autoReset,
                    onCheckedChange = onAutoResetChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onManualReset,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "إعادة تعيين اليوم",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

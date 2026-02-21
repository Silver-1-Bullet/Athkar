package com.istighfar.counter.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.istighfar.counter.data.CounterRepository

class IstighfarWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Single

    override val stateDefinition = PreferencesGlanceStateDefinition

    companion object {
        val todayCountKey = intPreferencesKey("widget_today_count")
        val lifetimeCountKey = intPreferencesKey("widget_lifetime_count")
        val dhikrKey = stringPreferencesKey("widget_dhikr")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = CounterRepository(context)

        // مزامنة حالة الـ Widget من بيانات التطبيق
        val snapshot = repo.getSnapshot()
        updateAppWidgetState(context, id) { prefs ->
            prefs.toMutablePreferences().apply {
                this[todayCountKey] = snapshot.first
                this[lifetimeCountKey] = snapshot.second
                this[dhikrKey] = snapshot.third
            }
        }

        provideContent {
            GlanceTheme {
                val prefs = currentState<Preferences>()
                WidgetContent(
                    dhikr = prefs[dhikrKey] ?: CounterRepository.DHIKR_LIST[0],
                    todayCount = prefs[todayCountKey] ?: 0,
                    lifetimeCount = prefs[lifetimeCountKey] ?: 0
                )
            }
        }
    }

    @Composable
    private fun WidgetContent(
        dhikr: String,
        todayCount: Int,
        lifetimeCount: Int
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surfaceVariant)
                .clickable(actionRunCallback<IncrementAction>())
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dhikr,
                    style = TextStyle(
                        color = GlanceTheme.colors.onSurfaceVariant,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    modifier = GlanceModifier.fillMaxWidth()
                )

                Text(
                    text = todayCount.toString(),
                    style = TextStyle(
                        color = GlanceTheme.colors.primary,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = GlanceModifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "الإجمالي: $lifetimeCount",
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
            
            // زر إعادة التعيين
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = "↺",
                    style = TextStyle(
                        color = GlanceTheme.colors.secondary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = GlanceModifier
                        .clickable(actionRunCallback<ResetTodayAction>())
                        .padding(8.dp)
                )
            }
        }
    }
}

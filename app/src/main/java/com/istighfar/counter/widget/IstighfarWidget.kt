package com.istighfar.counter.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.istighfar.counter.data.CounterRepository

class IstighfarWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: android.content.Context, id: GlanceId) {
        val repo = CounterRepository(context)
        val (todayCount, lifetimeCount, dhikr) = repo.getSnapshot()

        provideContent {
            WidgetContent(
                dhikr = dhikr,
                todayCount = todayCount,
                lifetimeCount = lifetimeCount
            )
        }
    }

    @Composable
    private fun WidgetContent(
        dhikr: String,
        todayCount: Int,
        lifetimeCount: Int
    ) {
        val tealDark = ColorProvider(Color(0xFF0D4D4D))
        val cream = ColorProvider(Color(0xFFF5F0E8))
        val gold = ColorProvider(Color(0xFFD4A843))
        val semiWhite = ColorProvider(Color(0x33FFFFFF))

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(tealDark)
                .clickable(actionRunCallback<IncrementAction>()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dhikr text
                Text(
                    text = dhikr,
                    style = TextStyle(
                        color = cream,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = GlanceModifier.fillMaxWidth()
                )

                Spacer(modifier = GlanceModifier.size(8.dp))

                // Counter box
                Box(
                    modifier = GlanceModifier
                        .background(semiWhite)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = todayCount.toString(),
                        style = TextStyle(
                            color = gold,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.size(8.dp))

                // Bottom row: lifetime count + reset button
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الكل: $lifetimeCount",
                        style = TextStyle(
                            color = cream,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        ),
                        modifier = GlanceModifier.wrapContentHeight()
                    )

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    Text(
                        text = "↺",
                        style = TextStyle(
                            color = gold,
                            fontSize = 20.sp,
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
}

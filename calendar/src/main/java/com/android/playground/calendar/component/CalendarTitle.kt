package com.android.playground.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.playground.calendar.ui.theme.Black4
import com.android.playground.calendar.ui.theme.Marron3
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarTitle(
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
    goToPreviousMonth: () -> Unit,
    goToNextMonth: () -> Unit,
) {
    Row(
        modifier = modifier.height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(4.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = goToPreviousMonth
                ),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            tint = Marron3,
            contentDescription = null
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = Black4
        )
        Icon(
            modifier = Modifier
                .padding(4.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = goToNextMonth
                ),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = Marron3,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarTitlePreview() {
    CalendarTitle(
        currentMonth = YearMonth.now(),
        goToPreviousMonth = {},
        goToNextMonth = {}
    )
}
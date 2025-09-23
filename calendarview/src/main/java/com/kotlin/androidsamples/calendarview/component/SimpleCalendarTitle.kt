package com.kotlin.androidsamples.calendarview.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.androidsamples.calendarview.Utils.displayText
import java.time.YearMonth

@Composable
fun SimpleCalendarTitle(
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarNavigationIcon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous",
            isHorizontal = isHorizontal,
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
        CalendarNavigationIcon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            isHorizontal = isHorizontal,
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isHorizontal: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        val rotation by animateFloatAsState(
            targetValue = if (isHorizontal) 0f else 90f,
            label = "CalendarNavigationIconAnimation"
        )
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.Center)
                .rotate(rotation),
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleCalendarTitlePreview() {
    SimpleCalendarTitle(
        currentMonth = YearMonth.now(),
        goToPrevious = {},
        goToNext = {}
    )
}
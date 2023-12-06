package com.ddanilov.workshop.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenItem(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Red)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart, // Red border with 2dp thickness
    ) {
        Text(text = value)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenItemPreview() {
    HomeScreenItem(value = "Hello", onClick = {})
}

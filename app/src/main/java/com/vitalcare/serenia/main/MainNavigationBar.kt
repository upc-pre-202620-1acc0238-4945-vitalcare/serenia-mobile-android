package com.vitalcare.serenia.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun MainNavigationBar(
    selectedItem: NavigationItem,
    onItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    simplified: Boolean = false
) {
    val items = if (simplified) {
        listOf(NavigationItem.HOME, NavigationItem.PROFILE)
    } else {
        NavigationItem.entries
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SereniaPalette.Neutro50)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SereniaPalette.Neutro200)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(if (simplified) 8.dp else 4.dp),
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    start = if (simplified) 20.dp else 8.dp,
                    top = 8.dp,
                    end = if (simplified) 20.dp else 8.dp,
                    bottom = 20.dp
                )
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    item = item,
                    isSelected = item == selectedItem,
                    onClick = { onItemClick(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavigationBarItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSelected) SereniaPalette.Teal700 else SereniaPalette.Neutro600

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .heightIn(min = 72.dp)
            .clip(RoundedCornerShape(SereniaRadius.md))
            .background(if (isSelected) SereniaPalette.Teal50 else Color.Transparent)
            .selectable(selected = isSelected, role = Role.Tab, onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(32.dp)
        )
        // One line that shrinks when the font is large, so a tab name is never split in two
        Text(
            text = stringResource(item.title),
            fontFamily = Nunito,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = contentColor,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = 11.sp, maxFontSize = 16.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavigationBarPreview() {
    SereniaTheme {
        Column {
            MainNavigationBar(selectedItem = NavigationItem.HOME, onItemClick = {})
            MainNavigationBar(selectedItem = NavigationItem.HOME, onItemClick = {}, simplified = true)
        }
    }
}

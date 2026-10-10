package com.vitalcare.serenia.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.icon.accountCircle
import com.vitalcare.serenia.core.designsystem.icon.eventAvailable
import com.vitalcare.serenia.core.designsystem.icon.family
import com.vitalcare.serenia.core.designsystem.icon.home
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark

enum class BottomNavItem(val label: String, val icon: ImageVector) {
    HOME("Inicio", home),
    REMINDERS("Recordar", eventAvailable),
    FAMILY("Familia", family),
    PROFILE("Perfil", accountCircle)
}

@Composable
fun BottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(38.dp))
            .background(Brush.horizontalGradient(listOf(forestDark, MaterialTheme.colorScheme.primary)))
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem.entries.forEach { item ->
            val isSelected = item == selectedItem

            Box(
                modifier = Modifier
                    .height(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable(role = Role.Tab) { onItemClick(item) }
                    .padding(horizontal = if (isSelected) 22.dp else 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = forestDark
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    SereniaTheme {
        BottomNavigationBar(selectedItem = BottomNavItem.HOME, onItemClick = {})
    }
}

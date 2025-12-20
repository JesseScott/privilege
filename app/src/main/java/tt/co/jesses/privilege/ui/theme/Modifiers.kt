package tt.co.jesses.privilege.ui.theme

import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Enforces the Android accessibility standard of a minimum 48dp touch target height.
 * Use this on clickable elements like Buttons or Cards to ensure they are accessible.
 */
fun Modifier.minTouchTarget(): Modifier = this.heightIn(min = 48.dp)

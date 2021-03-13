import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = { text = "Hello ${Quickjs().evaluate("1+1", "<test>", 0)}" }) {
            Text(text)
        }
    }
}

expect fun getPlatformName(): String

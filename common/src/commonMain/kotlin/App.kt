import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun App() {
  var text by remember { mutableStateOf(TextFieldValue()) }

  MaterialTheme {
    TextField(
      value = text,
      onValueChange = {
        text = TextFieldValue(
          Quickjs.INSTANCE.hightlight(it.text),
          it.selection,
          it.composition
        )
      }
    )
  }
}

expect fun getPlatformName(): String

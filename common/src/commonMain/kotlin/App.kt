import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

@Composable
fun App() {
  var text by remember { mutableStateOf(TextFieldValue()) }
  var evalval by remember { mutableStateOf("") }
  val quickjs = remember { Quickjs() }

  MaterialTheme {
    Column {
      TextField(
          value = text,
          onValueChange = {
            text = TextFieldValue(Hightlight.hightlight(it.text), it.selection, it.composition)
          },
          modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f))
      Button(onClick = {
        evalval = quickjs.evaluate(text.text, "<eval>", 0)
      }) { Text("Run") }
      Text(evalval)
    }
  }
}

expect fun getPlatformName(): String

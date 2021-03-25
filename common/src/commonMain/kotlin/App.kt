import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

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
          modifier = Modifier.fillMaxWidth().weight(1f))
      Row {
        Button(onClick = { evalval = quickjs.evaluate(text.text, "<eval>", 0) }) { Text("Run") }
        Button(onClick = { evalval = "ffmpeg ctx=${ Ffmpeg().avformatAllocContext() }" }) {
          Text("Test FFmpeg")
        }
      }
      Text(text = evalval, modifier = Modifier.padding(10.dp).weight(1f))
    }
  }
}

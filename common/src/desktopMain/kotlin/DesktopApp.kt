actual fun jniLoadLibrary(name: String) {
  val fname = "lib$name.dll"
  val ins = object {}::class.java.getResourceAsStream(fname)
  val file = java.io.File(System.getProperty("java.io.tmpdir") + "/" + fname)
  val fos = file.outputStream()
  ins.copyTo(fos)
  ins.close()
  fos.close()
  System.load(file.toString())
}

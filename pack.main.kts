@file:Repository("https://repo.maven.apache.org/maven2")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
@file:Suppress("OPT_IN_USAGE")

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

val json: Json = Json {
  prettyPrint = false
}

fun ZipOutputStream.minifyAndCopy(
  name: String,
) {
  putNextEntry(ZipEntry(name))
  json.encodeToStream(
    Json.decodeFromStream<JsonObject>(
      Paths
        .get(name)
        .inputStream()
        .buffered()
    ),
    this,
  )
}

ZipOutputStream(
  Paths
    .get("empty_exception_messages.zip")
    .outputStream()
    .buffered()
).use {
  it.minifyAndCopy("pack.mcmeta")
  it.minifyAndCopy("assets/minecraft/lang/en_us.json")
}

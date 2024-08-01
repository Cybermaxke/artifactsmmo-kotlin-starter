package artifactsmmo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import feign.Target
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.kotlin.CoroutineFeign
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private object Clients

private fun initFeign(): CoroutineFeign<Unit> {
  val token = Clients::class.java.getResourceAsStream("/token").use { inputStream ->
    requireNotNull(inputStream)
    BufferedReader(InputStreamReader(inputStream)).readText()
  }
  val objectMapper = ObjectMapper()
    .registerModules(JavaTimeModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  return CoroutineFeign.builder<Unit>()
    .decoder(JacksonDecoder(objectMapper))
    .encoder(JacksonEncoder(objectMapper))
    .requestInterceptor { template ->
      template.header("Authorization", "Bearer $token")
      if (template.body() != null && template.body().isNotEmpty() && !template.headers().contains("Content-Type")) {
        template.header("Content-Type", "application/json")
      }
    }
    .build()
}

private const val baseUrl = "https://api.artifactsmmo.com"
private val feign = initFeign()
private val clients = ConcurrentHashMap<Class<*>, Any>()

@Suppress("UNCHECKED_CAST")
private fun <T : Any> client(type: Class<T>): T {
  return clients.computeIfAbsent(type) { feign.newInstance(Target.HardCodedTarget(type, baseUrl)) } as T
}

fun <T : Any> client(type: KClass<T>): T = client(type.java)
inline fun <reified T : Any> client(): T = client(T::class)

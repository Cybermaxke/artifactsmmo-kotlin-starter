import artifactsmmo.client
import artifactsmmo.client.MyCharactersClient
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val myCharactersClient = client<MyCharactersClient>()
  val result = myCharactersClient.getMyCharactersMyCharactersGet()
  val characterNames = result.data.map { it.name }
  if (characterNames.isEmpty()) {
    println("Hello? Is somebody here?")
  } else {
    println("Hello " + characterNames.joinToString(", ") + "!")
  }
}

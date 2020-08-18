package reactive

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

class ReactiveTest {

  @Test
  fun sync() {
    println("start")

    val response = runBlockingTask()
    followAction(response)

    // cant do anything until finished waiting
    println("doing other works")

    println("finished main process")
  }

  private fun runBlockingTask(): String {
    Thread.sleep(1000)
    return "response"
  }

  private fun followAction(str: String): String {
    println("finished blocking works: $str")
    return "next"
  }

  @Test
  fun async() {
    println("start")

    val task = runAsyncTask { res ->
      followAction(res)
    }

    // do what you want
    println("doing other works")

    // avoid early exit
    task.join()

    println("finished main process")
  }


  private fun runAsyncTask(callback: (String) -> Unit) = thread {
    Thread.sleep(1000)
    callback("response")
  }

  @Test
  fun `nested async`() {
    println("start")

    fun followActionOf(str: String, callback: (String) -> Unit) {
      println("finished blocking works: $str")
      // use thread pool if you want to execute callback in another thread
      //  threadPool.submit{
      Thread.sleep(1000)
      callback("next")
      //  }
    }

    val task = runAsyncTask { res ->
      followActionOf(res) { next1 ->
        followActionOf(next1) { next2 ->
          followActionOf(next2) { next3 ->
            followActionOf(next3) {
              // done
            }
          }
        }
      }
    }

    // do what you want
    println("doing other works")

    // avoid early exit
    task.join()

    println("finished main process")
  }

  @Test
  fun `completable future`() {
    println("start")
    val future = CompletableFuture
      .supplyAsync(this::runBlockingTask)
      // .thenApply { followAction(it) }
      .thenApplyAsync { followAction(it) }
      .thenApplyAsync { followAction(it) }
      .thenApplyAsync { followAction(it) }
      .thenApplyAsync { followAction(it) }


    // do what you want
    println("doing other works")

    // avoid early exit
    future.get()

    println("finished main process")
  }

  @Test
  fun `nested async with coroutine`() {
    println("start")

    suspend fun followActionCo(str: String): String {
      println("finished blocking works: $str")
      delay(1000)
      return "next"
    }

    runBlocking {
      launch {
        val response = runBlockingTask()
        val next1 = followActionCo(response)
        val next2 = followActionCo(next1)
        val next3 = followActionCo(next2)
        val next4 = followActionCo(next3)
        followAction(next4)
      }

      // do what you want
      println("doing other works")
    }

    println("finished main process")
  }
}

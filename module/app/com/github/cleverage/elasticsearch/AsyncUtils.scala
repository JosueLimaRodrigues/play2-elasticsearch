package com.github.cleverage.elasticsearch
import org.elasticsearch.action.{ActionListener, ActionRequest, ActionRequestBuilder, ActionResponse}
import play.libs.F

import scala.concurrent.{Future, Promise}

/**
 * Utils for managing Asynchronous tasks
 */
object AsyncUtils {
  /**
   * Create a default promise
   * @return
   */
  def createPromise[T](): Promise[T] = Promise[T]()

  /**
   * Execute an Elasticsearch request asynchronously
   * @param requestBuilder
   * @return
   */
  def executeAsync[RQ <: ActionRequest[RQ],RS <: ActionResponse, RB <: ActionRequestBuilder[RQ,RS,RB]](requestBuilder: ActionRequestBuilder[RQ,RS,RB]): Future[RS] = {
    val promise = Promise[RS]()

    requestBuilder.execute(new ActionListener[RS] {
      def onResponse(response: RS) {
        promise.success(response)
      }

      def onFailure(t: Throwable) {
        promise.failure(t)
      }
    })

    promise.future
  }

  /**
   * Execute an Elasticsearch request asynchronously and return a Java Promise
   * @param requestBuilder
   * @return
   */
  def executeAsyncJava[RQ <: ActionRequest[RQ],RS <: ActionResponse, RB <: ActionRequestBuilder[RQ,RS,RB]](requestBuilder: ActionRequestBuilder[RQ,RS,RB]): F.Promise[RS] = {
    F.Promise.wrap(executeAsync(requestBuilder))
  }

}

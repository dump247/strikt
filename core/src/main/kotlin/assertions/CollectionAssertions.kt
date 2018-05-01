package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate("has size $expected") { subject ->
      if (subject.size == expected) {
        success()
      } else {
        failure()
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate("all elements match predicate") { subject ->
      val results = mutableListOf<Result>()
      subject.forEach { element ->
        val compoundAssertion = CollectingAssertion(element)
        compoundAssertion.predicate()
        results.addAll(compoundAssertion.results)
      }

      if (results.all { it.status == Status.Success }) {
        success(results)
      } else {
        failure(results)
      }
    }
  }

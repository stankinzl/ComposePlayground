package com.stanislavkinzl.composeplayground.screens.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

// https://medium.com/@arshamjafari85/mastering-flow-operators-in-kotlin-a-comprehensive-guide-192ac58e7104#:~:text=Resulting%20Flow%3A%20The%20flatMapLatest%20operator,the%20latest%20transformation%20are%20emitted.
@HiltViewModel
class FlowOperatorsSampleScreenVM @Inject constructor() : ViewModel() {

    private val _transformOutput = MutableStateFlow("")
    val transformOutput = _transformOutput.asStateFlow()

    private val _flatMapConcatOutput = MutableStateFlow("")
    val flatMapConcatOutput = _flatMapConcatOutput.asStateFlow()

    private val _flatMapMergeOutput = MutableStateFlow("")
    val flatMapMergeOutput = _flatMapMergeOutput.asStateFlow()

    private val _flatMapLatestOutput = MutableStateFlow("")
    val flatMapLatestOutput = _flatMapLatestOutput.asStateFlow()

    private val _zipOutput = MutableStateFlow("")
    val zipOutput = _zipOutput.asStateFlow()

    private val _conflateOutput = MutableStateFlow("")
    val conflateOutput = _conflateOutput.asStateFlow()

    private val _retryOutput = MutableStateFlow("")
    val retryOutput = _retryOutput.asStateFlow()

    init {
        viewModelScope.launch {
            mapSample()
            filterSample()
            transformSample()
            flatMapContactSample()
            flatMapMergeSample()
            flatMapLatestSample()
            takeExample()
            dropExample()
            collectSample()
            onEachSample()
            catchSample()
            zipSample()
            conflateSample()
            distinctUntilChangedSample()
            retrySample()
        }
    }

    /**
     * 1. map - Transforming Values
     * The map operator is a fundamental operation in Kotlin Flow (and in many other reactive programming libraries)
     * that allows you to transform the elements emitted by a Flow into new elements of a potentially different type.
     * It operates on each item emitted by the source Flow and applies a given transformation function to each item,
     * producing a new Flow with the transformed items. */
    private suspend fun mapSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val transformedFlow: Flow<String> = sourceFlow.map { number ->
            "Transformed: $number"
        }
        transformedFlow.collect { value ->
            println(value)
        }
        // Transformed: 1
        // Transformed: 2
        // Transformed: 3
        // Transformed: 4
        // Transformed: 5
    }

    /**
     * 2. filter - Filtering Values
     * The filter operator allows you to selectively include or exclude elements from a Flow based on a given predicate function.
     * It operates on each item emitted by the source Flow and includes only the items that satisfy the specified condition.*/
    private suspend fun filterSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val filteredFlow: Flow<Int> = sourceFlow.filter { number ->
            number % 2 == 0 // Include only even numbers
        }
        filteredFlow.collect { value ->
            println(value)
        }
        // 2
        // 4
    }

    /**
     * 3. transform - Custom Flow Transformations
     * The transform operator is a versatile and powerful operator in Kotlin Flow that allows you to apply a more complex transformation to each element emitted
     * by a source Flow. It differs from the map operator in that it allows you to emit zero or more elements for each input element and even switch to a
     * different Flow for each input element.*/
    private suspend fun transformSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val transformedFlow: Flow<String> = sourceFlow.transform {
            // Simulate some asynchronous processing with a delay
            delay(100)
            emit("Transformed: $it")
            emit("Additional: $it")
        }
        val output = StringBuilder()
        transformedFlow.collect { value ->
            output.appendLine(value)
            _transformOutput.emit(output.toString())
        }
    }

    /**
     * 4. flatMapConcat - Flattening Nested Flows
     * The flatMapConcat operator is an operator in Kotlin Flow that is used for flattening and merging multiple Flows
     * (or other asynchronous data sources) into a single Flow, while preserving the order of emissions. It applies a transformation
     * function to each item emitted by the source Flow and expects this function to return another Flow. The elements emitted
     * by these nested Flows are then emitted as a single continuous stream in the order in which they were emitted by the
     * nested Flows*/
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun flatMapContactSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3)
        val transformedFlow: Flow<String> = sourceFlow.flatMapConcat {
            return@flatMapConcat flow {
                emit("Start $it")
                delay(100)
                emit("End $it")
            }
        }
        val output = StringBuilder()
        transformedFlow.collect { value ->
            output.appendLine(value)
            _flatMapConcatOutput.emit(output.toString())
        }
    }

    /**
    The flatMapMerge operator is an operator in Kotlin Flow that is used for flattening and merging multiple Flows
    (or other asynchronous data sources) into a single Flow, without necessarily preserving the order of emissions.
    It applies a transformation function to each item emitted by the source Flow and expects this function to return
    another Flow. The elements emitted by these nested Flows are merged into a single stream, and their order is
    not guaranteed to be the same as the order in which they were emitted by the nested Flows.*/
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun flatMapMergeSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3)
        val transformedFlow: Flow<String> = sourceFlow.flatMapMerge { number ->
            flow {
                emit("Start $number")
                delay(100)
                emit("End $number")
            }
        }
        val output = StringBuilder()
        transformedFlow.collect { value ->
            output.appendLine(value)
            _flatMapMergeOutput.emit(output.toString())
        }
        // Start 1
        // Start 2
        // Start 3
        // End 1
        // End 2
        // End 3
        // As you can see, the order of emissions is not guaranteed to match the order of the nested Flows. Elements
        // from different nested Flows can interleave, depending on their completion times.
        //The flatMapMerge operator is useful when you want to concurrently process items in a Flow and merge
        // the results into a single stream without worrying about the order of emissions. This can be beneficial for scenarios
        // where you want to maximize concurrency and don't rely on the specific order of results.
    }

    /**
     * The flatMapLatest operator is an operator in Kotlin Flow that is used for flattening and merging multiple Flows
     * (or other asynchronous data sources) into a single Flow, while ensuring that only the latest emitted item from the
     * source Flow is considered for the transformation.*/
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun flatMapLatestSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3)
        val transformedFlow: Flow<String> = sourceFlow.flatMapLatest { number ->
            flow {
                emit("Start $number")
                delay(100)
                emit("End $number")
            }
        }
        val output = StringBuilder()
        transformedFlow.collect { value ->
            output.appendLine(value)
            _flatMapLatestOutput.emit(output.toString())
        }
        // Start 1
        // Start 2
        // Start 3
        // End 3
        // As you can see, the transformations for items 1 and 2 are canceled when item 3 is emitted by the source Flow.
        // Therefore, only the transformation for item 3 is considered and emitted.
        //The flatMapLatest operator is useful when you want to work with the latest data and cancel any ongoing work related to previous data when new data arrives. This can be especially helpful in scenarios where you want to keep your processing in sync with the most recent updates.
    }

    /**
     * The take operator is to limit the number of elements emitted by a Flow. It takes an integer parameter specifying
     * the maximum number of items you want to receive from the source Flow. */
    private suspend fun takeExample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val takenFlow: Flow<Int> = sourceFlow.take(3)
        takenFlow.collect { value ->
            println(value)
        }
        // 1
        // 2
        // 3
        // The take operator is useful when you want to limit the amount of data you receive from a Flow, especially
        // in scenarios where you are only interested in the initial items or need to prevent excessive data processing
        // or resource consumption.
    }

    /**
     * The drop operator is an operator in Kotlin Flow that allows you to skip a specified number of elements
     * at the beginning of a Flow and emit the remaining elements. It is useful when you want to ignore or
     * "drop" a certain number of items from the start of a Flow before processing the remaining elements. */
    private suspend fun dropExample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val droppedFlow: Flow<Int> = sourceFlow.drop(2)
        droppedFlow.collect { value ->
            println(value)
        }
        // 3
        // 4
        // 5
        // As you can see, the drop operator skips the first two items from the source Flow and emits the remaining items.
        // The drop operator is useful in situations where you have data at the beginning of a Flow that you want to ignore
        // or don't need for your current processing logic. It allows you to efficiently skip over those items and work
        // with the elements that follow.
    }

    /**
     * The collect operator is not an operator; rather, it's a terminal operator or a terminal function used to collect and consume
     * elements emitted by a Flow. When you apply the collect function to a Flow, you can specify a lambda function to process
     * each emitted element. */
    private suspend fun collectSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        sourceFlow.collect { value ->
            // Process each element (in this case, print the element)
            println("Received: $value")
        }
        // Received: 1
        // Received: 2
        // Received: 3
        // Received: 4
        // Received: 5
        // the collect function is a terminal operation that allows you to consume and process elements emitted by a Flow.
        // It's a key component for working with Flow data because it enables you to interact with and handle
        // the values emitted by the Flow in a structured way.
    }

    /**
     * The onEach operator in Kotlin Flow is used to perform a side effect for each element emitted by the Flow
     * while allowing the elements to continue flowing through the Flow unchanged. It doesn't modify the elements
     * but enables you to perform some action, such as logging, debugging, or monitoring, as the elements pass
     * through the Flow. Here's a more detailed explanation:
     * */
    private suspend fun onEachSample() {
        val sourceFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)

        val modifiedFlow: Flow<Int> = sourceFlow
            .onEach { value ->
                // Perform a side effect, such as printing the element
                println("Element: $value")
            }

        modifiedFlow.collect {
            // Process the elements (in this case, just collecting them)
        }
    }

    /**
     * The catch operator is for handling exceptions that might occur during the execution of a Flow and allow you to
     * gracefully recover from those exceptions. It is a mechanism for error handling
     * within Flow streams. Here's a more detailed explanation:
     * */
    private suspend fun catchSample() {
        @Suppress("UNREACHABLE_CODE")
        val sourceFlow: Flow<Int> = flow {
            emit(1)
            emit(2)
            throw RuntimeException("Error occurred!")
            emit(3)
            emit(4)
        }

        val recoveredFlow: Flow<Int> = sourceFlow.catch { exception ->
            // Handle the exception gracefully
            println("Caught exception: ${exception.message}")
            emit(5) // Emit a recovery value
        }

        recoveredFlow.collect { value ->
            println("Received: $value")
        }
        // Received: 1
        // Received: 2
        // Caught exception: Error occurred!
        // Received: 5
    }

    /**
     * The zip operator in Kotlin Flow is used to combine elements from multiple Flows into pairs or
     * tuples. It takes two or more Flow instances as arguments and emits elements as pairs,
     * where each pair contains one element from each of the input Flows. The zip operator aligns
     * elements from the input Flows based on their emission order, and it emits a new element only
     * when all input Flows have emitted an element.
     * */
    private suspend fun zipSample() {
        val numbersFlow: Flow<Int> = flowOf(1, 2, 3, 4, 5)
        val lettersFlow: Flow<String> = flowOf("A", "B", "C", "D", "E")

        val zippedFlow: Flow<Pair<Int, String>> = numbersFlow.zip(lettersFlow) { number, letter ->
            Pair(number, letter)
        }
        val output = StringBuilder()
        zippedFlow.collect { pair ->
            output.appendLine("Received: ${pair.first} - ${pair.second}")
            _zipOutput.emit(output.toString())
        }

        // Output
        // Received: 1 - A
        // Received: 2 - B
        // Received: 3 - C
        // Received: 4 - D
        // Received: 5 - E
    }

    /**
     * The conflate operator is used to control the emission of items when the downstream (the consumer
     * of the flow) is slower than the upstream (the producer of the flow). It is a flow operator that
     * helps in managing backpressure by dropping intermediate emitted values when the downstream
     * cannot keep up with the rate of emissions from the upstream.
     * */
    private suspend fun conflateSample() {
        val flow = flow {
            emit(1)
            delay(100) // Simulate some processing time
            emit(2)
            delay(100) // Simulate some processing time
            emit(3)
        }
        val output = StringBuilder()
        flow
            .conflate() // Use the conflate operator
            .collect { value ->
                delay(200) // Simulate slow processing
                output.appendLine(value)
                _conflateOutput.emit(output.toString())
            }
        // 1
        // 3
    }

    /**
     * The distinctUntilChanged operator in Kotlin Flow is used to filter out consecutive duplicate
     * elements emitted by the source Flow. It ensures that only distinct, non-consecutive elements
     * are emitted downstream.
     * */
    private suspend fun distinctUntilChangedSample() {
        val sourceFlow: Flow<Int> = flow {
            emit(1)
            emit(2)
            emit(2) // Duplicate
            emit(3)
            emit(3) // Duplicate
            emit(3) // Duplicate
            emit(4)
        }

        val distinctFlow: Flow<Int> = sourceFlow.distinctUntilChanged()

        distinctFlow.collect { value ->
            println("Received: $value")
        }

        // Received: 1
        // Received: 2
        // Received: 3
        // Received: 4
    }

    /**
     * The retry operator in Kotlin Flow is used to handle errors by allowing the Flow to be resubscribed and retried
     * when an exception occurs during its execution. It provides a way to recover from errors and continue
     * processing the Flow. Here's a more detailed explanation:
     * */
    private suspend fun retrySample() {
        val sourceFlow: Flow<Int> = flow {
            emit(1)
            emit(2)
            throw RuntimeException("Error occurred!")
            @Suppress("UNREACHABLE_CODE")
            emit(3)
        }.catch {  }
        val retriedFlow: Flow<Int> = sourceFlow.retry(2) // Retry up to 2 times
        val output = StringBuilder()
        retriedFlow.collect { value ->
            output.appendLine(value)
            _retryOutput.emit(output.toString())
        }

        // Received: 1
        // Received: 2
        // Received: 1
        // Received: 2
        // Received: 1
        // Received: 2
    }
}
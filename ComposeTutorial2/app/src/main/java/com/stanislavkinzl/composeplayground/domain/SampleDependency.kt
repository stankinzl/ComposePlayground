package com.stanislavkinzl.composeplayground.domain

class SampleDependency(
    private val sampleDependency2: SampleDependency2
) {
    fun dependencyThing() = "What do I do? I do: ${sampleDependency2.dependencyThing2Nested()}"
}
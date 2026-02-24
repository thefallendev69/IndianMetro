package com.thefallendeveloper.indianmetro.corecommon.basetest

actual object DelegateLifecycleInvoker {
    actual fun beforeEach(testInstance: Any) {
        delegatedSupportsInOrder(testInstance)
            .mapNotNull(DelegatedSupport::beforeTestSupport)
            .forEach(BeforeTestSupport::beforeTest)
    }

    actual fun afterEach(testInstance: Any) {
        delegatedSupportsInOrder(testInstance)
            .asReversed()
            .mapNotNull(DelegatedSupport::afterTestSupport)
            .forEach(AfterTestSupport::afterTest)
    }

    private fun delegatedSupportsInOrder(testInstance: Any): List<DelegatedSupport> {
        val delegatedSupportsByInstance = linkedMapOf<Any, DelegatedSupport>()

        var classDepth = 0
        var currentClass: Class<*>? = testInstance.javaClass
        while (currentClass != null && currentClass != Any::class.java) {
            currentClass.declaredFields
                .asSequence()
                .filter { it.name.startsWith("\$\$delegate_") }
                .sortedBy(::delegateIndex)
                .forEach { field ->
                    field.isAccessible = true
                    val delegateInstance = field.get(testInstance) ?: return@forEach
                    val beforeSupport = delegateInstance as? BeforeTestSupport
                    val afterSupport = delegateInstance as? AfterTestSupport

                    if (beforeSupport != null || afterSupport != null) {
                        delegatedSupportsByInstance[delegateInstance] =
                            DelegatedSupport(
                                beforeTestSupport = beforeSupport,
                                afterTestSupport = afterSupport,
                                classDepth = classDepth,
                                delegateOrder = delegateIndex(field),
                            )
                    }
                }

            classDepth += 1
            currentClass = currentClass.superclass
        }

        return delegatedSupportsByInstance.values
            .sortedWith(compareBy(DelegatedSupport::classDepth, DelegatedSupport::delegateOrder))
    }

    private fun delegateIndex(field: java.lang.reflect.Field): Int {
        val prefix = "\$\$delegate_"
        val index = field.name.removePrefix(prefix).toIntOrNull()
        if (index != null) {
            return index
        }
        return Int.MAX_VALUE
    }

    private data class DelegatedSupport(
        val beforeTestSupport: BeforeTestSupport?,
        val afterTestSupport: AfterTestSupport?,
        val classDepth: Int,
        val delegateOrder: Int,
    )
}

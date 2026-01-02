package xyz.sattar.javid.marketmessage.domain.model

enum class MessageVariable(val label: String, val value: String, val fakeValue: String) {
    NAME("نام مخاطب", "{NAME}", "سارا");

    companion object {
        fun replaceVariables(text: String, variableProvider: (MessageVariable) -> String): String {
            var result = text
            entries.forEach { variable ->
                result = result.replace(variable.value, variableProvider(variable))
            }
            return result
        }
    }
}

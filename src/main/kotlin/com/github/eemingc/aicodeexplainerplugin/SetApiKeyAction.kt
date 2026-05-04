package com.github.eemingc.aicodeexplainerplugin

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class SetApiKeyAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val key = Messages.showInputDialog(
            "Enter OpenRouter API Key:",
            "Set API Key",
            null
        )

        if (!key.isNullOrBlank()) {
            PropertiesComponent.getInstance()
                .setValue("OPENROUTER_API_KEY", key)

            Messages.showInfoMessage(
                "API key saved successfully.",
                "AI Code Explainer"
            )
        }
    }
}
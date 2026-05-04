package com.github.eemingc.aicodeexplainerplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages

import com.github.eemingc.aicodeexplainerplugin.services.AiService


class ExplainCodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)

        if (editor == null) {
            return
        }

        val selectedText = editor.selectionModel.selectedText

        if (selectedText.isNullOrEmpty()) {
            Messages.showMessageDialog(
                "Please select some code first.",
                "AI Code Explainer",
                Messages.getInformationIcon()
            )
            return
        }

        val aiService = AiService()

        val result = try {
            aiService.explainCode(selectedText)
        } catch (e: Exception) {
            "Error: ${e.message}"
        }

        Messages.showMessageDialog(
            result,
            "AI Code Explanation",
            Messages.getInformationIcon()
        )
    }
}
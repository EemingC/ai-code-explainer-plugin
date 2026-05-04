package com.github.eemingc.aicodeexplainerplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages

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

        Messages.showMessageDialog(
            "Selected code:\n$selectedText",
            "AI Code Explainer",
            Messages.getInformationIcon()
        )
    }
}
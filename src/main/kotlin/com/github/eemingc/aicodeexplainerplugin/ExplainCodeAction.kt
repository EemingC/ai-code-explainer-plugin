package com.github.eemingc.aicodeexplainerplugin

import com.github.eemingc.aicodeexplainerplugin.services.AiService
import com.github.eemingc.aicodeexplainerplugin.toolWindow.MyToolWindowFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task

class ExplainCodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return

        val selectedText = editor.selectionModel.selectedText

        if (selectedText.isNullOrBlank()) {
            ApplicationManager.getApplication().invokeLater {
                MyToolWindowFactory.MyToolWindowState.contentArea?.text =
                    "Please select some code first."
            }
            return
        }

        MyToolWindowFactory.MyToolWindowState.contentArea?.text = "Thinking..."

        val aiService = AiService()

        object : Task.Backgroundable(project, "Explaining code...", false) {
            override fun run(indicator: ProgressIndicator) {
                val result = try {
                    aiService.explainCode(selectedText)
                } catch (e: Exception) {
                    "Error: ${e.message}"
                }

                ApplicationManager.getApplication().invokeLater {
                    MyToolWindowFactory.MyToolWindowState.contentArea?.text = result
                }
            }
        }.queue()
    }
}
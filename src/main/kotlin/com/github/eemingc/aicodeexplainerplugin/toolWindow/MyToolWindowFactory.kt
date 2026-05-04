package com.github.eemingc.aicodeexplainerplugin.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class MyToolWindowFactory : ToolWindowFactory {

    object  MyToolWindowState {
        var contentArea: JTextArea? = null
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val textArea = JTextArea()
        textArea.lineWrap = true
        textArea.wrapStyleWord = true

        MyToolWindowState.contentArea = textArea

        val scrollPane = JBScrollPane(textArea)

        panel.add(scrollPane)

        val content = toolWindow.contentManager.factory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
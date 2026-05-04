package com.github.eemingc.aicodeexplainerplugin.services

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.intellij.ide.util.PropertiesComponent

class AiService {


    private val client = OkHttpClient()
    private val apiKey: String
        get() = PropertiesComponent.getInstance()
            .getValue("OPENROUTER_API_KEY")
            ?: error("API key not set. Please configure OPENROUTER_API_KEY in IDE settings.")

    fun explainCode(code: String): String {
        val prompt = """
        You are IntelliJ IDEA's static code inspection engine.
        
        Your job is to analyze code like IntelliJ inspections, NOT like a chatbot.
        
        Rules:
        - Do NOT explain the whole code
        - Do NOT suggest general improvements
        - Only report concrete findings
        - Every finding must be tied to a specific line or code element
        - If no issues exist, explicitly say: NO INSPECTIONS FOUND
        
        Use this format:
        
        === INSPECTIONS ===
        For each issue:
        [SEVERITY] short description (mention code element if possible)
        
        Severity levels:
        - INFO (style / minor observations)
        - WARNING (potential issue)
        - ERROR (likely bug or incorrect usage)
        
        === SUMMARY ===
        1–2 sentences max describing overall code quality
        
        Code:
        $code
        """.trimIndent()

        val json = JSONObject()
        json.put("model", "meta-llama/llama-3-8b-instruct")
        json.put("messages", listOf(
            mapOf("role" to "user", "content" to prompt)
        ))

        val mediaType = "application/json".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://openrouter.ai/api/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("HTTP-Referer", "http://localhost")
            .addHeader("X-Title", "AI Code Explainer Plugin")
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: return "No response"
            val jsonResponse = JSONObject(responseBody)

            return jsonResponse
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
        }
    }
}
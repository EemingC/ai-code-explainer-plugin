package com.github.eemingc.aicodeexplainerplugin.services

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import io.github.cdimascio.dotenv.dotenv

class AiService {

    val dotenv = dotenv()
    private val client = OkHttpClient()
    private val apiKey = dotenv["API_KEY"]

    fun explainCode(code: String): String {
        val prompt = """
        Explain the following code clearly.
        
        Structure your answer in:
        1. What the code does
        2. Key logic
        3. Possible improvements
        4. Potential bugs
        
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
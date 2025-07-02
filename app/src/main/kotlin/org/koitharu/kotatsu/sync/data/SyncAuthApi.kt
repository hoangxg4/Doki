package org.dokiteam.doki.sync.data

import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.dokiteam.doki.core.exceptions.SyncApiException
import org.dokiteam.doki.core.network.BaseHttpClient
import org.dokiteam.doki.core.util.ext.toRequestBody
import org.dokiteam.doki.parsers.util.await
import org.dokiteam.doki.parsers.util.parseJson
import org.dokiteam.doki.parsers.util.parseRaw
import org.dokiteam.doki.parsers.util.removeSurrounding
import javax.inject.Inject

@Reusable
class SyncAuthApi @Inject constructor(
	@BaseHttpClient private val okHttpClient: OkHttpClient,
) {

	suspend fun authenticate(syncURL: String, email: String, password: String): String {
		val body = JSONObject(
			mapOf("email" to email, "password" to password),
		).toRequestBody()
		val request = Request.Builder()
			.url("$syncURL/auth")
			.post(body)
			.build()
		val response = okHttpClient.newCall(request).await()
		if (response.isSuccessful) {
			return response.parseJson().getString("token")
		} else {
			val code = response.code
			val message = response.parseRaw().removeSurrounding('"')
			throw SyncApiException(message, code)
		}
	}
}

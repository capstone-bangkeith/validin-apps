package com.safiraak.validin.data

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) : Response {
        val req = chain.request()
        val basicReq = req.newBuilder()
            if (token != null) {
                basicReq.addHeader("token", "$token")
            }
        return chain.proceed(basicReq.build())
    }
    fun setToken(token: String?) {
        this.token = token
    }
}
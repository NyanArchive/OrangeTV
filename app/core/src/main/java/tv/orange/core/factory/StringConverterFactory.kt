package tv.orange.core.factory

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class StringConverterFactory private constructor() : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotationArr: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody?, *>? {
        return if (String::class.java == type) {
            Converter<ResponseBody?, Any> { it.string() }
        } else null
    }

    companion object {
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }
}
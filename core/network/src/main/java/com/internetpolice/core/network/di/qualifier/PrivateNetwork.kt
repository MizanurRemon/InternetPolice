package com.internetpolice.core.network.di.qualifier

import com.internetpolice.core.network.di.TypeEnum
import java.lang.annotation.Documented
import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PrivateNetwork(val value: TypeEnum)
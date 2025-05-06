package com.example

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.MapKey
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding
import kotlin.reflect.KClass


@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class AuthenticatorTypeKey(val value: KClass<out Authenticator.Type>)

@Inject
@SingleIn(AppScope::class)
internal class ExampleController(
    val platform: Platform,
    val authFactory: Authenticator.Factory<Authenticator>
)

@Inject
internal class ExampleDep(val platform: Platform)

interface Authenticator {
    interface Factory<T : Authenticator> {
        fun create(type: Type): T
    }

    sealed class Type {
        data object PhoneNumber : Type()
    }
}

@ContributesBinding(AppScope::class)
@Inject
@SingleIn(AppScope::class)
internal class AuthenticatorFactory(
    private val authenticators: Map<KClass<out Authenticator.Type>, Authenticator>
) : Authenticator.Factory<Authenticator> {
    override fun create(type: Authenticator.Type): Authenticator {
        return authenticators.getValue(type::class)
    }
}

@ContributesIntoMap(
    scope = AppScope::class,
    binding = binding<@AuthenticatorTypeKey(Authenticator.Type.PhoneNumber::class) Authenticator>(),
)
@SingleIn(AppScope::class)
@Inject
internal class PhoneAuthenticator(val dep: ExampleDep) : Authenticator

@ContributesTo(AppScope::class)
internal interface DataProviders {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        fun providePlatform(): Platform = Platform()
    }
}

@DependencyGraph(AppScope::class)
internal interface ExampleDependencyGraph {
    val controller: ExampleController
}
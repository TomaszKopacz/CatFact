package com.example.catfact.di

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Qualifier
@Retention(value = AnnotationRetention.BINARY)
annotation class RemoteRepository

package com.example.test.navigation.arg

import androidx.lifecycle.SavedStateHandle
import com.example.test.deserialize
import com.example.test.navigation.usersArg
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RecipientNavigationModule {

    @Provides
    @ViewModelScoped
    fun provideArg(savedStateHandle: SavedStateHandle) : RecipientNavigationArg {
        val json: String? = savedStateHandle[usersArg]
        return RecipientNavigationArg(users = json?.deserialize())
    }
}
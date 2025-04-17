package com.potaninpm.soundr.presentation.navigation

import kotlinx.serialization.Serializable

sealed class RootNavDestinations {

 /*   @Serializable
    data object Splash : RootNavDestinations()*/

    @Serializable
    data object Home : RootNavDestinations()

    @Serializable
    data object Training : RootNavDestinations()

    @Serializable
    data object Welcome : RootNavDestinations()

    @Serializable
    data object Profile : RootNavDestinations()

    @Serializable
    data object Calendar : RootNavDestinations()
}
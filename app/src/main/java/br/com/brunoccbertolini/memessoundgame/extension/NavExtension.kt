package br.com.brunoccbertolini.memessoundgame.extension

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import br.com.brunoccbertolini.memessoundgame.R

private val slideLeftOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopEnterAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimations(
    destinationId: Int,
    animation: NavOptions = slideLeftOptions
) {
this.navigate(destinationId, null, animation)
}

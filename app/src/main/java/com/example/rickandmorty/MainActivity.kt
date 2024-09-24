package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmorty.screens.CharacterList
import com.example.rickandmorty.screens.CharactersDetails
import com.example.rickandmorty.ui.theme.RickAndMortyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controleDeNavegacao = rememberNavController()
                    NavHost(
                        navController = controleDeNavegacao,
                        startDestination = "CharacterList"
                    )
                    {
                        composable(route = "CharacterList") { CharacterList(controleDeNavegacao) }
                        composable(
                            route = "CharactersDetails/{id}"
                        ) {backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            CharactersDetails(controleDeNavegacao, id)
                        }
                    }
                }
            }
        }
    }
}



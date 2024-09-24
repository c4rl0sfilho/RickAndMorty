package com.example.rickandmorty.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CharactersDetails(controleDeNavegacao: NavHostController, id: String?) {

    val character = remember { mutableStateOf(Character()) }
    val episodes = remember { mutableStateOf(listOf<Episode>()) } // Lista de episódios
    val idState = remember { mutableStateOf(id?.toIntOrNull() ?: 0) }

    // Chamada para API do personagem
    val callCharacterById = RetrofitFactory().getCharacterService().getCharacterById(idState.value)
    callCharacterById.enqueue(object : Callback<Character> {
        override fun onResponse(call: Call<Character>, response: Response<Character>) {
            character.value = response.body()!!

            // Agora que temos o personagem, chamamos a API para os episódios
            val episodeUrls = character.value.episode ?: emptyList()
            val episodeIds = episodeUrls.map { it.split("/").last().toInt() } // Extrair IDs dos episódios

            // Para cada episódio, fazemos uma chamada de API para buscar os detalhes
            episodeIds.forEach { episodeId ->
                val callEpisodeById = RetrofitFactory().getCharacterService().getEpisodeById(episodeId)
                callEpisodeById.enqueue(object : Callback<Episode> {
                    override fun onResponse(call: Call<Episode>, response: Response<Episode>) {
                        val episode = response.body()!!
                        episodes.value = episodes.value + episode // Adiciona o episódio à lista
                    }

                    override fun onFailure(call: Call<Episode>, t: Throwable) {
                        Log.e("API Error", "Failed to load episode with ID: $episodeId")
                    }
                })
            }
        }

        override fun onFailure(call: Call<Character>, t: Throwable) {
            Log.e("API Error", "Failed to load character")
        }
    })

    Surface {
        Image(
            painter = painterResource(id = R.drawable.dark_universe_bg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = character.value.image,
                    contentDescription = character.value.name,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .border(
                            width = 5.dp,
                            color = Color(0xFF00BCD4),
                            shape = RoundedCornerShape(100.dp)
                        )
                )
                Column {
                    Text(text = character.value.name, color = Color.White)
                    Text(text = character.value.species, color = Color.White)
                    Text(text = character.value.gender, color = Color.White)
                    Text(text = character.value.status, color = Color.White)
                    Text(text = character.value.type, color = Color.White)
                }
            }
            character.value.origin?.let { Text(text = it.name, color = Color.White) }
            character.value.location?.let { Text(text = it.name, color = Color.White) }
            Text(text = "Episódios:", color = Color.White)
            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .border(
                        width = 1.dp, color = Color(0xFF00BCD4), shape = RoundedCornerShape(10.dp)
                    )
            ) {
                items(episodes.value) { episode ->
                    Text(
                        text = "${episode.episode} - ${episode.name} ", // Exibe o nome do episódio
                        color = Color.White,
                        modifier = Modifier.padding(all = 5.dp)
                    )
                }
            }
            Image(
                painterResource(id = R.drawable.logo_rick_and_morty),
                contentDescription = "Logo Rick and Morty",
                alignment = Alignment.Center,
                modifier = Modifier.padding(start = 35.dp, top = 75.dp)
            )
        }
    }
}

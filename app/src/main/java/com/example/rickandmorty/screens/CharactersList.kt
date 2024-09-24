package com.example.rickandmorty.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Result
import com.example.rickandmorty.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CharacterList(controleDeNavegacao: NavHostController){

    var characterList by remember {
        mutableStateOf(listOf<Character>())
    }

    //conexao com a api
    val callCharacters = RetrofitFactory().getCharacterService().getAllCharacters()


    callCharacters.enqueue(object : Callback<Result>{
        override fun onResponse(p0: Call<Result>, p1: Response<Result>) {
            characterList = p1.body()!!.results
        }

        override fun onFailure(p0: Call<Result>, p1: Throwable) {
            TODO("Not yet implemented")
        }

    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Red
        //Image(painterResource(id = R.drawable.dark_universe_bg), contentDescription ="Background")
    ) {
        Image(
            painter = painterResource(id = R.drawable.dark_universe_bg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ajuste o ContentScale conforme necessário
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(painterResource(
                id = R.drawable.logo_rick_and_morty),
                contentDescription = "Logo Rick and Morty",
                alignment = Alignment.Center,
                modifier = Modifier.padding(start = 35.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(characterList){ char ->
                    CharacterCard(character = char, controleDeNavegacao)
                }

            }

            //Button(onClick = { controleDeNavegacao.navigate("CharactersDetails") }) {

            }
        }


}

@Composable
fun CharacterCard(character: Character?, controleDeNavegacao: NavHostController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .height(100.dp),
        border = BorderStroke(width = 3.dp, color = Color(0xFF00BCD4)),
        colors = CardDefaults.cardColors( containerColor = Color.Black),
        onClick = {
            controleDeNavegacao.navigate("CharactersDetails/${character?.id}")
        }

    ) {
        Row (
            modifier = Modifier.fillMaxSize()
        ){
            Card(
                modifier = Modifier.size(100.dp)
            )
            {
            AsyncImage(
                model = character?.image,
                contentDescription = "")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp)
            ){
                Text(
                    text = character?.name!!,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                    )
                Text(
                    text = character.species,
                    color = Color(0xFF00BCD4)
                )
            }
        }
    }
}
//@Preview()
//@Composable
//private  fun CharacterCard(){
//    CharacterCard(character = null)
//}

//@Preview(showSystemUi = true)
//@Composable
//private fun CharacterListPreview(){
//    CharacterList()
//}
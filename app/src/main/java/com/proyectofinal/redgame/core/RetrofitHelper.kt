package com.proyectofinal.redgame.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {




        fun getRetrofit(): Retrofit {

            return Retrofit.Builder()
                .baseUrl("https://api.rawg.io/api/games?key=ea5710fd888a4d6b82220d407aa759e8")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        }



}
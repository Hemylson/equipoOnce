package com.example.equipoonce.di

import android.content.Context
import com.example.equipoonce.data.local.AppDatabase
import com.example.equipoonce.data.remote.RetrofitClient
import com.example.equipoonce.data.repository.PokemonRepository
import com.example.equipoonce.data.repository.RetoRepository
import com.example.equipoonce.utils.GameAudioManager

/**
 * Proveedor de dependencias (inyección de dependencias manual).
 * Garantiza una sola instancia de cada dependencia durante la vida de la app.
 * Se inicializa una vez desde MainActivity con el ApplicationContext.
 */
object RepositoryProvider {

    @Volatile private var retoRepository: RetoRepository? = null
    @Volatile private var pokemonRepository: PokemonRepository? = null
    @Volatile private var audioManager: GameAudioManager? = null

    fun init(context: Context) {
        val appContext = context.applicationContext
        val db = AppDatabase.getDatabase(appContext)
        if (retoRepository == null) retoRepository = RetoRepository(db.retoDao())
        if (pokemonRepository == null) pokemonRepository = PokemonRepository(RetrofitClient.pokemonApiService)
        if (audioManager == null) audioManager = GameAudioManager(appContext)
    }

    fun provideRetoRepository(): RetoRepository =
        retoRepository ?: error("RepositoryProvider no inicializado. Llama init() en MainActivity.")

    fun providePokemonRepository(): PokemonRepository =
        pokemonRepository ?: error("RepositoryProvider no inicializado. Llama init() en MainActivity.")

    fun provideAudioManager(): GameAudioManager =
        audioManager ?: error("RepositoryProvider no inicializado. Llama init() en MainActivity.")
}

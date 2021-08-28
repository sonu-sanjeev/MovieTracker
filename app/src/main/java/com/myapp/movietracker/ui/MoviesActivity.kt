package com.myapp.movietracker.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.ui.theme.MovieTrackerTheme
import com.myapp.movietracker.R
import com.myapp.movietracker.domain.GetMovieListUseCase
import com.myapp.movietracker.domain.GetMovieListUseCaseImpl
import com.myapp.movietracker.domain.MoviesRepository
import com.myapp.movietracker.domain.MoviesRepositoryImpl
import com.myapp.movietracker.ui.viewmodel.MoviesViewModel
import com.myapp.movietracker.ui.viewmodel.MoviesViewModelFactory
import com.myapp.movietracker.util.getDate

class MoviesActivity : ComponentActivity() {

    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl()
    private val getMoviesListUseCase: GetMovieListUseCase =
        GetMovieListUseCaseImpl(moviesRepository)
    private val moviesViewModelFactory = MoviesViewModelFactory(getMoviesListUseCase)
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTrackerTheme {
                moviesViewModel =
                    ViewModelProvider(this, moviesViewModelFactory).get(MoviesViewModel::class.java)
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen(
                        moviesViewModel = moviesViewModel
                    ) { error ->
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(moviesViewModel: MoviesViewModel, onError: (String) -> Unit) {
    val isLoading by moviesViewModel.isLoading.observeAsState(false)
    val movies by moviesViewModel.movies.observeAsState(listOf())
    val error by moviesViewModel.error.observeAsState("")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.my_movie_list)) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_movies),
                        contentDescription = stringResource(R.string.fab_icon),
                    )
                }
            )
        },
        content = {
            MovieListScreen(movies = movies, isLoading = isLoading, error = error) {
                onError(error)
            }
        }
    )
}


@Composable
fun MovieListScreen(
    movies: List<GetMoviesQuery.Node>,
    isLoading: Boolean,
    error: String,
    onError: () -> Unit
) {
    when {
        isLoading -> {
            CenterProgressBar()
        }
        error.isNotBlank() -> {
            onError()
        }
        else -> {
            LazyColumn {
                items(movies) { movie ->
                    MovieCard(movie = movie)
                }
            }
        }
    }
}

@Composable
fun CenterProgressBar() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MovieCard(movie: GetMoviesQuery.Node) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_movies),
                contentDescription = stringResource(R.string.movies_icon),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = movie.title,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Release Date: ${movie.releaseDate.toString().getDate()}",
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Season: ${movie.seasons?.toInt()}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.myapp.movietracker.GetMoviesQuery
import com.myapp.movietracker.ui.theme.MovieTrackerTheme
import com.myapp.movietracker.R
import com.myapp.movietracker.domain.*
import com.myapp.movietracker.ui.theme.SecondaryTextColor
import com.myapp.movietracker.ui.viewmodel.MoviesViewModel
import com.myapp.movietracker.ui.viewmodel.MoviesViewModelFactory
import com.myapp.movietracker.util.getDate

class MoviesActivity : ComponentActivity() {

    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl()
    private val getMoviesListUseCase: GetMovieListUseCase =
        GetMovieListUseCaseImpl(moviesRepository)
    private val createMovieUseCase: CreateMovieUseCase =
        CreateMovieUseCaseImpl(moviesRepository)
    private val moviesViewModelFactory =
        MoviesViewModelFactory(getMoviesListUseCase, createMovieUseCase)
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
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (moviesViewModel.isAddMovies.value == true) {
            moviesViewModel.isAddMovies(false)
            return
        }
        super.onBackPressed()
    }
}

@Composable
fun HomeScreen(moviesViewModel: MoviesViewModel) {
    val isLoading by moviesViewModel.isLoading.observeAsState(false)
    val movies by moviesViewModel.movies.observeAsState(listOf())
    val error by moviesViewModel.error.observeAsState("")
    val isAddMovieScreen by moviesViewModel.isAddMovies.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { moviesViewModel.isAddMovies(true) },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(R.string.fab_icon),
                    )
                }
            )
        },
        content = {
            if (isAddMovieScreen) {
                AddMovieScreen {

                }
            } else {
                MovieListScreen(movies = movies, isLoading = isLoading, error = error)
            }
        }
    )
}


@Composable
fun MovieListScreen(
    movies: List<GetMoviesQuery.Node>,
    isLoading: Boolean,
    error: String,
) {
    when {
        isLoading -> {
            CenterProgressBar()
        }
        error.isNotBlank() -> {
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
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
                    text = "${stringResource(id = R.string.release_date)} ${
                        movie.releaseDate.toString().getDate()
                    }",
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(id = R.string.season)} ${movie.seasons?.toInt()}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun AddMovieScreen(onAddClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_movie),
            modifier = Modifier.padding(all = 8.dp),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.movie_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.release_date)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.season)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /*TODO*/ },
            Modifier
                .width(150.dp)
                .padding(all = 8.dp)
        ) {
            Text(text = stringResource(R.string.add), color = SecondaryTextColor)
        }
    }
}
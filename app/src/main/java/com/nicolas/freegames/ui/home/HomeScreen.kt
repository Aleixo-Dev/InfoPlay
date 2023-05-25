package com.nicolas.freegames.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nicolas.freegames.R
import com.nicolas.freegames.ui.home.components.CardGame
import com.nicolas.freegames.ui.theme.BackgroundApplication
import com.nicolas.freegames.ui.theme.BlackGray
import com.nicolas.freegames.ui.theme.CrayolaColor
import com.nicolas.freegames.ui.theme.QuartzColor
import com.nicolas.freegames.utils.*

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit) { homeViewModel.onEvent(event = GameUiEvent.FetchAllGames) }

    Scaffold(bottomBar = { AdView() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundApplication)
                .padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Categories(viewModel = homeViewModel)
            ContainerGames(
                modifier = Modifier
                    .weight(1f),
                state = uiState,
                viewModel = homeViewModel,
                navController = navController
            )
        }
    }
}


@Composable
fun Categories(
    viewModel: HomeViewModel
) {

    val currentContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.category_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6,
            )
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "",
                modifier = Modifier
                    .size(27.dp)
                    .clickable { ShareGame.shareMyApplication(currentContext) }
            )
        }
        CardContainerSelector(
            categories = MockData.provideCategories(),
            onAllGames = { viewModel.onEvent(GameUiEvent.FetchAllGames) },
            onGetCategory = { categoryName ->
                viewModel.onEvent(
                    GameUiEvent.FetchCategory(
                        categoryName
                    )
                )
            }
        )
    }
}

@Composable
fun CardContainerSelector(
    categories: List<Category>,
    onAllGames: () -> Unit,
    onGetCategory: (String) -> Unit
) {

    var selectedItem by remember { mutableStateOf<Category?>(null) }

    LazyRow {
        items(categories) { category ->
            CardCategories(category = category,
                setCardSelector = selectedItem?.let {
                    category.gameCategory == it.gameCategory
                } ?: false,
                isSelector = {
                    selectedItem = it
                    if (selectedItem?.gameCategory?.title.isNullOrEmpty()) {
                        onAllGames.invoke()
                    } else {
                        onGetCategory.invoke(selectedItem?.gameCategory?.query.toString())
                    }
                }
            )
        }
    }
}


@Composable
fun CardCategories(
    category: Category,
    setCardSelector: Boolean = false,
    isSelector: (Category?) -> Unit,
) {
    val background = if (setCardSelector) CrayolaColor else QuartzColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(width = 110.dp, height = 145.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = background,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    val categorySelected = if (!setCardSelector) category else null
                    isSelector.invoke(categorySelected)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = category.gameCategory.icon),
                contentDescription = "icon_category",
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = category.gameCategory.title,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun ContainerGames(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    viewModel: HomeViewModel,
    navController: NavController
) {
    Column(
        modifier = modifier
            .background(color = BlackGray)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            HomeUiState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            is HomeUiState.Error -> {
                ErrorModal { viewModel.onEvent(GameUiEvent.FetchAllGames) }
            }
            is HomeUiState.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyVerticalGrid(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 5.dp,
                            top = 8.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = state.data) {
                            CardGame(game = it, navController)
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}
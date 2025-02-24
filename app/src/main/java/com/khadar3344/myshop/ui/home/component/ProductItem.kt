package com.khadar3344.myshop.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.khadar3344.myshop.data.network.dto.Product
import com.khadar3344.myshop.util.Dimensions

@Composable
fun ProductItem(
    product: Product,
    onProductClicked: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.spacing_small)
            .clickable { onProductClicked.invoke(product) },
        elevation = CardDefaults.cardElevation(Dimensions.spacing_small),
        shape = RoundedCornerShape(Dimensions.spacing_small)
    ) {
        Column(
            modifier = Modifier
                .clickable { onProductClicked.invoke(product) }
                .fillMaxWidth()
                .padding(Dimensions.spacing_small)
        ) {
            ImageHolder(imageUrl = product.thumbnail)

            Text(
                text = product.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimensions.text_large
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.spacing_small)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rating",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.text_medium
                    ),
                    modifier = Modifier.padding(vertical = Dimensions.spacing_small)
                )
                StarRating(rating = product.rating.toFloat())
            }

            Text(
                text = "$${product.price}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimensions.text_medium,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.spacing_small),
            )
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClicked: (Product) -> Unit,
) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(Dimensions.gridColumns),
        modifier = Modifier
            .fillMaxSize()
            .height(700.dp),
    ) {
        items(products.size, key = { products[it].id }) {
            ProductItem(product = products[it], onProductClicked = onProductClicked)
        }
    }
}

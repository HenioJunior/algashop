package com.henio.algashop.ordering.infrastructure.product.client.http;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductCatalogService;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.product.ProductName;
import com.henio.algashop.ordering.presentation.BadGatewayException;
import com.henio.algashop.ordering.presentation.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {

    private final ProductCatalogAPIClient productCatalogAPIClient;

    @Override
    public Optional<Product> ofId(ProductId productId) {
        ProductResponse productResponse;

        try {
            productResponse = productCatalogAPIClient.getById(productId.value().toString());

        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Product Catalog API Timeout", e);

        } catch(HttpClientErrorException.NotFound e) {
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            throw new BadGatewayException("Product Catalog API Bad Gateway", e);
        }

        return Optional.of(
                Product.builder()
                        .id(new ProductId(productResponse.getId()))
                        .name(new ProductName(productResponse.getName()))
                        .price(new Money(productResponse.getSalePrice()))
                        .inStock(productResponse.getInStock())
                        .build()
        );
    }
}

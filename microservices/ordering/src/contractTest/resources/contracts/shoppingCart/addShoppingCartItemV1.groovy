package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        urlPath("/api/v1/shopping-carts/01226N0640J7Q/items")

        headers {
            contentType("application/json")
        }

        body([
                productId: value(
                        test("0S66P0865N43J"),
                        stub(regex('[0-9A-HJKMNP-TV-Z]{13}'))
                ),
                quantity: value(
                        test(2),
                        stub(anyPositiveInt())
                )
        ])
    }

    response {
        status 204
    }
}
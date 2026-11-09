package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/shopping-carts"

        headers {
            contentType("application/json")
        }

        body([
                customerId: value(
                        test("0S66P0865N43G"),
                        stub(regex('[0-9A-HJKMNP-TV-Z]{13}'))
                )
        ])
    }

    response {
        status 201

        headers {
            contentType("application/json")
        }

        body([
                id: regex('[0-9A-HJKMNP-TV-Z]{13}'),
                customerId: "0S66P0865N43G",
                totalItems: 3,
                totalAmount: 1250.00,
                items: [
                        [
                                id: regex('[0-9A-HJKMNP-TV-Z]{13}'),
                                productId: regex('[0-9A-HJKMNP-TV-Z]{13}'),
                                name: "Notebook",
                                price: 500.00,
                                quantity: 2,
                                totalAmount: 1000.00,
                                available: anyBoolean()
                        ],
                        [
                                id: regex('[0-9A-HJKMNP-TV-Z]{13}'),
                                productId: regex('[0-9A-HJKMNP-TV-Z]{13}'),
                                name: "Mouse pad",
                                price: 250.00,
                                quantity: 1,
                                totalAmount: 250.00,
                                available: anyBoolean()
                        ]
                ]
        ])
    }
}
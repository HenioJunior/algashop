package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/01226N0640J7Q")
    }

    response {
        status 200

        headers {
            contentType('application/json')
        }

        body([
                id: value(
                        test("01226N0640J7Q"),
                        stub(fromRequest().path(3))
                ),

                customerId: regex('[0-9A-HJKMNP-TV-Z]{13}'),

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
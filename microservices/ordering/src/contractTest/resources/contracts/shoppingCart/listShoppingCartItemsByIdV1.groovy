package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/01226N0640J7Q/items")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                items: [
                        [
                                id: $(consumer('0S91DYB50M41R'),
                                        producer(regex('[0-9A-HJKMNP-TV-Z]{13}'))),
                                productId: anyUuid(),
                                name: "Notebook",
                                price: 500.00,
                                quantity: 2,
                                totalAmount: 1000.00,
                                available: anyBoolean()
                        ],
                        [
                                id: $(consumer('0S91DYB50M41S'),
                                        producer(regex('[0-9A-HJKMNP-TV-Z]{13}'))),
                                productId: anyUuid(),
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
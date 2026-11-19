
package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/550e8400-e29b-41d4-a716-446655440000")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                id: fromRequest().path(3),
                customerId: anyUuid(),
                totalItems: 3,
                totalAmount: 1250.00,
                items: [
                        [
                                id: anyUuid(),
                                productId: anyUuid(),
                                name: "Notebook",
                                price: 500.00,
                                quantity: 2,
                                totalAmount: 1000.00,
                                available: anyBoolean()
                        ],
                        [
                                id: anyUuid(),
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

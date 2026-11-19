
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
                        test("f5ab7a1e-37da-41e1-892b-a1d38275c2f2"),
                        stub(anyUuid())
                )
        ])
    }
    response {
        status 201
        headers {
            contentType('application/json')
        }
        body([
                id: $(consumer('550e8400-e29b-41d4-a716-446655440000'),
                        producer(regex(uuid()))),
                customerId: anyUuid(),
                totalItems: 3,
                totalAmount: 1250.00,
                items: [
                        [
                                id: $(consumer('550e8400-e29b-41d4-a716-446655440001'),
                                        producer(regex(uuid()))),
                                productId: anyUuid(),
                                name: "Notebook",
                                price: 500.00,
                                quantity: 2,
                                totalAmount: 1000.00,
                                available: anyBoolean()
                        ],
                        [
                                id: $(consumer('550e8400-e29b-41d4-a716-446655440002'),
                                        producer(regex(uuid()))),
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

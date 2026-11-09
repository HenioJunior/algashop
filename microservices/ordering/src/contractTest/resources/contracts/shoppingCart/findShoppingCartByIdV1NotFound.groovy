package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/0S66P0865N43J")
    }

    response {
        status 404

        headers {
            contentType("application/problem+json")
        }

        body([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not found",
                status: 404,
                detail: "Shopping cart with id 0S66P0865N43J not found"
        ])
    }
}
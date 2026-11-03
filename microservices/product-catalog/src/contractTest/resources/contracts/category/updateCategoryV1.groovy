package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method PUT()
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
        urlPath("/api/v1/categories/f5ab7a1e-37da-41e1-892b-a1d38275c2f2")
        body([
                name: value(
                        test("Notebooks"),
                        stub(nonBlank())
                ),
                enabled: value(
                        test(true),
                        stub(anyBoolean())
                )
        ])
    }
    response {
        status 204
    }
}

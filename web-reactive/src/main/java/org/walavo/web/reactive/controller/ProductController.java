package org.walavo.web.reactive.controller;

import org.walavo.web.reactive.controller.dto.ProductDTO;
import org.walavo.web.reactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/product/event", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createEvent(@RequestBody ProductDTO productDto) {
        return ResponseEntity.ok(productService.saveAsync(productDto));
    }

    @PostMapping(value = "/product", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody ProductDTO productDto) {
        return ResponseEntity.ok(productService.save(productDto));
    }

    @PutMapping(value = "/product", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@RequestBody ProductDTO productDto) {
        return ResponseEntity.ok(productService.update(productDto));
    }


    @PostMapping(value = "/products", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Flux<?>> findById(@RequestBody List<String> skus) {
        return ResponseEntity.ok(productService.findAll(skus));
    }

    @PostMapping(value = "/product/errors", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Mono<?>> errors(@RequestBody @Valid ProductDTO productDto) throws Exception {
        return ResponseEntity.ok(productService.errorController(productDto));
    }
}

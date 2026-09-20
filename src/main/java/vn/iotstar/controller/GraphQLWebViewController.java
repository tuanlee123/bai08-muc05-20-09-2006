package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ql-graphql")
public class GraphQLWebViewController {

    // Công cụ GraphiQL IDE độc lập (không lo bị lỗi CORS unpkg.com)
    @GetMapping("/ide")
    public String ide() {
        return "graphql/graphiql-tool";
    }

    @GetMapping("/home")
    public String home() {
        return "graphql/graphql-home";
    }

    @GetMapping("/categories")
    public String categories() {
        return "graphql/graphql-categories";
    }

    @GetMapping("/products")
    public String products() {
        return "graphql/graphql-products";
    }
}
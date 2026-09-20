package vn.iotstar.filter;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setCustomDecoratorSelector(new DecoratorSelector<WebAppContext>() {
            @Override
            public String[] selectDecoratorPaths(Content content, WebAppContext context) {
                String path = context.getPath();

                // Loại trừ: GraphiQL, GraphQL, API, tài nguyên tĩnh
                if (path.startsWith("/graphiql")
                        || path.startsWith("/graphql")
                        || path.startsWith("/ql-graphql")
                        || path.startsWith("/vendor")
                        || path.startsWith("/api/")
                        || path.startsWith("/static/")
                        || path.startsWith("/uploads/")
                        || path.startsWith("/login") 
                        || path.startsWith("/register") 
                        || path.startsWith("/verify-otp") 
                        || path.startsWith("/forgot-password") 
                        || path.startsWith("/reset-password") 
                        || path.startsWith("/image") 
                        || path.startsWith("/assets/")) {
                    return new String[0];
                }

                if (path.startsWith("/admin") || path.contains("/views/admin/")) {
                    return new String[] { "/views/layouts/admin.jsp" };
                }

                return new String[] { "/views/layouts/web.jsp" };
            }
        });
    }
}
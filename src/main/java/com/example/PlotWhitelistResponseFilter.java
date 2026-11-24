package com.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class PlotWhitelistResponseFilter extends OncePerRequestFilter {
    private static final Set<String> ALLOWED_IP_ADDRESSES = Set.of(
            "51.222.245.229",
            "51.222.244.138",
            "148.113.223.138",
            "127.0.0.1",
            "0:0:0:0:0:0:0:1"
    );

    private static final String ALLOWED_PLOT = "Hypercube/<Patch> (<Plot ID>, <Plot Owner>)";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        if(!ALLOWED_IP_ADDRESSES.contains(ip.trim())) {
            LoggerFactory.getLogger(PlotWhitelistResponseFilter.class)
                    .info("Invalid IP {} attempted to connect, blocking.", ip);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String userAgent = request.getHeader("User-Agent");
        if(!ALLOWED_PLOT.equals(userAgent)) {
            LoggerFactory.getLogger(PlotWhitelistResponseFilter.class)
                    .info("Invalid user agent {} attempted to connect, blocking.", userAgent);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

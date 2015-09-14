package io.siz.web.rest.siz;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

/**
 * Réimplémente /mappings pour respecter l'interface legacy. Mais plus complete
 * et mise à jour automatiquement.
 *
 * {
 * "links": { "stories": "https://api.siz.io/stories", "users":
 * "https://api.siz.io/users", "usernames": "https://api.siz.io/usernames",
 * "tokens": "https://api.siz.io/tokens", "emails": "https://api.siz.io/emails"
 * } }
 *
 * @author fred
 */
@RestController
public class IndexEndpoint implements ApplicationContextAware {

    /**
     * la regex \{\[(.*)\]\} matche [ {[/events]} , /events ] dans {[/events]}
     */
    private Pattern route = Pattern.compile("\\[(.*?)\\]");

    private ApplicationContext applicationContext;

    @RequestMapping(value = "/index",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map<String, String>> index(HttpServletRequest request) {

        Map<String, Map<String, String>> newHashMap = Maps.<String, Map<String, String>>newHashMap();

        newHashMap.put("links", extractMethodMappings(applicationContext)
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(entry
                                -> {
                            Matcher m = route.matcher(entry.getKey());
                            m.find();
                            final String controllerName = m.group(1).replaceAll("/", "");
                            /**
                             * on change {[/foo]} en foo , et si c'est la
                             * racine, c-a-d "/" -> "")on indique home plutot
                             * que string vide.
                             */
                            return !"".equals(controllerName) ? controllerName : "home";
                        },
                        entry
                        -> {
                            Matcher m = route.matcher(entry.getKey());
                            m.find();
                            return "//" + request.getLocalName() + m.group(1);
                        },
                        (t, u) -> {
                            // s'il y a plusieurs fois la meme clé, on ignore la seconde.
                            // SI on veut plus de détails, il faut utiliser la route /mappings
                            return t;
                        })
                ));

        return newHashMap;
    }

    /**
     * Méthode issue de {@link RequestMappingEndpoint}
     *
     * @param applicationContext
     * @return
     */
    protected Map<String, Object> extractMethodMappings(ApplicationContext applicationContext) {
        Map<String, Object> result = new HashMap<>();
        if (applicationContext != null) {
            for (String name : applicationContext.getBeansOfType(
                    AbstractHandlerMethodMapping.class).keySet()) {
                @SuppressWarnings("unchecked")
                Map<?, HandlerMethod> methods = applicationContext.getBean(name,
                        AbstractHandlerMethodMapping.class).getHandlerMethods();
                methods.keySet().stream().forEach((key) -> {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("bean", name);
                    map.put("method", methods.get(key).toString());
                    result.put(key.toString(), map);
                });
            }
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
